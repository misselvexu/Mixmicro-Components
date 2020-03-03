package xyz.vopen.mixmicro.kits.retry;

import xyz.vopen.mixmicro.kits.retry.config.RetryConfig;
import xyz.vopen.mixmicro.kits.retry.config.RetryConfigBuilder;
import xyz.vopen.mixmicro.kits.retry.exception.RetriesExhaustedException;
import xyz.vopen.mixmicro.kits.retry.exception.UnexpectedException;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AsyncCallExecutorTest {

  private RetryConfig retryOnAnyExceptionConfig;

  private RetryConfig failOnAnyExceptionConfig;

  private ExecutorService executorService;

  @BeforeClass
  public void setup() {
    retryOnAnyExceptionConfig =
        new RetryConfigBuilder()
            .retryOnAnyException()
            .withFixedBackoff()
            .withMaxNumberOfTries(1)
            .withDelayBetweenTries(Duration.ofMillis(0))
            .build();

    failOnAnyExceptionConfig =
        new RetryConfigBuilder()
            .failOnAnyException()
            .withFixedBackoff()
            .withMaxNumberOfTries(1)
            .withDelayBetweenTries(Duration.ofMillis(0))
            .build();

    executorService = Executors.newFixedThreadPool(5);
  }

  @AfterClass
  public void teardown() {
    executorService.shutdown();
  }

  @Test
  public void verifyMultipleCalls_noExecutorService() throws Exception {
    Callable<Boolean> callable = () -> true;

    AsyncCallExecutor<Boolean> executor =
        new CallExecutorBuilder().config(retryOnAnyExceptionConfig).buildAsync();

    CompletableFuture<Status<Boolean>> future1 = executor.execute(callable);
    CompletableFuture<Status<Boolean>> future2 = executor.execute(callable);
    CompletableFuture<Status<Boolean>> future3 = executor.execute(callable);

    CompletableFuture combinedFuture = CompletableFuture.allOf(future1, future2, future3);
    combinedFuture.get();

    Assertions.assertThat(future1).isDone();
    Assertions.assertThat(future2).isDone();
    Assertions.assertThat(future3).isDone();
  }

  @Test
  public void verifyOneCall_success_noExecutorService() throws Exception {
    Callable<Boolean> callable = () -> true;

    AsyncCallExecutor<Boolean> executor =
        new CallExecutorBuilder().config(retryOnAnyExceptionConfig).buildAsync();

    CompletableFuture<Status<Boolean>> future = executor.execute(callable);

    Status<Boolean> status = future.get();
    Assertions.assertThat(future).isDone();
    assertThat(status.wasSuccessful()).isTrue();
  }

  @Test
  public void verifyOneCall_failDueToTooManyRetries_noExecutorService() throws Exception {
    Callable<Boolean> callable =
        () -> {
          throw new RuntimeException();
        };

    AsyncCallExecutor<Boolean> executor =
        new CallExecutorBuilder().config(retryOnAnyExceptionConfig).buildAsync();

    CompletableFuture<Status<Boolean>> future = executor.execute(callable);

    assertThatThrownBy(future::get)
        .isExactlyInstanceOf(ExecutionException.class)
        .hasCauseExactlyInstanceOf(RetriesExhaustedException.class);
  }

  @Test
  public void verifyOneCall_failDueToUnexpectedException_noExecutorService() throws Exception {
    Callable<Boolean> callable =
        () -> {
          throw new RuntimeException();
        };

    AsyncCallExecutor<Boolean> executor =
        new CallExecutorBuilder().config(failOnAnyExceptionConfig).buildAsync();

    CompletableFuture<Status<Boolean>> future = executor.execute(callable);

    assertThatThrownBy(future::get)
        .isExactlyInstanceOf(ExecutionException.class)
        .hasCauseExactlyInstanceOf(UnexpectedException.class);
  }

  @Test
  public void verifyMultipleCalls_withExecutorService() throws Exception {
    Callable<Boolean> callable = () -> true;

    AsyncCallExecutor<Boolean> executor =
        new CallExecutorBuilder().config(retryOnAnyExceptionConfig).buildAsync(executorService);

    CompletableFuture<Status<Boolean>> future1 = executor.execute(callable);
    CompletableFuture<Status<Boolean>> future2 = executor.execute(callable);
    CompletableFuture<Status<Boolean>> future3 = executor.execute(callable);

    CompletableFuture combinedFuture = CompletableFuture.allOf(future1, future2, future3);
    combinedFuture.get();

    Assertions.assertThat(future1).isDone();
    Assertions.assertThat(future2).isDone();
    Assertions.assertThat(future3).isDone();
  }

  @Test
  public void verifyOneCall_success_withExecutorService() throws Exception {
    Callable<Boolean> callable = () -> true;

    AsyncCallExecutor<Boolean> executor =
        new CallExecutorBuilder().config(retryOnAnyExceptionConfig).buildAsync(executorService);

    CompletableFuture<Status<Boolean>> future = executor.execute(callable);

    Status<Boolean> status = future.get();
    Assertions.assertThat(future).isDone();
    assertThat(status.wasSuccessful()).isTrue();
  }

  @Test
  public void verifyOneCall_failDueToTooManyRetries_withExecutorService() throws Exception {
    Callable<Boolean> callable =
        () -> {
          throw new RuntimeException();
        };

    AsyncCallExecutor<Boolean> executor =
        new CallExecutorBuilder().config(retryOnAnyExceptionConfig).buildAsync(executorService);

    CompletableFuture<Status<Boolean>> future = executor.execute(callable);

    assertThatThrownBy(future::get)
        .isExactlyInstanceOf(ExecutionException.class)
        .hasCauseExactlyInstanceOf(RetriesExhaustedException.class);
  }

  @Test
  public void verifyOneCall_failDueToUnexpectedException_withExecutorService() throws Exception {
    Callable<Boolean> callable =
        () -> {
          throw new RuntimeException();
        };

    AsyncCallExecutor<Boolean> executor =
        new CallExecutorBuilder().config(failOnAnyExceptionConfig).buildAsync(executorService);

    CompletableFuture<Status<Boolean>> future = executor.execute(callable);

    assertThatThrownBy(future::get)
        .isExactlyInstanceOf(ExecutionException.class)
        .hasCauseExactlyInstanceOf(UnexpectedException.class);
  }
}
