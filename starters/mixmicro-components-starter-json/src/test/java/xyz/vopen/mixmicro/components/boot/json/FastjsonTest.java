package xyz.vopen.mixmicro.components.boot.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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
            SerializerFeature.WriteNullStringAsEmpty
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
  @NoArgsConstructor
  public static class User {

    private String id;

    private String name;

    private int age;

    private List<String> list;

    private Map<String, String> map;

    @Builder
    public User(String id, String name, int age, List<String> list, Map<String, String> map) {
      this.id = id;
      this.name = name;
      this.age = age;
      this.list = list;
      this.map = map;
    }
  }
}
