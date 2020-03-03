package xyz.vopen.mixmicro.kits.retry;

import xyz.vopen.mixmicro.kits.retry.config.RetryConfig;
import xyz.vopen.mixmicro.kits.retry.config.RetryConfigBuilder;
import xyz.vopen.mixmicro.kits.retry.exception.RetriesExhaustedException;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.AssertJUnit.fail;

public class CallExecutorTest_RetryOnValueTest {

  private RetryConfigBuilder retryConfigBuilder;

  @BeforeMethod
  public void setup() {
    boolean configValidationEnabled = true;
    this.retryConfigBuilder = new RetryConfigBuilder(configValidationEnabled);
  }

  @Test
  public void verifyRetryOnStringValue_shouldRetry() {
    Callable<String> callable = () -> "should retry!";

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValue("should retry!")
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryOccurs(config, callable);
  }

  @Test
  public void verifyRetryOnStringValue_shouldNotRetry() {
    Callable<String> callable = () -> "should NOT retry!";

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValue("should retry!")
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryDoesNotOccur(config, callable);
  }

  @Test
  public void verifyRetryOnBooleanValue_shouldRetry() {
    Callable<Boolean> callable = () -> false;

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValue(false)
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryOccurs(config, callable);
  }

  @Test
  public void verifyRetryOnBooleanValue_shouldNotRetry() {
    Callable<Boolean> callable = () -> true;

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValue(false)
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryDoesNotOccur(config, callable);
  }

  @Test
  public void verifyRetryOnComplexValue_shouldRetry() {
    Callable<RetryOnValueTestObject> callable =
        () -> new RetryOnValueTestObject("should retry on this value!");

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValue(new RetryOnValueTestObject("should retry on this value!"))
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryOccurs(config, callable);
  }

  @Test
  public void verifyRetryOnComplexValue_shouldNotRetry() {
    Callable<RetryOnValueTestObject> callable =
        () -> new RetryOnValueTestObject("should NOT retry on this value!");

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValue(new RetryOnValueTestObject("should retry on this value!"))
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryDoesNotOccur(config, callable);
  }

  @Test
  public void verifyRetryOnReturnValues_shouldRetry() {
    Callable<RetryOnValueTestObject> callable = () -> new RetryOnValueTestObject("500 ERROR");

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValues(
                new RetryOnValueTestObject("500 ERROR"),
                new RetryOnValueTestObject("501 NOT IMPLEMENTED"))
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryOccurs(config, callable, 3);
  }

  @Test
  public void verifyRetryOnReturnValuesExcluding_shouldRetry() {
    Callable<RetryOnValueTestObject> callable = () -> new RetryOnValueTestObject("500 ERROR");

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValuesExcluding(new RetryOnValueTestObject("200 OK"))
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryOccurs(config, callable, 3);
  }

  @Test
  public void verifyRetryOnReturnValuesExcluding_shouldNotRetry() {
    Callable<RetryOnValueTestObject> callable = () -> new RetryOnValueTestObject("201 CREATED");

    RetryConfig config =
        retryConfigBuilder
            .retryOnAnyException()
            .retryOnReturnValuesExcluding(
                new RetryOnValueTestObject("200 OK"), new RetryOnValueTestObject("201 CREATED"))
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(3)
            .withFixedBackoff()
            .build();

    assertRetryDoesNotOccur(config, callable);
  }

  @Test
  public void verifyRetryOnValueAndExceptionInSameCall() {
    final Random random = new Random();
    Callable<Boolean> callable =
        () -> {
          if (random.nextBoolean()) {
            return false;
          } else {
            throw new FileNotFoundException();
          }
        };

    RetryConfig config =
        retryConfigBuilder
            .retryOnSpecificExceptions(FileNotFoundException.class)
            .retryOnReturnValue(false)
            .withDelayBetweenTries(Duration.ZERO)
            .withMaxNumberOfTries(100)
            .withFixedBackoff()
            .build();

    assertRetryOccurs(config, callable, 100);
  }

  private void assertRetryOccurs(
      RetryConfig config, Callable<?> callable, int expectedNumberOfTries) {
    try {
      new CallExecutorBuilder().config(config).build().execute(callable);
      fail("Expected RetriesExhaustedException but one wasn't thrown!");
    } catch (RetriesExhaustedException e) {
      Assertions.assertThat(e.getStatus().wasSuccessful()).isFalse();
      Assertions.assertThat(e.getStatus().getTotalTries()).isEqualTo(expectedNumberOfTries);
    }
  }

  private void assertRetryOccurs(RetryConfig config, Callable<?> callable) {
    assertRetryOccurs(config, callable, 3);
  }

  private void assertRetryDoesNotOccur(RetryConfig config, Callable<?> callable) {
    Status status = new CallExecutorBuilder().config(config).build().execute(callable);
    assertThat(status.wasSuccessful()).isTrue();
    assertThat(status.getTotalTries()).isEqualTo(1);
  }

  private class RetryOnValueTestObject {

    private String blah;

    RetryOnValueTestObject(String blah) {
      this.blah = blah;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      RetryOnValueTestObject that = (RetryOnValueTestObject) o;

      return blah != null ? blah.equals(that.blah) : that.blah == null;
    }

    @Override
    public int hashCode() {
      return blah != null ? blah.hashCode() : 0;
    }
  }
}
