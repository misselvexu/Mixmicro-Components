package xyz.vopen.mixmicro.kits.timer;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import lombok.Getter;

/**
 * {@link ScheduleIndex}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-05-08.
 */
@Getter
public class ScheduleIndex {

  private static final Interner<String> INTERNER = Interners.newStrongInterner();

  private final String subject;
  private final long scheduleTime;
  private final long offset;
  private final int size;
  private final long sequence;

  public ScheduleIndex(String subject, long scheduleTime, long offset, int size, long sequence) {
    this.subject = INTERNER.intern(subject);
    this.scheduleTime = scheduleTime;
    this.offset = offset;
    this.size = size;
    this.sequence = sequence;
  }
}
