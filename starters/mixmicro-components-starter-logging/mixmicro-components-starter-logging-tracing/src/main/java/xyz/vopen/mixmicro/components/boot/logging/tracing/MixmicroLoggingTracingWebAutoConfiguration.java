package xyz.vopen.mixmicro.components.boot.logging.tracing;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.vopen.framework.logging.client.LoggingFactoryBean;
import xyz.vopen.framework.logging.client.interceptor.LoggingInterceptor;
import xyz.vopen.framework.logging.client.interceptor.web.LoggingWebInterceptor;

/**
 * Mixmicro Boot logging web auto configuration registry logging request interceptor {@link
 * LoggingInterceptor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass(LoggingFactoryBean.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(MixmicroLoggingTracingProperties.class)
@AutoConfigureAfter(MixmicroLoggingTracingAutoConfiguration.class)
public class MixmicroLoggingTracingWebAutoConfiguration implements WebMvcConfigurer {
  private LoggingWebInterceptor loggingWebInterceptor;
  private MixmicroLoggingTracingProperties loggingProperties;

  public MixmicroLoggingTracingWebAutoConfiguration(
      LoggingWebInterceptor loggingWebInterceptor, MixmicroLoggingTracingProperties loggingProperties) {
    this.loggingWebInterceptor = loggingWebInterceptor;
    this.loggingProperties = loggingProperties;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(loggingWebInterceptor)
        .addPathPatterns(loggingProperties.getLoggingPathPrefix());
  }
}
