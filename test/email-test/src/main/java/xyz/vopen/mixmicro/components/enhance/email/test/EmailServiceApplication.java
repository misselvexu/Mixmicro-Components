package xyz.vopen.mixmicro.components.enhance.email.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.vopen.mixmicro.components.enhance.mail.configuration.EnableMixmicroEmail;

/**
 * {@link EmailServiceApplication}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/15/20
 */
@SpringBootApplication
@EnableMixmicroEmail
public class EmailServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EmailServiceApplication.class, args);
  }

}
