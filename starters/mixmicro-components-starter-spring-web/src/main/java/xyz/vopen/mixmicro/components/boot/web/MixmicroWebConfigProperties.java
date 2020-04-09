package xyz.vopen.mixmicro.components.boot.web;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

import static xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties.MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX;

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
public class MixmicroWebConfigProperties implements Serializable {

  public static final String MIXMICRO_WEB_CONFIG_PROPERTIES_PREFIX = "mixmicro.spring.web";

  private boolean enabled = true;

  @NestedConfigurationProperty private ExceptionConfig exception = new ExceptionConfig();

  @NestedConfigurationProperty private ResponseConfig response = new ResponseConfig();

  @Data
  public static class ResponseConfig implements Serializable {

    private int defaultSuccessResponseCode = 0;

  }

  @Data
  public static class ExceptionConfig implements Serializable {

    private boolean printStackTrace = false;

    private Class<?> handlerClass;

    private int defaultExceptionResponseCode = -1;

  }
}
