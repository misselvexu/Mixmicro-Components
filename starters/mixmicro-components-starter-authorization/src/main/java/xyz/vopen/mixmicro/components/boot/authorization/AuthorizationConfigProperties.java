package xyz.vopen.mixmicro.components.boot.authorization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.vopen.mixmicro.components.authorization.AuthorizationConfig;

import static xyz.vopen.mixmicro.components.boot.authorization.AuthorizationConfigProperties.AUTHORIZATION_PROPERTIES_PREFIX;

/**
 * {@link AuthorizationConfigProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-11.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = AUTHORIZATION_PROPERTIES_PREFIX)
public class AuthorizationConfigProperties extends AuthorizationConfig {


  public static final String AUTHORIZATION_PROPERTIES_PREFIX = "mixmicro.authorization";

  // DEFAULT CONSTRUCTOR

  public AuthorizationConfigProperties() {
  }

}
