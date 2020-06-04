package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {

  private static final Logger LOG = LoggerFactory.getLogger(ExecutorUtils.class);

  public static boolean shutdownAndAwaitTermination(
      ExecutorService executorService, Duration timeout) {
    executorService.shutdown();
    boolean successfulShutdown = awaitTermination(executorService, timeout);
    if (!successfulShutdown) {
      LOG.info("Failed to shutdown executor service gracefully. Trying interrupt...");
      executorService.shutdownNow();
      return awaitTermination(executorService, timeout);
    } else {
      return true;
    }
  }

  private static boolean awaitTermination(ExecutorService executor, Duration timeout) {
    try {
      return executor.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      LOG.warn("Interrupted while waiting for termination of executor.", e);
      return false;
    }
  }

  public static ThreadFactory defaultThreadFactoryWithPrefix(String prefix) {
    return new PrefixingDefaultThreadFactory(prefix);
  }

  private static class PrefixingDefaultThreadFactory implements ThreadFactory {

    private final String prefix;
    private final ThreadFactory defaultThreadFactory;

    public PrefixingDefaultThreadFactory(String prefix) {
      this.defaultThreadFactory = Executors.defaultThreadFactory();
      this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
      final Thread thread = defaultThreadFactory.newThread(r);
      thread.setName(prefix + thread.getName());
      return thread;
    }
  }
}
