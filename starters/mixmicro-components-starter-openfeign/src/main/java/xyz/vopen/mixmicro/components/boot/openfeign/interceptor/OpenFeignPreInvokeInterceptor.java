package xyz.vopen.mixmicro.components.boot.openfeign.interceptor;

import com.google.common.collect.Lists;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.vopen.mixmicro.components.boot.openfeign.OpenFeignConfigProperties;
import xyz.vopen.mixmicro.components.boot.openfeign.core.FeignAttributes;
import xyz.vopen.mixmicro.components.boot.openfeign.env.ContextEnvironmentFactory;
import xyz.vopen.mixmicro.kits.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

  private static final String EMPTY_STRING = "";


  private Map<String, Collection<String>> covertRequestHeaders(RequestTemplate template) {

    Map<String, Collection<String>> copyHeaders = template.headers();

    template.headers().clear();

    return copyHeaders;

  }

  /**
   * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
   *
   * @param template Request
   */
  @Override
  public void apply(RequestTemplate template) {

    OpenFeignConfigProperties properties0 = factory.openFeignConfigProperties();

    // Process Sensitive Headers
    if(!properties0.getSensitiveHeaders().isEmpty()) {
      try{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
          HttpServletRequest request = attributes.getRequest();

          // convert headers
          Map<String, Collection<String>> theaders = covertRequestHeaders(template);

          for (String header : properties0.getSensitiveHeaders()) {
            String value = Optional.ofNullable(request.getHeader(header)).orElse(EMPTY_STRING);
            if (!StringUtils.isBlank(value)) {
              if(theaders.containsKey(header)) {
                theaders.remove(header);
              }

              theaders.put(header, Lists.newArrayList(Optional.ofNullable(request.getHeader(header)).orElse(EMPTY_STRING)));
            }
          }

          template.headers(theaders);
        }
      } catch (Exception ignored) {}
    }

    // Process Attributes

    List<OpenFeignConfigProperties.TransportAttribute> attributes = properties0.getAttributes();

    if(!attributes.isEmpty()) {

      try{
        for (OpenFeignConfigProperties.TransportAttribute attribute : attributes) {
          String name = attribute.getName();
          try{
            switch (attribute.getType()) {
              case MANUAL:
                Map<String, String> temp = FeignAttributes.getAttributes();
                if(temp.containsKey(name)) {

                  // convert headers
                  Map<String, Collection<String>> theaders = covertRequestHeaders(template);

                  String value = Optional.ofNullable(temp.get(name)).orElse(EMPTY_STRING);
                  if (!StringUtils.isBlank(value)) {
                    if(theaders.containsKey(name)) {
                      theaders.remove(name);
                    }

                    theaders.put(name, Lists.newArrayList(Optional.ofNullable(temp.get(name)).orElse(EMPTY_STRING)));
                  }
                }
                break;
              case REQUEST_HEADER:
                try{
                  ServletRequestAttributes attributes0 = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                  if (attributes0 != null) {
                    HttpServletRequest request = attributes0.getRequest();

                    // convert headers
                    Map<String, Collection<String>> theaders = covertRequestHeaders(template);

                    String value = Optional.ofNullable(request.getHeader(name)).orElse(EMPTY_STRING);
                    if (!StringUtils.isBlank(value)) {

                      if(theaders.containsKey(name)) {
                        theaders.remove(name);
                      }

                      theaders.put(name, Lists.newArrayList(Optional.ofNullable(request.getHeader(name)).orElse(EMPTY_STRING)));
                    }
                  }
                } catch (Exception ignored) {}
                break;
              default:
                break;
            }
          } catch (Exception e) {
            log.warn("[Feign Interceptor] key name: {} process failed. " , name, e);
          }
        }
      } finally{
        FeignAttributes.remove();
      }
    }


    // Process Customer Metadata(s)
    try{
      template.header(MIXMICRO_SERVICE_INVOKE_HEADER, "true");

      OpenFeignConfigProperties.TransportMetadata metadata = properties0.getMetadata();

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
