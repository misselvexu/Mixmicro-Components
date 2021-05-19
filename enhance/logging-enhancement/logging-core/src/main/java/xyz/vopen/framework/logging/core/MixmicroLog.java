/*
 * Copyright (c) 2018 VOPEN.XYZ
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package xyz.vopen.framework.logging.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Mixmicro Boot Log Object
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MixmicroLog implements Serializable {
  /** service id */
  private String serviceId;
  /** service detail id, relation to LogServiceDetail's primary key */
  private String serviceDetailId;
  /** trace id */
  private String traceId;
  /** span id */
  private String spanId;
  /** parent span id */
  private String parentSpanId;
  /** request uri */
  private String requestUri;
  /** request method */
  private String requestMethod;
  /** http status code */
  private int httpStatus;
  /** request ip */
  private String requestIp;
  /** service ip address */
  private String serviceIp;
  /** service port */
  private String servicePort;
  /** start time */
  private Long startTime;
  /** end time */
  private Long endTime;
  /** this request time consuming */
  private long timeConsuming;
  /** request headers */
  private Map<String, String> requestHeaders;
  /** request param */
  private String requestParam;
  /** request body */
  private String requestBody;
  /** response headers */
  private Map<String, String> responseHeaders;
  /** response body */
  private String responseBody;
  /** exception stack */
  private String exceptionStack;
  /** Global method log list */
  private List<MixmicroGlobalLog> mixmicroGlobalLogs;

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

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public String getSpanId() {
    return spanId;
  }

  public void setSpanId(String spanId) {
    this.spanId = spanId;
  }

  public String getParentSpanId() {
    return parentSpanId;
  }

  public void setParentSpanId(String parentSpanId) {
    this.parentSpanId = parentSpanId;
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

  public int getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(int httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getRequestIp() {
    return requestIp;
  }

  public void setRequestIp(String requestIp) {
    this.requestIp = requestIp;
  }

  public String getServiceIp() {
    return serviceIp;
  }

  public void setServiceIp(String serviceIp) {
    this.serviceIp = serviceIp;
  }

  public String getServicePort() {
    return servicePort;
  }

  public void setServicePort(String servicePort) {
    this.servicePort = servicePort;
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

  public long getTimeConsuming() {
    return timeConsuming;
  }

  public void setTimeConsuming(long timeConsuming) {
    this.timeConsuming = timeConsuming;
  }

  public Map<String, String> getRequestHeaders() {
    return requestHeaders;
  }

  public void setRequestHeaders(Map<String, String> requestHeaders) {
    this.requestHeaders = requestHeaders;
  }

  public String getRequestParam() {
    return requestParam;
  }

  public void setRequestParam(String requestParam) {
    this.requestParam = requestParam;
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

  public String getExceptionStack() {
    return exceptionStack;
  }

  public void setExceptionStack(String exceptionStack) {
    this.exceptionStack = exceptionStack;
  }

  public List<MixmicroGlobalLog> getMixmicroGlobalLogs() {
    return mixmicroGlobalLogs;
  }

  public void setMixmicroGlobalLogs(List<MixmicroGlobalLog> mixmicroGlobalLogs) {
    this.mixmicroGlobalLogs = mixmicroGlobalLogs;
  }
}
