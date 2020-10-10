package xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.ExecutionComplete;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Daily implements Schedule {

  private final List<LocalTime> times;
  private final ZoneId zone;

  public Daily(LocalTime... times) {
    this(ZoneId.systemDefault(), Arrays.asList(times));
  }

  public Daily(List<LocalTime> times) {
    this(ZoneId.systemDefault(), times);
  }

  public Daily(ZoneId zone, LocalTime... times) {
    this(zone, Arrays.asList(times));
  }

  public Daily(ZoneId zone, List<LocalTime> times) {
    this.zone = Objects.requireNonNull(zone, "zone cannot be null");
    if (times.size() < 1) {
      throw new IllegalArgumentException("times cannot be empty");
    }
    this.times = times.stream().sorted().collect(Collectors.toList());
  }

  @Override
  public Instant getNextExecutionTime(ExecutionComplete executionComplete) {
    Instant timeDone = executionComplete.getTimeDone();
    LocalDate doneDate = timeDone.atZone(zone).toLocalDate();

    for (LocalTime time : times) {
      Instant nextTimeCandidate = ZonedDateTime.of(doneDate, time, zone).toInstant();
      if (nextTimeCandidate.isAfter(timeDone)) {
        return nextTimeCandidate;
      }
    }

    return ZonedDateTime.of(doneDate, times.get(0), zone).plusDays(1).toInstant();
  }

  @Override
  public boolean isDeterministic() {
    return true;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Daily)) return false;
    Daily that = (Daily) o;
    return Objects.equals(this.times, that.times) && Objects.equals(this.zone, that.zone);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(times, zone);
  }

  @Override
  public String toString() {
    return "Daily " + "times=" + times + ", zone=" + zone;
  }
}
