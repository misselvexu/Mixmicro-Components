package xyz.vopen.mixmicro.components.boot.web.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties;
import xyz.vopen.mixmicro.components.boot.web.aspect.WebApiAspect;
import xyz.vopen.mixmicro.components.boot.web.core.advice.MixmicroExceptionAdvice;
import xyz.vopen.mixmicro.components.boot.web.core.advice.MixmicroResponseBodyAdvice;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
public class MixmicroWebAutoConfiguration implements WebMvcConfigurer {

  @Bean
  @Primary
  @ConditionalOnProperty(
      prefix = MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX,
      value = "response-wrap-advice",
      havingValue = "true",
      matchIfMissing = true)
  MixmicroExceptionAdvice mixmicroExceptionAdvice() {
    return new MixmicroExceptionAdvice();
  }

  @Bean
  @Primary
  @ConditionalOnProperty(
      prefix = MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX,
      value = "global-exception-handler-advice",
      havingValue = "true",
      matchIfMissing = true)
  MixmicroResponseBodyAdvice mixmicroResponseBodyAdvice() {
    return new MixmicroResponseBodyAdvice();
  }

  @Bean
  @ConditionalOnProperty(
      prefix = MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX + ".log",
      value = "enabled-request-log",
      havingValue = "true",
      matchIfMissing = true)
  WebApiAspect webApiAspect(MixmicroWebConfigProperties properties) {
    return new WebApiAspect(properties);
  }

  // FIX default string http message converter charset.

  @Bean
  @Primary
  StringHttpMessageConverter stringHttpMessageConverter() {
    return new StringHttpMessageConverter(StandardCharsets.UTF_8);
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(stringHttpMessageConverter());
  }
}
