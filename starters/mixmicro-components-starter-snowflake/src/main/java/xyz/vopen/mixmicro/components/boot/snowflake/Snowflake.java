package xyz.vopen.mixmicro.components.boot.snowflake;

import com.google.common.base.Preconditions;
import java.util.Calendar;
import lombok.SneakyThrows;

/**
 * Snowflake Bean
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 23/10/2018.
 */
@SuppressWarnings("ALL")
public final class Snowflake {

  public static final long EPOCH;
  private static final long SEQUENCE_BITS = 8L;
  private static final long WORKER_ID_BITS = 4L;
  private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;
  private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
  private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
  private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;
  private static TimeService timeService = new TimeService();

  static {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2016, Calendar.NOVEMBER, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    EPOCH = calendar.getTimeInMillis();
  }

  private long workerId;
  private int maxTolerateTimeDifferenceMilliseconds = 10;
  private byte sequenceOffset;
  private long sequence;
  private long lastMilliseconds;

  /**
   * New Snowflag Builder
   *
   * @param workerId workerId
   */
  public Snowflake(long workerId) {
    setWorkerId(workerId);
  }

  /**
   * Set work process id.
   *
   * @param workerId work process id
   */
  public void setWorkerId(final long workerId) {
    Preconditions.checkArgument(workerId >= 0L && workerId < WORKER_ID_MAX_VALUE);
    this.workerId = workerId;
  }

  /**
   * Set max tolerate time difference milliseconds.
   *
   * @param maxTolerateTimeDifferenceMilliseconds max tolerate time difference milliseconds
   */
  public void setMaxTolerateTimeDifferenceMilliseconds(
      final int maxTolerateTimeDifferenceMilliseconds) {
    this.maxTolerateTimeDifferenceMilliseconds = maxTolerateTimeDifferenceMilliseconds;
  }

  /**
   * Generate key.
   *
   * @return key type is @{@link Long}.
   */
  public synchronized long nextId() {
    long currentMilliseconds = timeService.getCurrentMillis();
    if (waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
      currentMilliseconds = timeService.getCurrentMillis();
    }
    if (lastMilliseconds == currentMilliseconds) {
      if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
        currentMilliseconds = waitUntilNextTime(currentMilliseconds);
      }
    } else {
      vibrateSequenceOffset();
      sequence = sequenceOffset;
    }
    lastMilliseconds = currentMilliseconds;
    return ((currentMilliseconds - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS)
        | (workerId << WORKER_ID_LEFT_SHIFT_BITS)
        | sequence;
  }

  @SneakyThrows
  private boolean waitTolerateTimeDifferenceIfNeed(final long currentMilliseconds) {
    if (lastMilliseconds <= currentMilliseconds) {
      return false;
    }
    long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
    Preconditions.checkState(
        timeDifferenceMilliseconds < maxTolerateTimeDifferenceMilliseconds,
        "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds",
        lastMilliseconds,
        currentMilliseconds);
    Thread.sleep(timeDifferenceMilliseconds);
    return true;
  }

  private long waitUntilNextTime(final long lastTime) {
    long result = timeService.getCurrentMillis();
    while (result <= lastTime) {
      result = timeService.getCurrentMillis();
    }
    return result;
  }

  private void vibrateSequenceOffset() {
    sequenceOffset = (byte) (~sequenceOffset & 1);
  }

  private static class TimeService {

    /**
     * Get current millis.
     *
     * @return current millis
     */
    public long getCurrentMillis() {
      return System.currentTimeMillis();
    }
  }
}
