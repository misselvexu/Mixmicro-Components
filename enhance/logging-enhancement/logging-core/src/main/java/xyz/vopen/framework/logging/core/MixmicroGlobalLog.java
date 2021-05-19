package xyz.vopen.framework.logging.core;

import java.io.Serializable;

/**
 * Global log data entity
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MixmicroGlobalLog implements Serializable {
  /** service id */
  private String serviceId;
  /** service detail id, relation to LogServiceDetail's primary key */
  private String serviceDetailId;
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

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getServiceDetailId() {
    return serviceDetailId;
  }

  public void setServiceDetailId(String serviceDetailId) {
    this.serviceDetailId = serviceDetailId;
  }

  public MixmicroLogLevel getLevel() {
    return level;
  }

  public void setLevel(MixmicroLogLevel level) {
    this.level = level;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getExceptionStack() {
    return exceptionStack;
  }

  public void setExceptionStack(String exceptionStack) {
    this.exceptionStack = exceptionStack;
  }

  public String getCallerClass() {
    return callerClass;
  }

  public void setCallerClass(String callerClass) {
    this.callerClass = callerClass;
  }

  public String getCallerMethod() {
    return callerMethod;
  }

  public void setCallerMethod(String callerMethod) {
    this.callerMethod = callerMethod;
  }

  public int getCallerCodeLineNumber() {
    return callerCodeLineNumber;
  }

  public void setCallerCodeLineNumber(int callerCodeLineNumber) {
    this.callerCodeLineNumber = callerCodeLineNumber;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }
}
