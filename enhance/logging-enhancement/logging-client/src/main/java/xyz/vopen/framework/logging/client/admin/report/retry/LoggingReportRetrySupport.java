package xyz.vopen.framework.logging.client.admin.report.retry;

import xyz.vopen.framework.logging.core.MixmicroLog;
import org.springframework.beans.factory.DisposableBean;

import java.util.LinkedList;
import java.util.List;

/**
 * {@link LoggingReportRetry} Support
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingReportRetrySupport implements LoggingReportRetry, DisposableBean {
  /** the bean name of {@link LoggingReportRetrySupport} */
  public static final String BEAN_NAME = "loggingReportRetry";
  /** Log waiting for retry report */
  private static final LinkedList<MixmicroLog> LOGS = new LinkedList<>();

  @Override
  public void addToRetryCollection(MixmicroLog mixmicroLog) {
    LOGS.add(mixmicroLog);
  }

  @Override
  public void addToRetryCollection(List<MixmicroLog> mixmicroLogs) {
    LOGS.addAll(mixmicroLogs);
  }

  @Override
  public List<MixmicroLog> getAllRetryLogs() {
    return LOGS;
  }

  @Override
  public void destroy() throws Exception {}
}
