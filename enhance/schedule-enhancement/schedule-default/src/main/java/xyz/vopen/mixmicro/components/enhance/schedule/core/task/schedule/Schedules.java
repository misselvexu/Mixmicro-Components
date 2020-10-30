package xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

public class Schedules {
  private static final Parser SCHEDULE_PARSER =
      CompositeParser.of(new FixedDelayParser(), new DailyParser());

  public static Schedule daily(LocalTime... times) {
    return new Daily(times);
  }

  public static Schedule daily(ZoneId zone, LocalTime... times) {
    return new Daily(zone, times);
  }

  public static Schedule fixedDelay(Duration delay) {
    return FixedDelay.of(delay);
  }

  public static Schedule cron(String cronPattern) {
    return new CronSchedule(cronPattern);
  }

  public static Schedule cron(String cronPattern, ZoneId zoneId) {
    return new CronSchedule(cronPattern, zoneId);
  }

  /**
   * Currently supports Daily- and FixedDelay-schedule on the formats:
   *
   * <pre>DAILY|hh:mm,hh:mm,...,hh:mm(|TIME_ZONE)</pre>
   *
   * <br>
   *
   * <pre>FIXED_DELAY|xxxs  (xxx is number of seconds)</pre>
   *
   * @param scheduleString
   * @return A new schedule
   * @throws UnrecognizableSchedule When the scheduleString cannot be parsed
   */
  public static Schedule parseSchedule(String scheduleString) {
    return SCHEDULE_PARSER
        .parse(scheduleString)
        .orElseThrow(() -> new UnrecognizableSchedule(scheduleString, SCHEDULE_PARSER.examples()));
  }

  public static class UnrecognizableSchedule extends RuntimeException {
    public UnrecognizableSchedule(String inputSchedule) {
      super("Unrecognized schedule: '" + inputSchedule + "'");
    }

    public UnrecognizableSchedule(String inputSchedule, List<String> examples) {
      super("Unrecognized schedule: '" + inputSchedule + "'. Parsable examples: " + examples);
    }
  }
}
