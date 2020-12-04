package xyz.vopen.mixmicro.components.boot.httpclient.retry;

/**
 * Rules that trigger retry
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public enum RetryRule {

  /** The response status code is not 2xx */
  RETRY_WHEN_NON200CODE,

  /** Any exception occurred */
  RETRY_WHEN_ANY_EXCEPTION,

  /** IO exception occurred */
  RETRY_WHEN_IO_EXCEPTION,
}
