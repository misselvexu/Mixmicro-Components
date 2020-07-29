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

package xyz.vopen.framework.logging.client.http.openfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import xyz.vopen.framework.logging.client.LogThreadLocal;
import xyz.vopen.framework.logging.client.LoggingConstant;
import xyz.vopen.framework.logging.core.MixmicroLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Openfeign Request Interceptor Requests initiated by openfeign carry Mixmicro Boot TraceId and spanId
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingOpenFeignInterceptor implements RequestInterceptor {
  /** logger instance */
  static Logger logger = LoggerFactory.getLogger(LoggingOpenFeignInterceptor.class);

  @Override
  public void apply(RequestTemplate requestTemplate) {
    MixmicroLog log = LogThreadLocal.get();
    requestTemplate.header(LoggingConstant.HEADER_NAME_TRACE_ID, log.getTraceId());
    requestTemplate.header(LoggingConstant.HEADER_NAME_PARENT_SPAN_ID, log.getSpanId());
    logger.debug(
        "RequestUri：{}, Method：{}，Setting Logging TraceId：{}，SpanId：{} With Openfeign.",
        requestTemplate.url(),
        requestTemplate.request().httpMethod().toString(),
        log.getTraceId(),
        log.getSpanId());
  }
}
