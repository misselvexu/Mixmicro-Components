package xyz.vopen.framework.logging.admin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import xyz.vopen.framework.logging.core.MixmicroLogLevel;

import java.io.Serializable;

/**
 * {@link GlobalLogModel} global log mongodb mapping model
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/9
 */
@Document(collection = "logging_global_logs")
public class GlobalLogModel implements Serializable {
  /** Global log primary key */
  @Id private String id;
  /** request log primary key */
  private String requestLogId;
  /** Global log level {@link MixmicroLogLevel} */
  private MixmicroLogLevel level;
  /** all level's log content */
  private String content;
  /** caller class name {@link StackTraceElement#getClassName()} */
  private String callerClass;
  /** caller method name {@link StackTraceElement#getMethodName()} */
  private String callerMethod;
  /** caller code line number {@link StackTraceElement#getLineNumber()} */
  private Integer callerCodeLineNumber;
  /** Error stack information collected in error level logs */
  private String exceptionStack;
  /** the global log create time default is current time millis */
  private Long createTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRequestLogId() {
    return requestLogId;
  }

  public void setRequestLogId(String requestLogId) {
    this.requestLogId = requestLogId;
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

  public Integer getCallerCodeLineNumber() {
    return callerCodeLineNumber;
  }

  public void setCallerCodeLineNumber(Integer callerCodeLineNumber) {
    this.callerCodeLineNumber = callerCodeLineNumber;
  }

  public String getExceptionStack() {
    return exceptionStack;
  }

  public void setExceptionStack(String exceptionStack) {
    this.exceptionStack = exceptionStack;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }
}
