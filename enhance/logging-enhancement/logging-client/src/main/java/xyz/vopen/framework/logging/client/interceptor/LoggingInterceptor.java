package xyz.vopen.framework.logging.client.interceptor;

import xyz.vopen.framework.logging.client.span.LoggingSpanGenerator;
import xyz.vopen.framework.logging.client.tracer.LoggingTraceGenerator;

/**
 * MinBox logging interceptor
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface LoggingInterceptor {
  /**
   * create new traceId {@link LoggingTraceGenerator}
   *
   * @return traceId
   */
  String createTraceId();

  /**
   * create new spanId {@link LoggingSpanGenerator}
   *
   * @return spanId
   */
  String createSpanId();
}
