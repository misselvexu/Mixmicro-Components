package xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class CompositeParser implements Parser {
  private final List<Parser> delegates;

  private CompositeParser(List<Parser> delegates) {
    this.delegates = delegates;
  }

  @Override
  public Optional<Schedule> parse(String scheduleString) {
    return delegates.stream()
        .map(it -> it.parse(scheduleString))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
  }

  @Override
  public List<String> examples() {
    return delegates.stream().flatMap(it -> it.examples().stream()).collect(Collectors.toList());
  }

  static CompositeParser of(Parser... parsers) {
    if (parsers == null || parsers.length == 0)
      throw new IllegalArgumentException("Unable to create CompositeParser");
    return new CompositeParser(Arrays.asList(parsers));
  }
}
