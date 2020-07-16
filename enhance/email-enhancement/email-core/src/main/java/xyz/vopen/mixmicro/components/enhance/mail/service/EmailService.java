package xyz.vopen.mixmicro.components.enhance.mail.service;

import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.model.InlinePicture;
import xyz.vopen.mixmicro.components.enhance.mail.service.exception.CannotSendEmailException;

import javax.mail.internet.MimeMessage;
import java.util.Map;

public interface EmailService {

  /**
   * Send an email message.
   * <p>
   * The send date is set or overridden if any is present.
   *
   * @param mimeEmail an email to be send
   */
  MimeMessage send(MixmicroEmail mimeEmail);

  /**
   * Send an email message.
   * <p>
   * The body is ignored if present.
   * The send date is set or overridden if any is present.
   *
   * @param mimeEmail      an email to be send
   * @param template       the reference to the template file
   * @param modelObject    the model object to be used for the template engine, it may be null
   * @param inlinePictures list of pictures to be rendered inline in the template
   */
  MimeMessage send(MixmicroEmail mimeEmail,
                   String template, Map<String, Object> modelObject,
                   InlinePicture... inlinePictures) throws CannotSendEmailException;

}
