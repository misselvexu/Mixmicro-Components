package xyz.vopen.mixmicro.components.enhance.mail.configuration;

import xyz.vopen.mixmicro.components.enhance.mail.logging.LoggingStrategy;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static xyz.vopen.mixmicro.components.enhance.mail.configuration.ApplicationPropertiesConstants.__SPRING_MAIL_LOGGING;

/**
 * {@link LoggingProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/15/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = __SPRING_MAIL_LOGGING)
public class LoggingProperties {

  // spring.mail.logging.enabled
  private boolean enabled = true;

  // spring.mail.logging.strategy.*
  private Strategy strategy = new Strategy();

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Strategy {

    // spring.mail.logging.strategy.from
    private LoggingStrategy from = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.replyTo
    private LoggingStrategy replyTo = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.to
    private LoggingStrategy to = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.cc
    private LoggingStrategy cc = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.bcc
    private LoggingStrategy bcc = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.subject
    private LoggingStrategy subject = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.body
    private LoggingStrategy body = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.attachments
    private LoggingStrategy attachments = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.encoding
    private LoggingStrategy encoding = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.locale
    private LoggingStrategy locale = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.sentAt
    private LoggingStrategy sentAt = LoggingStrategy.STANDARD_DATE_FORMAT;

    // spring.mail.logging.strategy.receiptTo
    private LoggingStrategy receiptTo = LoggingStrategy.PLAIN_TEXT;

    // spring.mail.logging.strategy.depositionNotificationTo
    private LoggingStrategy depositionNotificationTo = LoggingStrategy.PLAIN_TEXT;

    @Getter(AccessLevel.NONE)
    private Ignore ignore = new Ignore();

    public boolean areCustomHeadersIgnored() {
      return ignore.customHeaders;
    }

    public boolean areNullAndEmptyCollectionsIgnored() {
      return ignore.nullAndEmptyCollections;
    }

    @Data
    @NoArgsConstructor
    public static class Ignore {
      // spring.mail.logging.strategy.ignore.customHeaders
      private boolean customHeaders = true;

      private boolean nullAndEmptyCollections = true;

    }
  }

}