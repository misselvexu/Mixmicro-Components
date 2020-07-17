package xyz.vopen.mixmicro.components.enhance.mail.configuration;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.enhance.mail.logging.EmailRenderer;
import xyz.vopen.mixmicro.components.enhance.mail.logging.defaultimpl.CustomizableEmailRenderer;
import xyz.vopen.mixmicro.components.enhance.mail.logging.defaultimpl.CustomizableEmailRenderer.CustomizableEmailRendererBuilder;
import xyz.vopen.mixmicro.components.enhance.mail.logging.defaultimpl.EmailFieldFormat;
import xyz.vopen.mixmicro.components.enhance.mail.service.impl.ConditionalExpression;

import javax.mail.internet.InternetAddress;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static java.util.Objects.nonNull;

/**
 * {@link EmailRendererConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnExpression(ConditionalExpression.EMAIL_LOGGING_RENDERER_IS_ENABLED)
public class EmailRendererConfiguration {

  private final LoggingProperties loggingProperties;

  public EmailRendererConfiguration(final LoggingProperties loggingProperties) {
    this.loggingProperties = loggingProperties;
  }

  @Bean
  public EmailRenderer emailRenderer() {
    final CustomizableEmailRendererBuilder customizableEmailRendererBuilder = createCustomizableEmailRendererBuilder();
    final LoggingProperties.Strategy loggingPropertiesStrategy = loggingProperties.getStrategy();

    final Function<InternetAddress, String> fromFormat = EmailFieldFormat.emailFormatterFrom(loggingPropertiesStrategy.getFrom());
    if (nonNull(fromFormat)) {
      customizableEmailRendererBuilder.withFromFormat(fromFormat);
    }

    final Function<InternetAddress, String> replyToFormat = EmailFieldFormat.emailFormatterFrom(loggingPropertiesStrategy.getReplyTo());
    if (nonNull(replyToFormat)) {
      customizableEmailRendererBuilder.withReplyToFormat(replyToFormat);
    }

    final Function<InternetAddress, String> toFormat = EmailFieldFormat.emailFormatterFrom(loggingPropertiesStrategy.getTo());
    if (nonNull(toFormat)) {
      customizableEmailRendererBuilder.withToFormat(toFormat);
    }

    final Function<InternetAddress, String> ccFormat = EmailFieldFormat.emailFormatterFrom(loggingPropertiesStrategy.getCc());
    if (nonNull(ccFormat)) {
      customizableEmailRendererBuilder.withCcFormat(ccFormat);
    }

    final Function<InternetAddress, String> bccFormat = EmailFieldFormat.emailFormatterFrom(loggingPropertiesStrategy.getBcc());
    if (nonNull(bccFormat)) {
      customizableEmailRendererBuilder.withBccFormat(bccFormat);
    }

    final UnaryOperator<String> subjectFormat = EmailFieldFormat.textFormatterFrom(loggingPropertiesStrategy.getSubject());
    if (nonNull(subjectFormat)) {
      customizableEmailRendererBuilder.withSubjectFormat(subjectFormat);
    }

    final UnaryOperator<String> bodyFormat = EmailFieldFormat.textFormatterFrom(loggingPropertiesStrategy.getBody());
    if (nonNull(bodyFormat)) {
      customizableEmailRendererBuilder.withBodyFormat(bodyFormat);
    }

    final UnaryOperator<String> attachmentsFormat = EmailFieldFormat.textFormatterFrom(loggingPropertiesStrategy.getAttachments());
    if (nonNull(attachmentsFormat)) {
      customizableEmailRendererBuilder.withAttachmentsFormat(attachmentsFormat);
    }

    final UnaryOperator<String> encodingFormat = EmailFieldFormat.textFormatterFrom(loggingPropertiesStrategy.getEncoding());
    if (nonNull(encodingFormat)) {
      customizableEmailRendererBuilder.withEncodingFormat(encodingFormat);
    }

    final Function<Locale, String> localeFormat = EmailFieldFormat.localeFormatterFrom(loggingPropertiesStrategy.getLocale());
    if (nonNull(localeFormat)) {
      customizableEmailRendererBuilder.withLocaleFormat(localeFormat);
    }

    final Function<Date, String> sentAtFormat = EmailFieldFormat.dateFormatterFrom(loggingPropertiesStrategy.getSentAt());
    if (nonNull(sentAtFormat)) {
      customizableEmailRendererBuilder.withSentAtFormat(sentAtFormat);
    }

    final Function<InternetAddress, String> receiptToFormat = EmailFieldFormat.emailFormatterFrom(loggingPropertiesStrategy.getReceiptTo());
    if (nonNull(receiptToFormat)) {
      customizableEmailRendererBuilder.withReceiptToFormat(receiptToFormat);
    }

    final Function<InternetAddress, String> depositionNotificationToFormat = EmailFieldFormat.emailFormatterFrom(loggingPropertiesStrategy.getDepositionNotificationTo());
    if (nonNull(depositionNotificationToFormat)) {
      customizableEmailRendererBuilder.withDepositionNotificationToFormat(depositionNotificationToFormat);
    }

    if (!loggingPropertiesStrategy.areCustomHeadersIgnored()) {
      customizableEmailRendererBuilder.includeCustomHeaders();
    }

    if (!loggingPropertiesStrategy.areNullAndEmptyCollectionsIgnored()) {
      customizableEmailRendererBuilder.includeNullAndEmptyCollections();
    }

    return customizableEmailRendererBuilder.build();
  }

  @VisibleForTesting
  protected CustomizableEmailRendererBuilder createCustomizableEmailRendererBuilder() {
    return CustomizableEmailRenderer.builder();
  }


}
