package xyz.vopen.mixmicro.components.enhance.email.test;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.model.impl.DefaultMixmicroMixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.service.EmailService;
import xyz.vopen.mixmicro.components.enhance.mail.service.exception.CannotSendEmailException;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * {@link SimpleMixmicroEmailSender}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/15/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmailServiceApplication.class)
public class SimpleMixmicroEmailSender {


  @Autowired
  private EmailService emailService;


  @Test
  public void testSimpleEmail() throws UnsupportedEncodingException {
    final MixmicroEmail email = DefaultMixmicroMixmicroEmail.builder()
        .from(new InternetAddress("elve.xu@yunlsp.com", "Elve.Xu "))
        .to(Lists.newArrayList(new InternetAddress("x_vivi@yeah.net", "Elve.Xu-Yeah")))
        .subject("Spring Boot Email Starter Unit Test Subject")
        .body("This a test email , do no reply .")
        .encoding("UTF-8").build();

    emailService.send(email);
  }


  @Test
  public void testTemplateEmail() throws UnsupportedEncodingException, CannotSendEmailException {
    final MixmicroEmail email = DefaultMixmicroMixmicroEmail.builder()
        .from(new InternetAddress("elve.xu@yunlsp.com", "Elve.Xu "))
        .to(Lists.newArrayList(new InternetAddress("x_vivi@yeah.net", "Elve.Xu-Yeah")))
        .subject("Spring Boot Template Html Email Starter Unit Test Subject")
        .body("")
        .encoding("UTF-8").build();

    final Map<String, Object> modelObject = new HashMap<>();
    modelObject.put("value", "##value##");

    emailService.send(email, "templates/simple-biz-html.ftl", modelObject);
  }

}
