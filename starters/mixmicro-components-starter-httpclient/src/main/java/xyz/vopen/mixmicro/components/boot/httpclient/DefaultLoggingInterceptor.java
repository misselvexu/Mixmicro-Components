package xyz.vopen.mixmicro.components.boot.httpclient;

import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.AbstractLoggingInterceptor;

import java.io.IOException;

public class DefaultLoggingInterceptor extends AbstractLoggingInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(DefaultLoggingInterceptor.class);

  private final HttpLoggingInterceptor httpLoggingInterceptor;

  public DefaultLoggingInterceptor(Level logLevel, MixHttpClientLogStrategy logStrategy) {
    super(logLevel, logStrategy);
    HttpLoggingInterceptor.Logger logger = httpLoggingInterceptorLogger(logLevel);
    httpLoggingInterceptor = new HttpLoggingInterceptor(logger);
    String name = logStrategy.name();
    HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.valueOf(name);
    httpLoggingInterceptor.setLevel(level);
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    return httpLoggingInterceptor.intercept(chain);
  }

  public HttpLoggingInterceptor.Logger httpLoggingInterceptorLogger(Level level) {

    switch (level) {
      case WARN:
        return logger::warn;
      case DEBUG:
        return logger::debug;
      case ERROR:
        return logger::error;
      case TRACE:
        return logger::trace;
      case INFO:
      default:
        return logger::info;
    }
  }
}
