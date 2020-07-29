package xyz.vopen.mixmicro.components.boot.logging.tracing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import xyz.vopen.framework.logging.client.http.rest.LoggingRestTemplateInterceptor;

import java.util.Arrays;
import java.util.List;

/**
 * Mixmicro Boot Logging ResTemplate Config {@link RestTemplate} Setting Interceptor Transmit Link
 * Information
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnBean(RestTemplate.class)
public class MixmicroLoggingTracingRestTemplateAutoConfiguration {

  public MixmicroLoggingTracingRestTemplateAutoConfiguration(RestTemplate restTemplate) {

    List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

    LoggingRestTemplateInterceptor interceptor = new LoggingRestTemplateInterceptor();

    if (ObjectUtils.isEmpty(interceptors)) {
      restTemplate.setInterceptors(Arrays.asList(interceptor));
    } else {
      interceptors.add(interceptor);
    }
  }
}
