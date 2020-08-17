package xyz.vopen.mixmicro.components.enhance.mail.service.impl;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import xyz.vopen.mixmicro.components.enhance.mail.logging.EmailLogRenderer;
import xyz.vopen.mixmicro.components.enhance.mail.model.InlinePicture;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmailAttachment;
import xyz.vopen.mixmicro.components.enhance.mail.service.EmailService;
import xyz.vopen.mixmicro.components.enhance.mail.service.TemplateService;
import xyz.vopen.mixmicro.components.enhance.mail.service.exception.CannotSendEmailException;
import xyz.vopen.mixmicro.components.enhance.mail.service.exception.TemplateException;
import xyz.vopen.mixmicro.components.enhance.mail.utils.EmailToMimeMessage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Optional.fromNullable;

@Service
@Slf4j
public class DefaultEmailService implements EmailService {

  private JavaMailSender javaMailSender;

  private TemplateService templateService;

  private EmailToMimeMessage emailToMimeMessage;

  private EmailLogRenderer emailLogRenderer;

  @Autowired(required = false)
  public DefaultEmailService(final @NonNull JavaMailSender javaMailSender,
                             final TemplateService templateService,
                             final @NonNull EmailToMimeMessage emailToMimeMessage,
                             final @NonNull EmailLogRenderer emailLogRenderer) {
    this.javaMailSender = javaMailSender;
    this.templateService = templateService;
    this.emailToMimeMessage = emailToMimeMessage;
    this.emailLogRenderer = emailLogRenderer.registerLogger(log);
  }

  @Autowired(required = false)
  public DefaultEmailService(final @NonNull JavaMailSender javaMailSender,
                             final @NonNull EmailToMimeMessage emailToMimeMessage,
                             final @NonNull EmailLogRenderer emailLogRenderer) {
    this(javaMailSender, null, emailToMimeMessage, emailLogRenderer);
  }

  @Override
  public MimeMessage send(final @NonNull MixmicroEmail email) throws CannotSendEmailException {

    switch (email.getContentType()){

      case HTML:
        email.setSentAt(new Date());
        final MimeMessage mimeMessage = toMimeMessage(email);
        try {
          final MimeMultipart content = new MimeMultipart("mixed");
          for (final MixmicroEmailAttachment emailAttachment : email.getAttachments()) {
            final MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(emailAttachment.getAttachmentData(), emailAttachment.getContentType().toString());
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(MimeUtility.encodeText(emailAttachment.getAttachmentName()));
            content.addBodyPart(attachmentPart);
          }

          //Set the HTML text part
          final MimeBodyPart textPart = new MimeBodyPart();
          textPart.setText(email.getBody(), email.getEncoding(), "html");
          content.addBodyPart(textPart);

          mimeMessage.setContent(content);
          mimeMessage.saveChanges();
          javaMailSender.send(mimeMessage);
          emailLogRenderer.info("Sent email {}.", emailWithCompiledBody(email, email.getBody()));
        } catch (Exception e) {
          log.error("The mime message cannot be created", e);
          throw new CannotSendEmailException("Error while sending the email due to problems with the mime content.", e);
        }
        return mimeMessage;

      case TEXT_PLAIN:
      default:
        email.setSentAt(new Date());
        final MimeMessage simpleMimeMessage = toMimeMessage(email);
        javaMailSender.send(simpleMimeMessage);
        emailLogRenderer.info("Sent email {}.", email);
        return simpleMimeMessage;
    }
  }

  @Override
  public MimeMessage send(final @NonNull MixmicroEmail email,
                          final @NonNull String template,
                          final Map<String, Object> modelObject,
                          final @NonNull InlinePicture... inlinePictures) throws CannotSendEmailException {
    email.setSentAt(new Date());
    final MimeMessage mimeMessage = toMimeMessage(email);
    try {
      final MimeMultipart content = new MimeMultipart("mixed");

      String text = templateService.mergeTemplateIntoString(template,
          fromNullable(modelObject).or(ImmutableMap.of()));

      for (final InlinePicture inlinePicture : inlinePictures) {
        final String cid = UUID.randomUUID().toString();

        //Set the cid in the template
        text = text.replace(inlinePicture.getTemplateName(), "cid:" + cid);

        //Set the image part
        final MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.attachFile(inlinePicture.getFile());
        imagePart.setContentID('<' + cid + '>');
        imagePart.setDisposition(MimeBodyPart.INLINE);
        imagePart.setHeader("Content-Type", inlinePicture.getImageType().getContentType());
        content.addBodyPart(imagePart);
      }

      for (final MixmicroEmailAttachment emailAttachment : email.getAttachments()) {
        //Set the image part
        final MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(emailAttachment.getAttachmentData(),
            emailAttachment.getContentType().toString());
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(MimeUtility.encodeText(emailAttachment.getAttachmentName()));
        content.addBodyPart(attachmentPart);
      }

      //Set the HTML text part
      final MimeBodyPart textPart = new MimeBodyPart();
      textPart.setText(text, email.getEncoding(), "html");
      content.addBodyPart(textPart);

      mimeMessage.setContent(content);
      mimeMessage.saveChanges();
      javaMailSender.send(mimeMessage);
      emailLogRenderer.info("Sent email {}.", emailWithCompiledBody(email, text));
    } catch (IOException e) {
      log.error("The template file cannot be read", e);
      throw new CannotSendEmailException("Error while sending the email due to problems with the template file.", e);
    } catch (TemplateException e) {
      log.error("The template file cannot be processed", e);
      throw new CannotSendEmailException("Error while processing the template file with the given model object.", e);
    } catch (MessagingException e) {
      log.error("The mime message cannot be created", e);
      throw new CannotSendEmailException("Error while sending the email due to problems with the mime content.", e);
    }
    return mimeMessage;
  }

  private MimeMessage toMimeMessage(@NotNull MixmicroEmail email) {
    return emailToMimeMessage.apply(email);
  }

  private MixmicroEmail emailWithCompiledBody(MixmicroEmail email, String body) {
    return new MixmicroEmailFromTemplate(email).body(body);
  }

  @RequiredArgsConstructor
  @Accessors(fluent = true)
  private static class MixmicroEmailFromTemplate implements MixmicroEmail {
    @Delegate
    private final MixmicroEmail email;

    @Setter
    private String body;


  }

}