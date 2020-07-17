package xyz.vopen.mixmicro.components.boot.dbm;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * {@link DBMRouterProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/17/20
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = DBMRouterProperties.MIXMICRO_DBM_CONFIG_PROPERTIES_PREFIX)
public class DBMRouterProperties implements Serializable {

  public static final String MIXMICRO_DBM_CONFIG_PROPERTIES_PREFIX = "mixmicro.dbm";

  private boolean enabled = true;

}
