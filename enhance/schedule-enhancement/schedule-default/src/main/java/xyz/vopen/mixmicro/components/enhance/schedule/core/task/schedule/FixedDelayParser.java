package xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule;

import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

final class FixedDelayParser extends RegexBasedParser {
  private static final Pattern FIXED_DELAY_PATTERN = Pattern.compile("^FIXED_DELAY\\|(\\d+)s$");
  private static final List<String> EXAMPLES = Collections.singletonList("FIXED_DELAY|120s");

  FixedDelayParser() {
    super(FIXED_DELAY_PATTERN, EXAMPLES);
  }

  @Override
  protected Schedule matchedSchedule(MatchResult matchResult) {
    return FixedDelay.ofSeconds(Integer.parseInt(matchResult.group(1)));
  }
}
