package xyz.vopen.mixmicro.components.boot.web.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.boot.web.Marker;
import xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties;

import static xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties.MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX;

/**
 * {@link MixmicroWebAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
@Configuration
@ConditionalOnProperty(
    prefix = MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX,
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@EnableConfigurationProperties(MixmicroWebConfigProperties.class)
@ComponentScan(basePackageClasses = Marker.class)
public class MixmicroWebAutoConfiguration {



}
