package xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class DailyParser extends RegexBasedParser {
  private static final Pattern DAILY_PATTERN_WITH_TIMEZONE =
      Pattern.compile("^DAILY\\|((\\d{2}:\\d{2})(,\\d{2}:\\d{2})*)(\\|(.+))?$");
  private static final List<String> EXAMPLES =
      Arrays.asList("DAILY|12:00", "DAILY|12:00,13:45", "DAILY|12:00,13:45|Europe/Rome");

  DailyParser() {
    super(DAILY_PATTERN_WITH_TIMEZONE, EXAMPLES);
  }

  @Override
  protected Schedule matchedSchedule(MatchResult matchResult) {
    return maybeTimeZone(matchResult)
        .map(timeZone -> new Daily(ZoneId.of(timeZone), scheduleTimes(matchResult)))
        .orElseGet(() -> new Daily(scheduleTimes(matchResult)));
  }

  private Optional<String> maybeTimeZone(MatchResult matchResult) {
    return Optional.ofNullable(matchResult.group(5));
  }

  private List<LocalTime> scheduleTimes(MatchResult matchResult) {
    String[] times = matchResult.group(1).split(",");
    return Stream.of(times).map(LocalTime::parse).collect(Collectors.toList());
  }
}
