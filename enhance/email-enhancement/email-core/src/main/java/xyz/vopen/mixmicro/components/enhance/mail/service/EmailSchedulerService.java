package xyz.vopen.mixmicro.components.enhance.mail.service;

import org.springframework.scheduling.annotation.Async;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.model.InlinePicture;
import xyz.vopen.mixmicro.components.enhance.mail.service.exception.CannotSendEmailException;

import java.time.OffsetDateTime;
import java.util.Map;

public interface EmailSchedulerService {

  /**
   * Schedules the sending of an email message at time <strong>now</strong> (UTC).
   *
   * @param mimeEmail            an email to be sent
   * @param desiredPriorityLevel the desiredPriority level for the email:
   *                             the emails with scheduledTime<=now are sent according to an order depending
   *                             on their desiredPriority level
   */
  @Async
  void schedule(MixmicroEmail mimeEmail, int desiredPriorityLevel);

  /**
   * Schedules the sending of an email message.
   *
   * @param mimeEmail            an email to be sent
   * @param scheduledDateTime    the date-time at which the email should be sent
   * @param desiredPriorityLevel the desiredPriority level for the email:
   *                             the emails with scheduledTime<=now are sent according to an order depending
   *                             on their desiredPriority level
   */
  @Async
  void schedule(MixmicroEmail mimeEmail, OffsetDateTime scheduledDateTime, int desiredPriorityLevel);

  /**
   * Schedules the sending of an email message at time <strong>now</strong> (UTC).
   *
   * @param mimeEmail            an email to be sent
   * @param desiredPriorityLevel the desiredPriority level for the email:
   *                             the emails with scheduledTime<=now are sent according to an order depending
   *                             on their desiredPriority level
   * @param template             the reference to the template file
   * @param modelObject          the model object to be used for the template engine, it may be null
   * @param inlinePictures       list of pictures to be rendered inline in the template
   */
  @Async
  void schedule(MixmicroEmail mimeEmail, int desiredPriorityLevel,
                String template, Map<String, Object> modelObject,
                InlinePicture... inlinePictures) throws CannotSendEmailException;

  /**
   * Schedules the sending of an email message.
   *
   * @param mimeEmail            an email to be sent
   * @param scheduledDateTime    the date-time at which the email should be sent
   * @param desiredPriorityLevel the desiredPriority level for the email:
   *                             the emails with scheduledTime<=now are sent according to an order depending
   *                             on their desiredPriority level
   * @param template             the reference to the template file
   * @param modelObject          the model object to be used for the template engine, it may be null
   * @param inlinePictures       list of pictures to be rendered inline in the template
   */
  @Async
  void schedule(MixmicroEmail mimeEmail, OffsetDateTime scheduledDateTime, int desiredPriorityLevel,
                String template, Map<String, Object> modelObject,
                InlinePicture... inlinePictures) throws CannotSendEmailException;


  default ServiceStatus status() {
    return ServiceStatus.CLOSED;
  }

}
