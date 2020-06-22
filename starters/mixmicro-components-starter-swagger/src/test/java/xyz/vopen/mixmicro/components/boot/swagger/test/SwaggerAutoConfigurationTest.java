package xyz.vopen.mixmicro.components.boot.swagger.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.vopen.mixmicro.components.boot.swagger.annotation.EnableMixmicroSwagger;

/**
 * {@link SwaggerAutoConfigurationTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
@SpringBootApplication
@EnableMixmicroSwagger
public class SwaggerAutoConfigurationTest {

  public static void main(String[] args) {
    SpringApplication.run(SwaggerAutoConfigurationTest.class, args);
  }


}
