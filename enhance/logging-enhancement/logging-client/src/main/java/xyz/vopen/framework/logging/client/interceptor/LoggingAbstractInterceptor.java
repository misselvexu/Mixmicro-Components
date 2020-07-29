package xyz.vopen.framework.logging.client.interceptor;

import xyz.vopen.framework.logging.client.LoggingFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.framework.logging.client.span.LoggingSpanGenerator;
import xyz.vopen.framework.logging.client.span.support.LoggingDefaultSpanGenerator;
import xyz.vopen.framework.logging.client.tracer.LoggingTraceGenerator;
import xyz.vopen.framework.logging.client.tracer.support.LoggingDefaultTraceGenerator;

/**
 * The {@link LoggingInterceptor} basic support
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingAbstractInterceptor implements LoggingInterceptor {
  /** logger instance */
  static Logger logger = LoggerFactory.getLogger(LoggingAbstractInterceptor.class);
  /** logging factory bean {@link LoggingFactoryBean} */
  private LoggingFactoryBean factoryBean;

  public LoggingAbstractInterceptor(LoggingFactoryBean factoryBean) {
    this.factoryBean = factoryBean;
  }

  public LoggingFactoryBean getFactoryBean() {
    return factoryBean;
  }

  /**
   * create new traceId {@link LoggingTraceGenerator}
   *
   * @return traceId
   * @see LoggingDefaultTraceGenerator
   */
  @Override
  public String createTraceId() {
    return factoryBean.getTraceGenerator().createTraceId();
  }

  /**
   * create new spanId {@link LoggingSpanGenerator}
   *
   * @return spanId
   * @see LoggingDefaultSpanGenerator
   */
  @Override
  public String createSpanId() {
    return factoryBean.getSpanGenerator().createSpanId();
  }
}
