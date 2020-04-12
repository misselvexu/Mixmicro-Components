package xyz.vopen.mixmicro.components.authorization.api;

import xyz.vopen.mixmicro.components.authorization.*;
import xyz.vopen.mixmicro.components.authorization.exception.AuthorizationException;
import xyz.vopen.mixmicro.components.authorization.exception.ExpiredAccessTokenException;
import xyz.vopen.mixmicro.components.authorization.exception.IllegalAccessTokenException;
import xyz.vopen.mixmicro.kits.lang.NonNull;

import static xyz.vopen.mixmicro.components.authorization.AuthorizationConfig.DEFAULT;

/**
 * {@link AuthorizationService}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
public interface AuthorizationService {

  /**
   * Get Authorization Config Properties
   * @return instance of {@link AuthorizationConfig}
   */
  default AuthorizationConfig getConfig() {
    return DEFAULT;
  }

  /**
   * Generate New Access Token With Defined {@link Payload}
   *
   * @param payload payload instance
   * @return instance of {@link AccessToken}
   * @throws AuthorizationException maybe thrown {@link AuthorizationException}
   */
  <P extends Payload> Token generateToken(@NonNull P payload) throws AuthorizationException;

  /**
   * Check provide {@link AccessToken} is valid .
   *
   * @param accessToken instance of {@link AccessToken}
   * @throws IllegalAccessTokenException thrown {@link IllegalAccessTokenException} when illegal
   *     access token provide
   * @throws ExpiredAccessTokenException thrown {@link ExpiredAccessTokenException} when access
   *     token is expired
   */
  void validateToken(@NonNull String accessToken) throws IllegalAccessTokenException, ExpiredAccessTokenException;

  /**
   * Decode Access Token's Payload instance
   * @param accessToken Access Token Instance
   * @param clazz Target custom class type of {@link Payload}
   * @return instance result of {@link Payload}
   * @throws AuthorizationException maybe thrown {@link AuthorizationException}
   */
  <P extends Payload> P decodeTokenPayload(@NonNull String accessToken, Class<P> clazz) throws AuthorizationException;
}
