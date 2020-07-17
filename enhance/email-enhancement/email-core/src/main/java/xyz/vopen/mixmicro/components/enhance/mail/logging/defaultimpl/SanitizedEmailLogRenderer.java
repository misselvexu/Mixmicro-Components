package xyz.vopen.mixmicro.components.enhance.mail.logging.defaultimpl;

import xyz.vopen.mixmicro.components.enhance.mail.logging.EmailLogRenderer;
import xyz.vopen.mixmicro.components.enhance.mail.logging.EmailRenderer;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class SanitizedEmailLogRenderer implements EmailLogRenderer {

  private final EmailRenderer emailRenderer;
  protected Logger logger;

  @Autowired
  public SanitizedEmailLogRenderer(final EmailRenderer emailRenderer) {
    logger = log;
    this.emailRenderer = emailRenderer;
  }

  /**
   * Sanitize text to prevent injection of EOL characters into log messages.
   * <p>
   * Actually, replaces all whitespaces including CR and LF with a space and all other ASCII control
   * characters with "?" to avoid malicious code in the logs.
   */
  private static String sanitizeString(String text) {
    return Encode.forJava(text);
  }

  @Override
  public EmailLogRenderer registerLogger(@NonNull final Logger logger) {
    this.logger = logger;
    return this;
  }

  @Override
  public void trace(@NonNull final String message, @NonNull final MixmicroEmail email, Object... messageParams) {
    if (logger.isTraceEnabled()) {
      logger.trace(message, renderEmail(email), messageParams);
    }
  }

  @Override
  public void trace(@NonNull final MixmicroEmail email) {
    if (logger.isTraceEnabled()) {
      logger.trace(renderEmail(email));
    }
  }

  @Override
  public void debug(@NonNull final String message, @NonNull final MixmicroEmail email, Object... messageParams) {
    if (logger.isDebugEnabled()) {
      logger.debug(message, renderEmail(email), messageParams);

    }
  }

  @Override
  public void debug(@NonNull final MixmicroEmail email) {
    if (logger.isDebugEnabled()) {
      logger.debug(renderEmail(email));
    }
  }

  @Override
  public void info(@NonNull final String message, @NonNull final MixmicroEmail email, Object... messageParams) {
    if (logger.isInfoEnabled()) {
      logger.info(message, renderEmail(email), messageParams);
    }
  }

  @Override
  public void info(@NonNull final MixmicroEmail email) {
    if (logger.isInfoEnabled()) {
      logger.info(renderEmail(email));
    }
  }

  @Override
  public void warn(@NonNull final String message, @NonNull final MixmicroEmail email, Object... messageParams) {
    if (logger.isWarnEnabled()) {
      logger.warn(message, renderEmail(email), messageParams);
    }
  }

  @Override
  public void warn(@NonNull final MixmicroEmail email) {
    if (logger.isWarnEnabled()) {
      logger.warn(renderEmail(email));
    }
  }

  @Override
  public void error(@NonNull final String message, @NonNull final MixmicroEmail email, Object... messageParams) {
    if (logger.isErrorEnabled()) {
      logger.error(message, renderEmail(email), messageParams);
    }
  }

  @Override
  public void error(@NonNull final MixmicroEmail email) {
    if (logger.isErrorEnabled()) {
      logger.error(renderEmail(email));
    }
  }

  private String renderEmail(final MixmicroEmail email) {
    return sanitizeString(emailRenderer.render(email));
  }

}
