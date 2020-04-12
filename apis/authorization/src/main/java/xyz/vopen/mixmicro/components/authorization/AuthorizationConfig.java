package xyz.vopen.mixmicro.components.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static xyz.vopen.mixmicro.components.authorization.Constants.*;

/**
 * {@link AuthorizationConfig}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-11.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthorizationConfig implements Serializable {

  /**
   * Default Resource Instance Config
   *
   * <p>
   */
  public static final AuthorizationConfig DEFAULT =
      new AuthorizationConfig() {
        @Override
        public String getPrivateKeyPem() {
          return RESOURCE_PREFIX + DEFAULT_RSA_PRIVATE_KEY_PEM_FILE;
        }

        @Override
        public String getPublicKeyPem() {
          return RESOURCE_PREFIX + DEFAULT_RSA_PUBLIC_KEY_PEM_FILE;
        }
      };

  // DEFAULT CONSTRUCTOR

  public AuthorizationConfig() {}

  /**
   * RSA Public Key File Resource Path .
   *
   * <p>default: <code>
   * {@link Constants#RESOURCE_PREFIX} + {@link Constants#DEFAULT_RSA_PUBLIC_KEY_PEM_FILE}</code>
   */
  private String publicKeyPem;

  /**
   * RSA Private Key File Resource Path .
   *
   * <p>default: <code>
   * {@link Constants#RESOURCE_PREFIX} + {@link Constants#DEFAULT_RSA_PRIVATE_KEY_PEM_FILE}</code>
   */
  private String privateKeyPem;

  /**
   * Debug config
   *
   * <p>default: false
   */
  private boolean debug;

  /**
   * Access Token Valid Live Time
   *
   * <p>default: 7 days
   */
  private long accessTokenTimeToLive = DEFAULT_PERIOD_OF_VALIDITY_MS;

  /**
   * Refresh Token Valid Live Time
   * <p>default: 30 days</p>
   */
  private long refreshTokenTimeToLive = DEFAULT_PERIOD_OF_REFRESH_TOKEN_VALIDITY_MS;
}
