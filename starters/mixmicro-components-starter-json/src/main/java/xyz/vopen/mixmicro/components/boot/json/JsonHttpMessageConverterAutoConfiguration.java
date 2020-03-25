package xyz.vopen.mixmicro.components.boot.json;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;

import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static xyz.vopen.mixmicro.components.boot.json.JsonProperties.JSON_PROPERTIES_PREFIX;

/**
 * {@link JsonHttpMessageConverterAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-03-19.
 */
@Configuration
@ConditionalOnClass(HttpMessageConverters.class)
@EnableConfigurationProperties(JsonProperties.class)
public class JsonHttpMessageConverterAutoConfiguration {

  private static final Logger log =
      LoggerFactory.getLogger(JsonHttpMessageConverterAutoConfiguration.class);

  @Bean
  @Primary
  @ConditionalOnClass(FastJsonHttpMessageConverter.class)
  @ConditionalOnProperty(
      prefix = JSON_PROPERTIES_PREFIX,
      value = "fastjson.enabled",
      havingValue = "true",
      matchIfMissing = true)
  public HttpMessageConverters fastJsonHttpMessageConverters(JsonProperties jsonProperties) {
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    List<SerializerFeature> features =
        Lists.newArrayList(WriteDateUseDateFormat, WriteNullBooleanAsFalse, WriteNullNumberAsZero);

    if (jsonProperties.isWriteNullStringAsEmpty()) {
      features.add(WriteNullStringAsEmpty);
    }

    if (jsonProperties.isWriteNonStringValueAsString()) {
      features.add(WriteNonStringKeyAsString);
    }

    if (jsonProperties.isWriteNullListAsEmpty()) {
      features.add(WriteNullListAsEmpty);
    }

    fastJsonConfig.setSerializerFeatures(features.toArray(new SerializerFeature[0]));
    fastJsonConfig.setDateFormat(jsonProperties.getDefaultDateFormat());

    converter.setFastJsonConfig(fastJsonConfig);
    converter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON));
    log.info(" == Http Message Converter:[{}] is created.", converter);
    return new HttpMessageConverters(converter);
  }
}
