package xyz.vopen.mixmicro.components.enhance.mail.model.impl;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmailAttachment;
import lombok.*;

import javax.mail.internet.InternetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Mime email.
 */
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DefaultMixmicroMixmicroEmail implements MixmicroEmail {

  private static final long serialVersionUID = 634175529482595823L;

  @NonNull
  private InternetAddress from;

  private InternetAddress replyTo;

  private Collection<InternetAddress> to;

  private Collection<InternetAddress> cc;

  private Collection<InternetAddress> bcc;

  @NonNull
  private String subject;

  @NonNull
  private String body;

  @NonNull
  @Singular
  private Collection<MixmicroEmailAttachment> attachments;

  private String encoding = StandardCharsets.UTF_8.name();

  private Locale locale;

  private Date sentAt;

  private InternetAddress receiptTo;

  private InternetAddress depositionNotificationTo;

  private Map<String, String> customHeaders;

  //This is to have default values in Lombok constructor
  public static class DefaultEmailBuilder {

    private Collection<InternetAddress> cc = ImmutableList.of();

    private Collection<InternetAddress> bcc = ImmutableList.of();

    private String encoding = StandardCharsets.UTF_8.name();

    private Map<String, String> customHeaders = ImmutableMap.of();
  }

}