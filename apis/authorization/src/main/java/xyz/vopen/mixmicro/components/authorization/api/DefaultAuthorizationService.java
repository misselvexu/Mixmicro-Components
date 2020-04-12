package xyz.vopen.mixmicro.components.authorization.api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.authorization.*;
import xyz.vopen.mixmicro.components.authorization.exception.AuthorizationException;
import xyz.vopen.mixmicro.components.authorization.exception.ExpiredAccessTokenException;
import xyz.vopen.mixmicro.components.authorization.exception.IllegalAccessTokenException;
import xyz.vopen.mixmicro.kits.lang.NonNull;
import xyz.vopen.mixmicro.kits.lang.Nullable;
import xyz.vopen.mixmicro.kits.timed.TimedIdGenerator;

import java.util.Date;

import static xyz.vopen.mixmicro.components.authorization.Constants.*;
import static xyz.vopen.mixmicro.components.authorization.Serialization.*;

/**
 * {@link DefaultAuthorizationService}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-12.
 */
public class DefaultAuthorizationService implements AuthorizationService {

  private final AuthorizationConfig config;

  private final Serialization.AuthorizationKeyLoader keyLoader;

  private final Serialization.Cipher cipher;

  private final TimedIdGenerator idGenerator = new TimedIdGenerator();

  // DEFAULT CONSTRUCTOR

  public DefaultAuthorizationService(
      AuthorizationConfig config, Serialization.AuthorizationKeyLoader keyLoader) {
    this.keyLoader = keyLoader;
    if (config != null) {
      if (StringUtils.isAnyBlank(config.getPrivateKeyPem(), config.getPublicKeyPem())) {
        this.config = AuthorizationConfig.DEFAULT;
      } else {
        this.config = config;
      }
    } else {
      this.config = AuthorizationConfig.DEFAULT;
    }

    // init cipher

    cipher =
        new Serialization.Cipher(
            this.keyLoader.loadPublicKey(this.config.getPublicKeyPem()),
            this.keyLoader.loadPrivateKey(this.config.getPrivateKeyPem()));
  }

  private static final Logger log = LoggerFactory.getLogger(AUTHORIZATION_LOG_NAME);

  /**
   * Get Authorization Config Properties
   *
   * @return instance of {@link AuthorizationConfig}
   */
  @Override
  public AuthorizationConfig getConfig() {
    return this.config;
  }

  /**
   * Generate New Access Token With Defined {@link Payload}
   *
   * @param payload payload instance
   * @return instance of {@link AccessToken}
   * @throws AuthorizationException maybe thrown {@link AuthorizationException}
   */
  @Override
  public <P extends Payload> Token generateToken(@NonNull P payload) throws AuthorizationException {

    try {

      if (this.config.isDebug()) {
        log.info("request create new access token with payload : {}", toJsonString(payload));
      }

      long gts = System.currentTimeMillis();

      AccessToken.AccessTokenBuilder tokenBuilder =
          AccessToken.builder()
              .tokenUniqueId(idGenerator.generate())
              .generateTimestamp(gts)
              .expiredTimestamp(gts + DEFAULT_PERIOD_OF_VALIDITY_MS)
              .payload(toJsonByte(payload));

      if (this.config.getAccessTokenTimeToLive() > 0) {
        tokenBuilder.expiredTimestamp(gts + this.config.getAccessTokenTimeToLive());
      }

      AccessToken accessToken = tokenBuilder.build();

      if (this.config.isDebug()) {
        log.info("access token instance :{}", toJsonString(accessToken));
      }

      RefreshToken.RefreshTokenBuilder refreshTokenBuilder = RefreshToken.builder().accessTokenId(accessToken.getTokenUniqueId());

      refreshTokenBuilder.generateTimestamp(gts)
          .expiredTimestamp(gts + DEFAULT_PERIOD_OF_REFRESH_TOKEN_VALIDITY_MS);

      if (this.config.getRefreshTokenTimeToLive() > 0) {
        refreshTokenBuilder.expiredTimestamp(gts + this.config.getRefreshTokenTimeToLive());
      }

      RefreshToken refreshToken = refreshTokenBuilder.build();

      if (this.config.isDebug()) {
        log.info("refresh token instance :{}", toJsonString(refreshToken));
      }

      Token token =
          Token.builder()
              .tokenId(accessToken.getTokenUniqueId())
              .accessToken(cipher.publicEncrypt(toJsonString(accessToken)))
              .refreshToken(cipher.publicEncrypt(toJsonString(refreshToken)))
              .expiredTime(this.config.getAccessTokenTimeToLive())
              .build();

      if (this.config.isDebug()) {
        log.info("Token : {}", toJsonString(token));
      }

      return token;

    } catch (Exception e) {
      throw new AuthorizationException("Generate token failed ", e);
    }
  }

  /**
   * Check provide {@link AccessToken} is valid .
   *
   * @param accessToken instance of {@link AccessToken}
   * @throws IllegalAccessTokenException thrown {@link IllegalAccessTokenException} when illegal
   *     access token provide
   * @throws ExpiredAccessTokenException thrown {@link ExpiredAccessTokenException} when access
   *     token is expired
   */
  @Override
  public void validateToken(@NonNull String accessToken)
      throws IllegalAccessTokenException, ExpiredAccessTokenException {

    try {

      if (StringUtils.isBlank(accessToken)) {
        throw new IllegalAccessTokenException("access token not be null. ");
      }

      String decryptToken = cipher.privateDecrypt(accessToken);

      if (StringUtils.isNoneBlank(decryptToken)) {

        AccessToken token = fromJson(decryptToken, AccessToken.class);

        if (token == null) {
          throw new IllegalAccessTokenException("invalid access token payload . ");
        }

        if (System.currentTimeMillis() > token.getExpiredTimestamp()) {
          throw new ExpiredAccessTokenException("token: [" + token.getTokenUniqueId() + "] is expired. ", new Date(token.getExpiredTimestamp()));
        }
      }
    } catch (IllegalAccessTokenException | ExpiredAccessTokenException e) {
      throw e;
    } catch (Exception e) {
      throw new AuthorizationException(e);
    }
  }

  @Nullable
  private AccessToken decodeToken(@NonNull String accessToken) {
    try{
      String decryptToken = cipher.privateDecrypt(accessToken);
      return fromJson(decryptToken, AccessToken.class);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Decode Access Token's Payload instance
   *
   * @param accessToken Access Token Instance
   * @param clazz       Target custom class type of {@link Payload}
   * @return instance result of {@link Payload}
   * @throws AuthorizationException maybe thrown {@link AuthorizationException}
   */
  @Override
  public <P extends Payload> P decodeTokenPayload(@NonNull String accessToken, Class<P> clazz) throws AuthorizationException {

    try {

      validateToken(accessToken);

      AccessToken token = decodeToken(accessToken);

      if (token == null) {
        throw new AuthorizationException("decode token payload failed. ");
      }


      return fromJson(token.getPayload(),clazz);

    } catch (AuthorizationException e) {
      throw e;
    } catch (Exception e) {
      throw new AuthorizationException(e);
    }
  }
}
