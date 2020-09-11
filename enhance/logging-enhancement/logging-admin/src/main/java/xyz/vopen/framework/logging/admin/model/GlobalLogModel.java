package xyz.vopen.framework.logging.admin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import xyz.vopen.framework.logging.core.MixmicroLogLevel;

import java.io.Serializable;

/**
 * global log mongodb mapping model
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/9
 */
@Data
@Document(collection = "logging_global_logs")
public class GlobalLogModel implements Serializable {
    /**
     * Global log primary key
     */
    @Id
    private String id;
    /**
     * request log primary key
     */
    private String requestLogId;
    /**
     * Global log level {@link MixmicroLogLevel}
     */
    private MixmicroLogLevel level;
    /**
     * all level's log content
     */
    private String content;
    /**
     * caller class name {@link StackTraceElement#getClassName()}
     */
    private String callerClass;
    /**
     * caller method name {@link StackTraceElement#getMethodName()}
     */
    private String callerMethod;
    /**
     * caller code line number {@link StackTraceElement#getLineNumber()}
     */
    private Integer callerCodeLineNumber;
    /**
     * Error stack information collected in error level logs
     */
    private String exceptionStack;
    /**
     * the global log create time default is current time millis
     */
    private Long createTime;
}
