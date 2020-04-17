package xyz.vopen.mixmicro.components.boot.openfeign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import xyz.vopen.mixmicro.components.boot.openfeign.OpenFeignConfigProperties;
import xyz.vopen.mixmicro.components.boot.openfeign.env.ContextEnvironmentFactory;

import java.util.Set;

import static xyz.vopen.mixmicro.components.common.MixmicroConstants.MIXMICRO_SERVICE_INVOKE_HEADER;

/**
 * {@link OpenFeignPreInvokeInterceptor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/20
 */
public class OpenFeignPreInvokeInterceptor implements RequestInterceptor {

  private ContextEnvironmentFactory factory = ContextEnvironmentFactory.instance();

  /**
   * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
   *
   * @param template Request
   */
  @Override
  public void apply(RequestTemplate template) {

    template.header(MIXMICRO_SERVICE_INVOKE_HEADER, "true");

    OpenFeignConfigProperties properties = factory.openFeignConfigProperties();

    OpenFeignConfigProperties.TransportMetadata metadata = properties.getMetadata();

    Set<String> keys = metadata.getEnvKeys();

    for (String key : keys) {
      String value = factory.getProperty(key, "");
      template.header(key, value);
    }
  }
}
