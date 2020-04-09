package xyz.vopen.mixmicro.components.boot.mq.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;

/**
 * {@link MessageQueueAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
@Configuration
@EnableConfigurationProperties(ConfigurationProperties.class)
@EnableIntegration
public class MessageQueueAutoConfiguration {



}
