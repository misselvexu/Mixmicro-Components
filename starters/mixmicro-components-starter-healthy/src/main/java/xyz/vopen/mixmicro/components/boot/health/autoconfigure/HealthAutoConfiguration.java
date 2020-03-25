package xyz.vopen.mixmicro.components.boot.health.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import xyz.vopen.mixmicro.components.boot.health.HealthProperties;

import static xyz.vopen.mixmicro.components.boot.health.HealthProperties.HEALTH_PROPERTIES_PREFIX;

/**
 * {@link HealthAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/25
 */
@ConditionalOnProperty(
    prefix = HEALTH_PROPERTIES_PREFIX,
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@EnableConfigurationProperties(HealthProperties.class)
public class HealthAutoConfiguration {

}
