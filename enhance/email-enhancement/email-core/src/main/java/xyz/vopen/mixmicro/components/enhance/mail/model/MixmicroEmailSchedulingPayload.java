package xyz.vopen.mixmicro.components.enhance.mail.model;


import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Comparator;

public interface MixmicroEmailSchedulingPayload extends Comparable<MixmicroEmailSchedulingPayload>, Serializable {

  Comparator<MixmicroEmailSchedulingPayload> DEFAULT_COMPARATOR = (o1, o2) -> {
    final OffsetDateTime o1ScheduledDateTime = o1.getScheduledDateTime();
    final OffsetDateTime o2ScheduledDateTime = o2.getScheduledDateTime();
    if (o1ScheduledDateTime.isBefore(o2ScheduledDateTime)) {
      return -1;
    } else if (o1ScheduledDateTime.isAfter(o2ScheduledDateTime)) {
      return 1;
    } else {
      int priorityComparison = Integer.compare(o1.getAssignedPriority(), o2.getAssignedPriority());
      if (priorityComparison != 0) {
        return priorityComparison;
      } else {
        return o1.getId().compareTo(o2.getId());
      }
    }
  };

  String getId();

  MixmicroEmail getEmail();

  default int getDesiredPriority() {
    return 1;
  }

  default int getAssignedPriority() {
    return 1;
  }

  OffsetDateTime getScheduledDateTime();

  @Override
  default int compareTo(MixmicroEmailSchedulingPayload o) {
    return DEFAULT_COMPARATOR.compare(this, o);
  }

}