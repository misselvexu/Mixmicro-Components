package xyz.vopen.framework.logging.client.global.support;

import org.springframework.util.ObjectUtils;
import xyz.vopen.framework.logging.client.global.AbstractMixmicroLogging;
import xyz.vopen.framework.logging.client.global.MixmicroLoggingThreadLocal;
import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLogLevel;
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
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.DEBUG, msg);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  /**
   * collection debug level log
   *
   * @param msg log content
   * @param callerClass logger caller class name
   */
  @Override
  public void debug(String msg, String callerClass) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.DEBUG, msg);
    mixmicroGlobalLog.setCallerClass(callerClass);
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
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.DEBUG, log);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  /**
   * collection debug level log for example: this is test log,value is {}
   *
   * @param format Unformatted log content
   * @param callerClass logger caller class name
   * @param arguments List of parameters corresponding to log content
   */
  @Override
  public void debug(String format, String callerClass, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.DEBUG, log);
    mixmicroGlobalLog.setCallerClass(callerClass);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void info(String msg) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.INFO, msg);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void info(String msg, String callerClass) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.INFO, msg);
    mixmicroGlobalLog.setCallerClass(callerClass);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void info(String format, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.INFO, log);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void info(String format, String callerClass, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.INFO, log);
    mixmicroGlobalLog.setCallerClass(callerClass);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void error(String msg) {
    this.error(msg, java.util.Optional.ofNullable(null));
  }

  @Override
  public void error(String msg, String callerClass) {
    this.error(msg, callerClass, java.util.Optional.ofNullable(null));
  }

  @Override
  public void error(String msg, Throwable throwable) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.ERROR, msg);
    if (!ObjectUtils.isEmpty(throwable)) {
      String exceptionStack = StackTraceUtil.getStackTrace(throwable);
      mixmicroGlobalLog.setExceptionStack(exceptionStack);
    }
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void error(String msg, String callerClass, Throwable throwable) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.ERROR, msg);
    if (!ObjectUtils.isEmpty(throwable)) {
      String exceptionStack = StackTraceUtil.getStackTrace(throwable);
      mixmicroGlobalLog.setExceptionStack(exceptionStack);
    }
    mixmicroGlobalLog.setCallerClass(callerClass);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void error(String format, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.ERROR, log);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void error(String format, String callerClass, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.ERROR, log);
    mixmicroGlobalLog.setCallerClass(callerClass);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void warn(String msg) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.WARN, msg);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void warn(String msg, String callerClass) {
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.WARN, msg);
    mixmicroGlobalLog.setCallerClass(callerClass);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void warn(String format, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.WARN, log);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }

  @Override
  public void warn(String format, String callerClass, Object... arguments) {
    String log = replacePlaceholder(format, arguments);
    MixmicroGlobalLog mixmicroGlobalLog = buildGlobalLog(MixmicroLogLevel.WARN, log);
    mixmicroGlobalLog.setCallerClass(callerClass);
    MixmicroLoggingThreadLocal.addGlobalLogs(mixmicroGlobalLog);
  }
}
