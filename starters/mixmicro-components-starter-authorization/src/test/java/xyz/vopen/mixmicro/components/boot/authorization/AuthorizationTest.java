package xyz.vopen.mixmicro.components.boot.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * {@link AuthorizationTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-11.
 */
@EnableAutoConfiguration
public class AuthorizationTest {

  public static void main(String[] args) {
    SpringApplication.run(AuthorizationTest.class,args);
  }
}
