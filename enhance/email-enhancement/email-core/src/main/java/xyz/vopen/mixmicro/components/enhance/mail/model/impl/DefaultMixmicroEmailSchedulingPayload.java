package xyz.vopen.mixmicro.components.enhance.mail.model.impl;

import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmail;
import xyz.vopen.mixmicro.components.enhance.mail.model.MixmicroEmailSchedulingPayload;
import xyz.vopen.mixmicro.components.enhance.mail.utils.TimeUtils;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
@EqualsAndHashCode(of = {"id", "scheduledDateTime", "assignedPriority", "desiredPriority"})
@ToString(of = {
    "id",
    "scheduledDateTime",
    "assignedPriority"
})
public class DefaultMixmicroEmailSchedulingPayload implements MixmicroEmailSchedulingPayload {

  private static final long serialVersionUID = 60021395842232155L;

  private final String id = UUID.randomUUID().toString();
  protected final MixmicroEmail email;
  protected final OffsetDateTime scheduledDateTime;
  protected final int assignedPriority;
  protected final int desiredPriority;

  @Builder(builderMethodName = "defaultEmailSchedulingDataBuilder")
  public DefaultMixmicroEmailSchedulingPayload(@NonNull final MixmicroEmail email,
                                               @NonNull final OffsetDateTime scheduledDateTime,
                                               final int desiredPriority,
                                               final int assignedPriority) {
    checkArgument(assignedPriority > 0, "Priority cannot be less than 1");

    this.email = email;
    this.scheduledDateTime = scheduledDateTime;
    this.desiredPriority = desiredPriority;
    this.assignedPriority = assignedPriority;
  }

  public static class DefaultEmailSchedulingDataBuilder {
    protected OffsetDateTime scheduledDateTime = TimeUtils.offsetDateTimeNow();
  }

}