package xyz.vopen.framework.logging.admin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

/**
 * {@link RequestLogModel} request logs mongodb mapping model
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
@Document(collection = "logging_request_logs")
public class RequestLogModel implements Serializable {
  /** primary key */
  @Id private String id;
  /** service detail id, relation to LogServiceDetail's primary key */
  private String serviceDetailId;
  /** all log trace id */
  private String traceId;
  /** parent span id */
  private String parentSpanId;
  /** span id */
  private String spanId;
  /** http status code */
  private Integer httpStatus;
  /** request ip */
  private String requestIp;
  /** request uri */
  private String requestUri;
  /** request method:GET\POST */
  private String requestMethod;
  /** request headers */
  private Map<String, String> requestHeaders;
  /** request params */
  private String requestParams;
  /** request body */
  private String requestBody;
  /** response headers */
  private Map<String, String> responseHeaders;
  /** response body */
  private String responseBody;
  /** this request time consuming */
  private Long timeConsuming;
  /** exception stack */
  private String exceptionStack;
  /** request start time */
  private Long startTime;
  /** request end time */
  private Long endTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getServiceDetailId() {
    return serviceDetailId;
  }

  public void setServiceDetailId(String serviceDetailId) {
    this.serviceDetailId = serviceDetailId;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public String getParentSpanId() {
    return parentSpanId;
  }

  public void setParentSpanId(String parentSpanId) {
    this.parentSpanId = parentSpanId;
  }

  public String getSpanId() {
    return spanId;
  }

  public void setSpanId(String spanId) {
    this.spanId = spanId;
  }

  public Integer getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(Integer httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getRequestIp() {
    return requestIp;
  }

  public void setRequestIp(String requestIp) {
    this.requestIp = requestIp;
  }

  public String getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  public Map<String, String> getRequestHeaders() {
    return requestHeaders;
  }

  public void setRequestHeaders(Map<String, String> requestHeaders) {
    this.requestHeaders = requestHeaders;
  }

  public String getRequestParams() {
    return requestParams;
  }

  public void setRequestParams(String requestParams) {
    this.requestParams = requestParams;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

  public Map<String, String> getResponseHeaders() {
    return responseHeaders;
  }

  public void setResponseHeaders(Map<String, String> responseHeaders) {
    this.responseHeaders = responseHeaders;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }

  public Long getTimeConsuming() {
    return timeConsuming;
  }

  public void setTimeConsuming(Long timeConsuming) {
    this.timeConsuming = timeConsuming;
  }

  public String getExceptionStack() {
    return exceptionStack;
  }

  public void setExceptionStack(String exceptionStack) {
    this.exceptionStack = exceptionStack;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }
}
