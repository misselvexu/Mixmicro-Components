package xyz.vopen.mixmicro.components.boot.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * {@link FastjsonTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/25
 */
public class FastjsonTest {

  @Test
  public void testNullString() {

    User user = User.builder().name(null).id("1").inner(new Inner()).build();

    System.out.println(
        JSON.toJSONString(
            user,
            PrettyFormat,
            WriteNonStringValueAsString,
//            WriteMapNullValue,
            WriteNullListAsEmpty,
            WriteBigDecimalAsPlain,
            WriteDateUseDateFormat
//            WriteNullNumberAsZero,
//            WriteNullStringAsEmpty

        ));

  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class User<T> {

    private String id;

    private String name;

    private Integer age;

    @JSONField(serialzeFeatures = {WriteNonStringValueAsString, WriteMapNullValue, WriteNullListAsEmpty, WriteNullNumberAsZero, WriteNullStringAsEmpty})
    private T body;

    private List<String> list;

    private Map<String, String> map;

    private Inner inner;
  }

  @Data
  public static class Inner {
    private String field;
    private Date date = new Date();
  }
}
