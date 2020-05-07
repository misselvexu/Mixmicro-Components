package xyz.vopen.mixmicro.components.boot.web.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vopen.mixmicro.components.common.ResponseEntity;
import xyz.vopen.mixmicro.components.common.rest.MixmicroResponseBody;

/**
 * {@link SampleApplication}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
@SpringBootApplication
public class SampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }

  @RestController
  @RequestMapping("/sample")
  static class SampleEndpoint {

    @GetMapping("/test01")
    ResponseEntity test() {
      return ResponseEntity.ok(null);
    }

    @MixmicroResponseBody(direct = true, json = false)
    @GetMapping("/test02")
    String test02() {
      return "你好";
    }

    @GetMapping("/test03")
    void test03() {
      System.out.println("test03");
    }
  }
}
