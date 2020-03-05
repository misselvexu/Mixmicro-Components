package xyz.vopen.mixmicro.components.boot.openfeign.autoconfigure;

import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.vopen.mixmicro.components.boot.openfeign.OpenFeignConfigProperties;
import xyz.vopen.mixmicro.components.boot.openfeign.decoder.OpenFeignDecoder;
import xyz.vopen.mixmicro.components.boot.openfeign.decoder.OpenFeignInvokeErrorDecoder;

import static xyz.vopen.mixmicro.components.boot.openfeign.OpenFeignConfigProperties.OPENFEIGN_PROPERTIES_PREFIX;

/**
 * {@link OpenFeignAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
@Configuration
@ConditionalOnClass(
    name = {
      "org.springframework.cloud.openfeign.FeignContext",
      "xyz.vopen.mixmicro.components.common.ResponseEntity"
    })
@EnableConfigurationProperties(OpenFeignConfigProperties.class)
public class OpenFeignAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(OpenFeignAutoConfiguration.class);

  @Bean
  @Primary
  @ConditionalOnMissingBean
  @ConditionalOnProperty(
      prefix = OPENFEIGN_PROPERTIES_PREFIX,
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
    log.info("[MIXMICRO] initializing openfeign uniform result converter .");
    return new OpenFeignDecoder(
        new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters))));
  }

  @Bean
  public OpenFeignInvokeErrorDecoder errorDecoder() {
    return new OpenFeignInvokeErrorDecoder();
  }

}
