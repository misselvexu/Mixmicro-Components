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

package xyz.vopen.framework.logging.admin.listener;

import com.alibaba.fastjson.JSON;
import org.springframework.lang.NonNull;
import xyz.vopen.framework.logging.admin.LoggingAdminFactoryBean;
import xyz.vopen.framework.logging.admin.endpoint.LoggingEndpoint;
import xyz.vopen.framework.logging.admin.event.ReportLogEvent;
import xyz.vopen.framework.logging.core.LoggingClientNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import xyz.vopen.framework.util.JsonUtil;

/**
 * Report Log Json Format Listener
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see ReportLogEvent
 */
public class ReportLogJsonFormatListener implements SmartApplicationListener {
  /** The bean name of {@link ReportLogJsonFormatListener} */
  public static final String BEAN_NAME = "reportLogJsonFormatListener";
  /** logger instance */
  static Logger logger = LoggerFactory.getLogger(ReportLogJsonFormatListener.class);

  private LoggingAdminFactoryBean loggingAdminFactoryBean;

  public ReportLogJsonFormatListener(LoggingAdminFactoryBean loggingAdminFactoryBean) {
    this.loggingAdminFactoryBean = loggingAdminFactoryBean;
  }

  /**
   * Report logs on console output Format Update Log
   *
   * @param event ReportLogEvent
   */
  @Override
  public void onApplicationEvent(@NonNull ApplicationEvent event) {
    try {
      ReportLogEvent reportLogEvent = (ReportLogEvent) event;
      if (loggingAdminFactoryBean.isShowConsoleReportLog()) {
        LoggingClientNotice notice = reportLogEvent.getLogClientNotice();
        String serviceInfo =
            String.format("%s -> %s", notice.getClientServiceId(), notice.getClientServiceIp());
        logger.info(
            "Receiving Service: 【{}】, Request Log Report，Logging Content：{}",
            serviceInfo,
            loggingAdminFactoryBean.isFormatConsoleLogJson()
                ? JsonUtil.beautifyJson(notice.getLoggers())
                : JSON.toJSONString(notice.getLoggers()));
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public boolean supportsSourceType(Class<?> sourceType) {
    return sourceType == LoggingEndpoint.class;
  }

  @Override
  public boolean supportsEventType(@NonNull Class<? extends ApplicationEvent> eventType) {
    return eventType == ReportLogEvent.class;
  }

  /**
   * first execute
   *
   * @return execute order
   */
  @Override
  public int getOrder() {
    return 0;
  }
}
