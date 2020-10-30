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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import xyz.vopen.framework.logging.admin.LoggingAdminFactoryBean;
import xyz.vopen.framework.logging.admin.endpoint.LoggingEndpoint;
import xyz.vopen.framework.logging.admin.event.ReportLogEvent;
import xyz.vopen.framework.logging.admin.service.LoggingDataService;
import xyz.vopen.framework.logging.core.LoggingClientNotice;
import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLog;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Storage Report Logs Listener
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ReportLogStorageListener implements SmartApplicationListener {
    /**
     * The bean name of {@link ReportLogStorageListener}
     */
    public static final String BEAN_NAME = "reportLogStorageListener";
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(ReportLogStorageListener.class);
    /**
     * ServiceDetails IDS
     */
    ConcurrentMap<String, String> SERVICE_DETAIL_IDS = new ConcurrentHashMap();
    /**
     * Logging Storage Interface {@link LoggingDataService}
     */
    private LoggingDataService loggingDataService;

    /**
     * init instance method
     *
     * @param adminFactoryBean
     */
    public ReportLogStorageListener(LoggingAdminFactoryBean adminFactoryBean) {
        Assert.notNull(adminFactoryBean, "[LoggingAdminFactoryBean] Can't be null.");
        this.loggingDataService = adminFactoryBean.getLoggingDataService();
    }

    /**
     * Storage Log
     *
     * @param event ReportLogEvent
     */
    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent event) {
        try {
            logger.debug("Starting Storage Report Request Logs.");
            ReportLogEvent reportLogEvent = (ReportLogEvent) event;
            LoggingClientNotice notice = reportLogEvent.getLogClientNotice();
            String serviceDetail =
                    formatServiceDetailID(
                            notice.getClientServiceId(),
                            notice.getClientServiceIp(),
                            notice.getClientServicePort());
            String serviceDetailId = SERVICE_DETAIL_IDS.get(serviceDetail);
            // new service
            if (ObjectUtils.isEmpty(serviceDetailId)) {
                // select service detail id from database
                serviceDetailId =
                        loggingDataService.selectLogServiceDetailId(
                                notice.getClientServiceId(),
                                notice.getClientServiceIp(),
                                notice.getClientServicePort());
                // if don't have in database
                // create new service detail
                if (ObjectUtils.isEmpty(serviceDetailId)) {
                    serviceDetailId =
                            loggingDataService.insertLogServiceDetail(
                                    notice.getClientServiceId(),
                                    notice.getClientServiceIp(),
                                    notice.getClientServicePort());
                }
                if (!ObjectUtils.isEmpty(serviceDetailId)) {
                    SERVICE_DETAIL_IDS.put(serviceDetail, serviceDetailId);
                }
            }
            // save global logs
            if (!ObjectUtils.isEmpty(notice.getLoggers())) {
                for (MixmicroLog log : notice.getLoggers()) {
                    String requestLogId = loggingDataService.insertRequestLog(serviceDetailId, log);
                    // save global logs
                    saveGlobalLogs(requestLogId, log.getMixmicroGlobalLogs());
                }
            }
            // update last report time
            loggingDataService.updateLogServiceDetailLastReportTime(serviceDetailId);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            logger.debug("Storage Report Request Logs Complete.");
        }
    }

    /**
     * Save the global log contained in each request log
     *
     * @param requestLogId       request log id
     * @param mixmicroGlobalLogs {@link MixmicroGlobalLog}
     */
    private void saveGlobalLogs(String requestLogId, List<MixmicroGlobalLog> mixmicroGlobalLogs) {
        if (!ObjectUtils.isEmpty(mixmicroGlobalLogs) && !ObjectUtils.isEmpty(requestLogId)) {
            mixmicroGlobalLogs.forEach(
                    globalLog -> loggingDataService.insertGlobalLog(requestLogId, globalLog));
        }
    }

    /**
     * format serviceDetail ID
     *
     * @param serviceId   service id
     * @param serviceIp   service ip address
     * @param servicePort service port
     * @return
     */
    String formatServiceDetailID(String serviceId, String serviceIp, Integer servicePort) {
        Assert.notNull(serviceId, "Service Id Is Required.");
        Assert.notNull(serviceIp, "Service Ip Is Required.");
        Assert.notNull(servicePort, "Service Port Is Required.");
        return String.format("%s-%s:%d", serviceId, serviceIp, servicePort);
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == ReportLogEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == LoggingEndpoint.class;
    }

    /**
     * second execute
     *
     * @return execute order
     */
    @Override
    public int getOrder() {
        return 1;
    }
}
