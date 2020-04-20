package xyz.vopen.mixmicro.components.boot.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.*;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;

/**
 * {@link FastjsonTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/25
 */
public class FastjsonTest {

  @Test
  public void testNullString() {

    User user = User.builder().name(null).id("1").age(0).build();

    System.out.println(
        JSON.toJSONString(
            user,
            SerializerFeature.WriteNullStringAsEmpty, WriteNullListAsEmpty
        ));

    System.out.println(
        JSON.toJSONString(
            user,
            SerializerFeature.WriteNonStringValueAsString
        ));

    System.out.println(
        JSON.toJSONString(
            user,
            SerializerFeature.WriteNullListAsEmpty
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

    private int age;

    private T body;

    private List<String> list;

    private Map<String, String> map;

  }
}
