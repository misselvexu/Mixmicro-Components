package xyz.vopen.mixmicro.components.enhance.mail.model.impl;

import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.model.InlinePicture;
import xyz.vopen.mixmicro.components.enhance.mail.utils.TimeUtils;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.Map;


@Getter
@EqualsAndHashCode(of = {"id", "scheduledDateTime", "assignedPriority", "desiredPriority"})
public class TemplateMixmicroEmailSchedulingPayload extends DefaultMixmicroEmailSchedulingPayload {

  private static final long serialVersionUID = -8267649519235191875L;

  private final String template;
  private final Map<String, Object> modelObject;
  private final InlinePicture[] inlinePictures;

  @Builder(builderMethodName = "templateEmailSchedulingDataBuilder")
  public TemplateMixmicroEmailSchedulingPayload(@NonNull final MixmicroEmail email,
                                                @NonNull final OffsetDateTime scheduledDateTime,
                                                final int desiredPriority,
                                                final int assignedPriority,
                                                @NonNull final String template,
                                                @NonNull final Map<String, Object> modelObject,
                                                @NonNull final InlinePicture[] inlinePictures) {
    super(email, scheduledDateTime, desiredPriority, assignedPriority);
    this.template = template;
    this.modelObject = modelObject;
    this.inlinePictures = inlinePictures;
  }

  public static class TemplateEmailSchedulingDataBuilder {
    protected OffsetDateTime scheduledDateTime = TimeUtils.offsetDateTimeNow();
  }

}