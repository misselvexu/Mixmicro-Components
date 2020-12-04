package xyz.vopen.mixmicro.components.boot.httpclient.annotation;

import xyz.vopen.mixmicro.components.boot.httpclient.retry.RetryRule;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface MixHttpClientRetry {

  /**
   * The maximum number of retries, the maximum can be set to 10
   *
   * @return The maximum number of retries
   */
  int maxRetries() default 2;

  /**
   * Retry interval
   *
   * @return Retry interval
   */
  int intervalMs() default 100;

  /**
   * Retry rule, default Response Status Code is not 2xx or Retry rule is triggered if an IO
   * exception occurs.
   *
   * @return Retry rule
   */
  RetryRule[] retryRules() default {
    RetryRule.RETRY_WHEN_NON200CODE, RetryRule.RETRY_WHEN_IO_EXCEPTION
  };
}
