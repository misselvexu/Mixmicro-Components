package xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule;

import java.util.List;
import java.util.Optional;

interface Parser {
  Optional<Schedule> parse(String scheduleString);

  List<String> examples();
}
