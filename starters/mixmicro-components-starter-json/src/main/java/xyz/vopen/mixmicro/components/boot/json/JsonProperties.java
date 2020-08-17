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

  @Deprecated @Beta private Jackson jackson = new Jackson();

  /**
   * 格式化打印，生产环境不建议开启。
   */
  private boolean prettyFormat = false;

  /**
   * 序列化输出NULL字段配置,默认关闭
   */
  private boolean writeMapNullValue = false;

  /**
   * 序列化NULL字符串为blank ""
   */
  private boolean writeNullStringAsEmpty = true;

  private boolean writeBigDecimalAsPlain = true;

  /**
   * 序列化Long类型转成String类型
   */
  private boolean writeLongAsString = true;

  private boolean writeNonStringValueAsString = false;

  private boolean writeEnumUsingToString;

  /**
   * 输出空数组
   */
  private boolean writeNullListAsEmpty = true;

  private String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

  @Getter
  @Setter
  public static class Fastjson implements Serializable {

    private boolean enabled = true;
  }

  @Getter
  @Setter
  @Deprecated
  public static class Jackson implements Serializable {

    private boolean enabled;
  }
}
