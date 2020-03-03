package xyz.vopen.mixmicro.components.boot.json;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * {@link JsonHttpMessageConverterAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-03-19.
 */
@Configuration
@ConditionalOnClass(HttpMessageConverters.class)
public class JsonHttpMessageConverterAutoConfiguration {

  private static final Logger log =
      LoggerFactory.getLogger(JsonHttpMessageConverterAutoConfiguration.class);

  @Bean
  @ConditionalOnClass(FastJsonHttpMessageConverter.class)
  public HttpMessageConverters fastJsonHttpMessageConverters() {
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(
        SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.WriteDateUseDateFormat);
    converter.setFastJsonConfig(fastJsonConfig);
    log.info(" == Http Message Converter:[{}] is created.", converter);
    return new HttpMessageConverters((HttpMessageConverter<?>) converter);
  }
}
