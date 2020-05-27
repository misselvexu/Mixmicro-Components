package xyz.vopen.mixmicro.components.boot.openfeign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.boot.openfeign.OpenFeignConfigProperties;
import xyz.vopen.mixmicro.components.boot.openfeign.env.ContextEnvironmentFactory;

import java.util.Map;

import static xyz.vopen.mixmicro.components.common.MixmicroConstants.MIXMICRO_SERVICE_INVOKE_HEADER;

/**
 * {@link OpenFeignPreInvokeInterceptor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/20
 */
public class OpenFeignPreInvokeInterceptor implements RequestInterceptor {

  private static final Logger log = LoggerFactory.getLogger(OpenFeignPreInvokeInterceptor.class);

  private final ContextEnvironmentFactory factory = ContextEnvironmentFactory.instance();

  /**
   * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
   *
   * @param template Request
   */
  @Override
  public void apply(RequestTemplate template) {

    try{
      template.header(MIXMICRO_SERVICE_INVOKE_HEADER, "true");

      OpenFeignConfigProperties properties = factory.openFeignConfigProperties();

      OpenFeignConfigProperties.TransportMetadata metadata = properties.getMetadata();

      Map<String, String> keys = metadata.getEnvKeys();

      keys.keySet()
          //fix: CPU 100% RISK
          // removed.
//          .parallelStream()
          .forEach(
              key -> {
                String envKey = keys.get(key);
                String value = factory.getProperty(envKey, "");
                String headerKey = metadata.getPrefix().concat(key);
                if(metadata.isEnvKeySensitive()) {
                  headerKey = headerKey.toUpperCase();
                }
                template.header(headerKey, value);
              });
    } catch (Exception e) {
      // 防御性容错
      log.warn("[==IGNORE==] openfeign client pre invoke set header(s) .");
    }
  }
}
