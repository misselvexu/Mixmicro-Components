package xyz.vopen.mixmicro.components.boot.web.autoconfigure;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * {@link MixmicroWebMvcConfigurer}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-05-07.
 */
@Configuration
public class MixmicroWebMvcConfigurer implements WebMvcConfigurer {

  // FIX default string http message converter charset.

  @Bean
  HttpMessageConverter<String> responseBodyConverter() {
    StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
    converter.setSupportedMediaTypes(
        Collections.singletonList(new MediaType("text", "plain", StandardCharsets.UTF_8)));
    return converter;
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(responseBodyConverter());
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.favorPathExtension(false);
  }

  @Bean
  public FilterRegistrationBean<Filter> filterRegistrationBean() {
    FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    characterEncodingFilter.setForceEncoding(true);
    characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
    registrationBean.setFilter(characterEncodingFilter);
    return registrationBean;
  }
}
