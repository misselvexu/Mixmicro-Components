package xyz.vopen.mixmicro.components.boot.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import xyz.vopen.mixmicro.components.exception.defined.MixmicroException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties.MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX;
import static xyz.vopen.mixmicro.components.common.SerializableBean.encode;

/**
 * {@link MixmicroWebConfigProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX)
public class MixmicroWebConfigProperties implements Serializable, InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(MixmicroWebConfigProperties.class);

  public static final String MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX = "mixmicro.spring.web";

  @Value("${spring.application.name:application}")
  private String appname;

  private boolean enabled = true;

  private boolean responseWrapAdvice = true;

  private boolean globalExceptionHandlerAdvice = true;

  @NestedConfigurationProperty private ExceptionConfig exception = new ExceptionConfig();

  @NestedConfigurationProperty private ResponseConfig response = new ResponseConfig();

  @NestedConfigurationProperty private LogConfig log = new LogConfig();

  @Override
  public void afterPropertiesSet() throws Exception {
    response.afterPropertiesSet();
  }

  @Data
  public static class ResponseConfig implements Serializable, InitializingBean {

    private int defaultSuccessResponseCode = 0;

    @Getter(AccessLevel.PRIVATE)
    private static final List<String> DEFAULT_URIS = Lists.newArrayList("/actuator/prometheus", "/v2/api-docs", "/swagger-resources", "/swagger-ui.html", "/webjars");

    /**
     * Config Full Request Context URL .
     *
     * <p>${context-path}/${url}
     */
    private List<String> ignoreWrapUris = Lists.newArrayList();

    @Override
    public void afterPropertiesSet() throws Exception {
      ignoreWrapUris.addAll(DEFAULT_URIS);
      logger.info("[==] mix response config ignore uris :{}", encode(ignoreWrapUris));
    }
  }

  @Data
  public static class ExceptionConfig implements Serializable {

    private boolean printStackTrace = false;

    /**
     * Print {@link MixmicroException} Stack Trace
     *
     * <p>default: true
     */
    private boolean printMixmicroStackTrace = true;

    private Class<?> handlerClass;

    /**
     * Default Biz Exception Code
     * <p><b>warning:: not http status code</b>
     */
    private int defaultExceptionResponseCode = -1;

    /**
     * Stack Trace Detail .
     *
     * <p>Checkout Order:
     * <li>1、Check Sensitive stacks map
     * <li>2、Check is instance of {@link MixmicroException}
     * <li>3、Check {{@link #printMixmicroStackTrace}} 's value
     *
     * @since 1.0.7
     */
    private Map<Class<? extends Exception>, Boolean> sensitiveStacks = Maps.newHashMap();
  }

  @Data
  public static class LogConfig implements Serializable {

    private boolean enabledRequestLog = false;
  }
}
