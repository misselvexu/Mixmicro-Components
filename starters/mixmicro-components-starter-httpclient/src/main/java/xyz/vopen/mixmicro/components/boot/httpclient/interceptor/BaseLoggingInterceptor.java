package xyz.vopen.mixmicro.components.boot.httpclient.interceptor;

import org.slf4j.event.Level;

public abstract class BaseLoggingInterceptor implements NetworkInterceptor {

  /** Log printing level */
  protected final Level logLevel;

  /** Log printing strategy */
  protected final LogStrategy logStrategy;

  public BaseLoggingInterceptor(Level logLevel, LogStrategy logStrategy) {
    this.logLevel = logLevel;
    this.logStrategy = logStrategy;
  }
}
