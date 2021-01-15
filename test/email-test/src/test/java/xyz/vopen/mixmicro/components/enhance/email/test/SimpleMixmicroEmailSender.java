package xyz.vopen.mixmicro.components.enhance.email.test;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.vopen.mixmicro.components.enhance.mail.model.ContentType;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.model.impl.DefaultMixmicroEmailAttachment;
import xyz.vopen.mixmicro.components.enhance.mail.model.impl.DefaultMixmicroMixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.service.EmailSchedulerService;
import xyz.vopen.mixmicro.components.enhance.mail.service.EmailService;
import xyz.vopen.mixmicro.components.enhance.mail.service.exception.CannotSendEmailException;

import javax.mail.internet.InternetAddress;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
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

  @Autowired
  private EmailSchedulerService emailSchedulerService;


  @Test
  public void testHtmlEmail() throws Exception {
    final MixmicroEmail email = DefaultMixmicroMixmicroEmail.builder()
        .from(new InternetAddress("mailservice@yunlsp.com", "苏州海管家物流科技有限公司"))
        .to(Lists.newArrayList(new InternetAddress("x_vivi@yeah.net", "Elve.Xu-Yeah")))
        .subject("Spring Boot Email Starter Unit Test Subject")
        .contentType(ContentType.HTML)
        .body("<html>This a <em>test email</em> , do no reply .</html>")
        .encoding("UTF-8").build();

    emailService.send(email);
  }

  @Test
  public void testSimpleEmail() throws Exception {
    final MixmicroEmail email = DefaultMixmicroMixmicroEmail.builder()
        .from(new InternetAddress("mailservice@yunlsp.com", "苏州海管家物流科技有限公司"))
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

    emailService.send(email, "simple-biz-html.ftl", modelObject);
  }


  @Test
  public void testAttachmentEmail() throws Exception {

    DefaultResourceLoader loader = new DefaultResourceLoader();
    loader.setClassLoader(EmailServiceApplication.class.getClassLoader());

    Resource resource = loader.getResource("/APPLE INC.txt");

    InputStream stream = resource.getInputStream();

    final MixmicroEmail email = DefaultMixmicroMixmicroEmail.builder()
        .from(new InternetAddress("elve.xu@yunlsp.com", "Elve.Xu "))
        .to(Lists.newArrayList(new InternetAddress("x_vivi@yeah.net", "Elve.Xu-Yeah")))
        .subject("Spring Boot Attachment Html Email Starter Unit Test Subject")
        .body("This is an attachment email .")
        .attachment(new DefaultMixmicroEmailAttachment("APPLE INC.txt", IOUtils.toByteArray(stream)))
        .encoding("UTF-8").build();

    emailService.send(email);
  }

  @Test
  public void testScheduleSimpleEmail() throws UnsupportedEncodingException, InterruptedException {

    OffsetDateTime now = OffsetDateTime.now();
    OffsetDateTime t20s = now.plusSeconds(20);

    final MixmicroEmail email = DefaultMixmicroMixmicroEmail.builder()
        .from(new InternetAddress("elve.xu@yunlsp.com", "Elve.Xu "))
        .to(Lists.newArrayList(new InternetAddress("x_vivi@yeah.net", "Elve.Xu-Yeah")))
        .subject("Spring Boot Schedule Email Starter Unit Test Subject")
        .body("This a test schedule email , do no reply .")
        .encoding("UTF-8").build();

    System.out.println("submit schedule email , 20 s");

    emailSchedulerService.schedule(email, t20s, 1);

    System.out.println("waiting 30 s ....");

    Thread.sleep(30 * 1000);
  }

}
