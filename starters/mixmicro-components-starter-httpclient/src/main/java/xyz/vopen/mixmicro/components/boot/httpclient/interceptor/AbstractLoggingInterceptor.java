package xyz.vopen.mixmicro.components.boot.httpclient.interceptor;

import org.slf4j.event.Level;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientLogStrategy;

public abstract class AbstractLoggingInterceptor implements NetworkInterceptor {

  /** Log printing level */
  protected final Level logLevel;

  /** Log printing strategy */
  protected final MixHttpClientLogStrategy logStrategy;

  public AbstractLoggingInterceptor(Level logLevel, MixHttpClientLogStrategy logStrategy) {
    this.logLevel = logLevel;
    this.logStrategy = logStrategy;
  }
}
