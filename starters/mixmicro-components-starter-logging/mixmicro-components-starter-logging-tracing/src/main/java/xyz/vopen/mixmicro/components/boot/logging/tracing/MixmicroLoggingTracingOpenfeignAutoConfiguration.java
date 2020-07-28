package xyz.vopen.mixmicro.components.boot.logging.tracing;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.framework.logging.client.http.openfeign.LoggingOpenFeignInterceptor;

/**
 * Mixmicro Boot Logging Openfeign Http Away Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass({RequestInterceptor.class, LoggingOpenFeignInterceptor.class})
@EnableConfigurationProperties(MixmicroLoggingTracingProperties.class)
public class MixmicroLoggingTracingOpenfeignAutoConfiguration {

  /**
   * Mixmicro Boot Logging Openfeign Interceptor
   *
   * @return ApiBootLogOpenFeignInterceptor
   */
  @Bean
  @ConditionalOnMissingBean
  public LoggingOpenFeignInterceptor apiBootLogOpenFeignInterceptor() {
    return new LoggingOpenFeignInterceptor();
  }
}
