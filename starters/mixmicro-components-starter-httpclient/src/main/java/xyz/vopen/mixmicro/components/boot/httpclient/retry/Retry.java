package xyz.vopen.mixmicro.components.boot.httpclient.retry;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Retry {

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
   * 重试规则，默认 响应状态码不是2xx 或者 发生IO异常 时触发重试 Retry rule
   *
   * @return Retry rule
   */
  RetryRule[] retryRules() default {
    RetryRule.RESPONSE_STATUS_NOT_2XX, RetryRule.OCCUR_IO_EXCEPTION
  };
}
