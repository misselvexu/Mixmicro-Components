package xyz.vopen.mixmicro.kits.retry.backoff;

import xyz.vopen.mixmicro.kits.retry.config.RetryConfig;

import java.time.Duration;

public interface BackoffStrategy {

  Duration getDurationToWait(int numberOfTriesFailed, Duration delayBetweenAttempts);

  default void validateConfig(RetryConfig config) {}
}
