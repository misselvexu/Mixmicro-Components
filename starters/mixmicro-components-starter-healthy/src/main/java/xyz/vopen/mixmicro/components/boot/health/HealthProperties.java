package xyz.vopen.mixmicro.components.boot.health;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

import static xyz.vopen.mixmicro.components.boot.health.HealthProperties.HEALTH_PROPERTIES_PREFIX;

/**
 * {@link HealthProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/25
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = HEALTH_PROPERTIES_PREFIX)
public class HealthProperties implements Serializable {

  public static final String HEALTH_PROPERTIES_PREFIX = "mimicro.health";

  private boolean enabled = true;

}
