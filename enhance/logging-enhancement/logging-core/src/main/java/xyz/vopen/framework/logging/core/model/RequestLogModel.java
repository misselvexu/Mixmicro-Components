package xyz.vopen.framework.logging.core.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

/**
 * request logs mongodb mapping model
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/9
 */
@Data
@Document(collection = "logging_request_logs")
public class RequestLogModel implements Serializable {
    /**
     * primary key
     */
    @Id
    private String id;
    /**
     * service detail id, relation to LogServiceDetail's primary key
     */
    private String serviceDetailId;
    /**
     * all log trace id
     */
    private String traceId;
    /**
     * parent span id
     */
    private String parentSpanId;
    /**
     * span id
     */
    private String spanId;
    /**
     * http status code
     */
    private Integer httpStatus;
    /**
     * request ip
     */
    private String requestIp;
    /**
     * request uri
     */
    private String requestUri;
    /**
     * request method:GET\POST
     */
    private String requestMethod;
    /**
     * request headers
     */
    private Map<String, String> requestHeaders;
    /**
     * request params
     */
    private String requestParams;
    /**
     * request body
     */
    private String requestBody;
    /**
     * reponse headers
     */
    private Map<String, String> responseHeaders;
    /**
     * response body
     */
    private String responseBody;
    /**
     * this request time consuming
     */
    private Long timeConsuming;
    /**
     * excepiton stack
     */
    private String exceptionStack;
    /**
     * request start time
     */
    private Long startTime;
    /**
     * request end time
     */
    private Long endTime;
}
