package xyz.vopen.mixmicro.kits.retry.backoff;

import xyz.vopen.mixmicro.kits.retry.config.RetryConfig;
import xyz.vopen.mixmicro.kits.retry.exception.InvalidRetryConfigException;

import java.time.Duration;

public class FixedBackoffStrategy implements BackoffStrategy {

  @Override
  public Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
    return delayBetweenAttempts;
  }

  @Override
  public void validateConfig(RetryConfig config) {
    if (null == config.getDelayBetweenRetries()) {
      throw new InvalidRetryConfigException("Retry config must specify the delay between retries!");
    }
  }
}
