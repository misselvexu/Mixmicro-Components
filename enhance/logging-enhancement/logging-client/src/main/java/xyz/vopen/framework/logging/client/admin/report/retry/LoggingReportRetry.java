package xyz.vopen.framework.logging.client.admin.report.retry;

import xyz.vopen.framework.logging.core.MixmicroLog;

import java.util.List;

/**
 * Request loggers retry interface definition use to log recycling, log retry, file caching
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface LoggingReportRetry {
  /**
   * Add {@link MixmicroLog} to retry log collection
   *
   * @param mixmicroLog {@link MixmicroLog}
   */
  void addToRetryCollection(MixmicroLog mixmicroLog);

  /**
   * Add {@link MixmicroLog} list to retry log collection
   *
   * @param mixmicroLogs {@link MixmicroLog}
   */
  void addToRetryCollection(List<MixmicroLog> mixmicroLogs);

  /**
   * Get all {@link MixmicroLog} from retry collection
   *
   * @return {@link MixmicroLog}
   */
  List<MixmicroLog> getAllRetryLogs();
}
