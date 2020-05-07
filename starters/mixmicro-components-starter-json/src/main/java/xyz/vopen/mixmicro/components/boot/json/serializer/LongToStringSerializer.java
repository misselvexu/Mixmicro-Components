package xyz.vopen.mixmicro.components.boot.json.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * {@link LongToStringSerializer}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-22.
 */
public class LongToStringSerializer implements ObjectSerializer {

  public static final LongToStringSerializer instance = new LongToStringSerializer();

  /**
   * fastjson invokes this call-back method during serialization when it encounters a field of the
   * specified type.
   *
   * @param serializer json serializer instance
   * @param object src the object that needs to be converted to Json.
   * @param fieldName parent object field name
   * @param fieldType parent object field type
   * @param features parent object field serializer features
   * @throws IOException maybe thrown {@link IOException}
   */
  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
    SerializeWriter out = serializer.out;

    if (object == null) {
      out.writeNull();
      return;
    }
    String strVal = object.toString();
    out.writeString(strVal);
  }
}
