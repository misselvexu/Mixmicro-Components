package xyz.vopen.framework.logging.client.global;

import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLogLevel;
import org.springframework.util.ObjectUtils;

/**
 * GlobalLogging abstract implementation Provide public methods required for log acquisition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class AbstractMixmicroLogging implements MixmicroLogging {
  /**
   * Get the StackTrace of the initial calling method caller class name {@link
   * StackTraceElement#getClassName()} caller method name {@link StackTraceElement#getMethodName()}
   * caller code line number {@link StackTraceElement#getLineNumber()}
   *
   * <p>Why is the index of the Get StackTraceElement 5? info、debug、error level log method
   * invocation process： 0. {@link #getCallMethodStackTrace()} 1. {@link #getCallerClassName()} or
   * {@link #getCallerMethodName()} or {@link #getCallerCodeLineNumber()} 2. {@link
   * #instanceGlobalLog()} 3. {@link #buildGlobalLog(MixmicroLogLevel, String)} 4. {@link
   * MixmicroLogging#info(String)} 5. Real business call method
   *
   * @return {@link StackTraceElement}
   */
  private StackTraceElement getCallMethodStackTrace() {
    StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
    return stackTraceElements[5];
  }

  /**
   * get caller class name
   *
   * @return {@link StackTraceElement#getClassName()}
   */
  protected String getCallerClassName() {
    return getCallMethodStackTrace().getClassName();
  }

  /**
   * get caller method name
   *
   * @return {@link StackTraceElement#getMethodName()}
   */
  protected String getCallerMethodName() {
    return getCallMethodStackTrace().getMethodName();
  }

  /**
   * get caller code line number
   *
   * @return {@link StackTraceElement#getLineNumber()}
   */
  protected int getCallerCodeLineNumber() {
    return getCallMethodStackTrace().getLineNumber();
  }

  /**
   * create the {@link MixmicroGlobalLog} object instance initialization set call information
   *
   * @return {@link MixmicroGlobalLog}
   */
  protected MixmicroGlobalLog instanceGlobalLog() {
    MixmicroGlobalLog mixmicroGlobalLog = new MixmicroGlobalLog();
    mixmicroGlobalLog.setCallerClass(getCallerClassName());
    mixmicroGlobalLog.setCallerMethod(getCallerMethodName());
    mixmicroGlobalLog.setCallerCodeLineNumber(getCallerCodeLineNumber());
    return mixmicroGlobalLog;
  }

  /**
   * Build Global Log Instance
   *
   * @param level {@link MixmicroLogLevel}
   * @param content {@link MixmicroGlobalLog#getContent()}
   * @return {@link MixmicroGlobalLog}
   */
  protected MixmicroGlobalLog buildGlobalLog(MixmicroLogLevel level, String content) {
    MixmicroGlobalLog mixmicroGlobalLog = instanceGlobalLog();
    mixmicroGlobalLog.setLevel(level);
    mixmicroGlobalLog.setContent(content);
    return mixmicroGlobalLog;
  }

  /**
   * Replace placeholders for log content
   *
   * @param format Unformatted log content
   * @param arguments List of parameters corresponding to log content
   * @return formatted log
   */
  protected String replacePlaceholder(String format, Object[] arguments) {
    if (!ObjectUtils.isEmpty(arguments)) {
      for (int i = 0; i < arguments.length; i++) {
        format = format.replaceFirst("\\{\\}", String.valueOf(arguments[i]));
      }
    }
    return format;
  }
}
