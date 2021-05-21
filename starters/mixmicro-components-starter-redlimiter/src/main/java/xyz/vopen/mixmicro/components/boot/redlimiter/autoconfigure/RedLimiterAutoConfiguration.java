package xyz.vopen.mixmicro.components.boot.redlimiter.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.boot.redlimiter.RedLimiterService;

/**
 * {@link RedLimiterAutoConfiguration}
 *
 * <p>Class RedLimiterAutoConfiguration Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/5/21
 */
@Configuration
@EnableConfigurationProperties(RedLimiterProperties.class)
public class RedLimiterAutoConfiguration {


  // ~~ red limiter

  @Bean
  public RedLimiterService redLimiterService() {
    return new RedLimiterService();
  }

}
