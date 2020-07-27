package xyz.vopen.framework.logging.core;

import lombok.Data;

import java.io.Serializable;

/**
 * Global log data entity
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Data
public class MixmicroGlobalLog implements Serializable {
  /** Global log level {@link MixmicroLogLevel} */
  private MixmicroLogLevel level;
  /** all level's log content */
  private String content;
  /** Error stack information collected in error level logs */
  private String exceptionStack;
  /** caller class name {@link StackTraceElement#getClassName()} */
  private String callerClass;
  /** caller method name {@link StackTraceElement#getMethodName()} */
  private String callerMethod;
  /** caller code line number {@link StackTraceElement#getLineNumber()} */
  private int callerCodeLineNumber;
  /** the global log create time default is current time millis */
  private Long createTime = System.currentTimeMillis();
}
