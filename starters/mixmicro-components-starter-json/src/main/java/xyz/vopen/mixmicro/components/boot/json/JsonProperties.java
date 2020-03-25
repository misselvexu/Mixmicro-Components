package xyz.vopen.mixmicro.components.boot.json;

import com.google.common.annotations.Beta;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

import static xyz.vopen.mixmicro.components.boot.json.JsonProperties.JSON_PROPERTIES_PREFIX;

/**
 * {@link JsonProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/25
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = JSON_PROPERTIES_PREFIX)
public class JsonProperties implements Serializable {

  static final String JSON_PROPERTIES_PREFIX = "mixmicro.http-message-converter";

  private Fastjson fastjson = new Fastjson();

  @Beta private Jackson jackson = new Jackson();

  private boolean writeNullStringAsEmpty = true;

  private boolean writeNonStringValueAsString = false;

  private boolean writeNullListAsEmpty = true;

  private String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

  @Getter
  @Setter
  public static class Fastjson implements Serializable {

    private boolean enabled = true;
  }

  @Getter
  @Setter
  public static class Jackson implements Serializable {

    private boolean enabled;
  }
}
