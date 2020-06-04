package xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexBasedParser implements Parser {
  private final Pattern pattern;
  private final List<String> examples;

  public RegexBasedParser(Pattern pattern, List<String> examples) {
    this.pattern = Objects.requireNonNull(pattern);
    this.examples = new ArrayList<>(examples);
  }

  @Override
  public Optional<Schedule> parse(String scheduleString) {
    if (scheduleString == null) {
      return Optional.empty();
    }
    Matcher m = pattern.matcher(scheduleString);
    if (m.matches()) {
      return Optional.of(matchedSchedule(m));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public List<String> examples() {
    return Collections.unmodifiableList(examples);
  }

  protected abstract Schedule matchedSchedule(MatchResult result);
}
