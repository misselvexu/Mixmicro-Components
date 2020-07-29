package xyz.vopen.framework.logging.client.global.support;

import xyz.vopen.framework.logging.client.global.AbstractMixmicroLogging;
import xyz.vopen.framework.logging.client.global.MixmicroLoggingThreadLocal;
import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLogLevel;
import org.springframework.util.ObjectUtils;
import xyz.vopen.framework.util.StackTraceUtil;

/**
 * Global log memory mode repository implementation
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MixmicroLoggingMemoryStorage extends AbstractMixmicroLogging {
  /**
   * collection debug level log
   *
   * @param msg log content
   */
  @Override
  public void debug(String msg) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.debug, msg);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  /**
   * collection debug level log for example: this is test log,value is {}
   *
   * @param format Unformatted log content
   * @param arguments List of parameters corresponding to log content
   */
  @Override
  public void debug(String format, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.debug, log);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void info(String msg) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.info, msg);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void info(String format, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.info, log);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void error(String msg) {
    this.error(msg, java.util.Optional.ofNullable(null));
  }

  @Override
  public void error(String msg, Throwable throwable) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.error, msg);
    if (!ObjectUtils.isEmpty(throwable)) {
      String exceptionStack = StackTraceUtil.getStackTrace(throwable);
      mixmicroGlobalLog.setExceptionStack(exceptionStack);
    }
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void error(String format, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.error, log);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }
}
