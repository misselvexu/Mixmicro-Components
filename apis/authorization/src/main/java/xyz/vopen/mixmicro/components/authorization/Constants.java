package xyz.vopen.mixmicro.components.authorization;

/**
 * {@link Constants}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
public final class Constants {

  // DEFAULT CONSTRUCTOR

  private Constants() {}

  public static final String AUTHORIZATION_LOG_NAME = "mixmicro-authorization";

  public static final String DATE_FORMAT_PATTEN = "yyyy-MM-dd HH:mm:ss";

  public static final String DEFAULT_CHARSET = "UTF-8";

  public static final long DEFAULT_PERIOD_OF_VALIDITY_MS = 7 * 24 * 60 * 60 * 1000;

  public static final long DEFAULT_PERIOD_OF_REFRESH_TOKEN_VALIDITY_MS = 30 * 24 * 60 * 60 * 1000;

  public static final String RESOURCE_PREFIX = "META-INF/";

  public static final String DEFAULT_RSA_PRIVATE_KEY_PEM_FILE = "mixmicro_default_authorization_pkcs8_private_key.pem";

  public static final String DEFAULT_RSA_PUBLIC_KEY_PEM_FILE = "mixmicro_default_authorization_rsa_public_key.pem";
}
