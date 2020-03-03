package xyz.vopen.mixmicro.kits.retry.backoff;

import java.time.Duration;

public class NoWaitBackoffStrategy implements BackoffStrategy {

  @Override
  public Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
    return Duration.ZERO;
  }
}
