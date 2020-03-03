package xyz.vopen.mixmicro.components.common;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * {@link SerializableBean}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-05-31.
 */
@Getter
@Setter
@NoArgsConstructor
public class SerializableBean implements Serializable {

  @Override
  public String toString() {
    return encode(this);
  }

  /**
   * Encode Object To JSON
   *
   * @param o object instance
   * @return json result
   */
  public static String encode(Object o) {
    return JSON.toJSONString(o);
  }

  /**
   * Encode Object To JSON Bytes
   *
   * @param o object instance
   * @return json result
   */
  public static byte[] bytes(Object o) {
    return JSON.toJSONBytes(o);
  }

  /**
   * Decode Json text to Object Instance
   *
   * @param json json content
   * @param clazz target class type
   * @param <T> T
   * @return bean instance
   */
  public static <T> T decode(String json, Class<T> clazz) {
    return JSON.parseObject(json, clazz);
  }

  /**
   * Decode Json bytes to Object Instance
   *
   * @param jsonBytes json content bytes
   * @param clazz target class type
   * @param <T> T
   * @return bean instance
   */
  public static <T> T decode(byte[] jsonBytes, Class<T> clazz) {
    return JSON.parseObject(jsonBytes, clazz);
  }
}
