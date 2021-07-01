package xyz.vopen.mixmicro.components.boot.snowflake.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.boot.snowflake.Snowflake;
import xyz.vopen.mixmicro.components.boot.snowflake.SnowflakeProperties;

/**
 * {@link SnowflakeAutoConfiguration}
 *
 * <p>Class SnowflakeAutoConfiguration Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/6/17
 */
@Configuration
@EnableConfigurationProperties(SnowflakeProperties.class)
public class SnowflakeAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(Snowflake.class)
  public Snowflake snowflake(SnowflakeProperties properties) {
    return new Snowflake(properties);
  }
}
