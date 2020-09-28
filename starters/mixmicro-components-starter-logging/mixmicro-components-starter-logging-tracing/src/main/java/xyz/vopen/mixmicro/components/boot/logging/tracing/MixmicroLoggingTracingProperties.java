package xyz.vopen.mixmicro.components.boot.logging.tracing;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import xyz.vopen.framework.logging.client.admin.discovery.lb.LoadBalanceStrategy;
import xyz.vopen.framework.logging.client.admin.discovery.lb.support.RandomWeightedStrategy;
import xyz.vopen.framework.logging.client.admin.discovery.lb.support.SmoothWeightedRoundRobinStrategy;
import xyz.vopen.framework.logging.core.ReportWay;

import java.util.ArrayList;
import java.util.List;

import static xyz.vopen.mixmicro.components.boot.logging.tracing.MixmicroLoggingTracingProperties.MIXMICRO_BOOT_LOGGING_PREFIX;

/**
 * Mixmicro Boot Logging Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * <p>DateTime：2019-07-15 22:29
 */
@ConfigurationProperties(prefix = MIXMICRO_BOOT_LOGGING_PREFIX)
@Data
public class MixmicroLoggingTracingProperties {
    /**
     * Mixmicro Boot logging properties config prefix
     */
    public static final String MIXMICRO_BOOT_LOGGING_PREFIX = "mixmicro.logging.tracing";

    /**
     * Interception log path prefix
     */
    private String[] loggingPathPrefix = new String[]{"/**"};
    /**
     * Ignore path array
     */
    private String[] ignorePaths;

    /**
     * Ignore the {@link HttpStatus} of not logging
     */
    private List<HttpStatus> ignoreHttpStatus =
            new ArrayList() {
                {
                    add(HttpStatus.NOT_FOUND);
                }
            };

    /**
     * Format console log JSON
     */
    private boolean formatConsoleLogJson = false;
    /**
     * show console log
     */
    private boolean showConsoleLog = false;
    /**
     * Report Request Log To Admin Away
     */
    private ReportWay reportWay = ReportWay.JUST;
    /**
     * Number of request logs reported once
     */
    private int reportNumberOfRequestLog = 10;
    /**
     * report to admin initial delay second
     */
    private int reportInitialDelaySecond = 5;
    /**
     * report to admin interval second
     */
    private int reportIntervalSecond = 5;
    /**
     * logging cache away
     */
    private LoggingCacheWay loggingCacheWay = LoggingCacheWay.MEMORY;
    /**
     * global logging repository away
     */
    private MixmicroLoggingTracingStorageWay mixmicroLoggingTracingStorageWay = MixmicroLoggingTracingStorageWay.MEMORY;
    /**
     * Mixmicro Boot Logging Admin Instance
     */
    private AdminInstance admin = new AdminInstance();
    /**
     * Mixmicro Boot Logging Discovery Instance support eureka
     */
    private DiscoveryInstance discovery;
    /**
     * Choose load balancing strategy for admin report log {@link
     * LoadBalanceStrategy}
     *
     * @see RandomWeightedStrategy
     * @see SmoothWeightedRoundRobinStrategy
     */
    private LoadBalanceStrategyWay loadBalanceStrategy = LoadBalanceStrategyWay.RANDOM_WEIGHT;

    /**
     * Config Mixmicro Boot Logging Admin Server report every request log to api-boot-logging-admin
     */
    @Data
    public static class AdminInstance {
        /**
         * Mixmicro Boot Logging Admin Server Address
         */
        private String serverAddress;
    }

    /**
     * Config Mixmicro Boot Logging Discovery Instance Draw the list of Mixmicro Boot Logging Admin addresses from
     * the registry and report the request log through load balancing
     */
    @Data
    public static class DiscoveryInstance {
        /**
         * Mixmicro Boot Logging Admin Spring Security Username
         */
        private String username;
        /**
         * Mixmicro Boot Logging Admin Spring Security User Password
         */
        private String password;
        /**
         * Mixmicro Boot Logging Admin Service ID
         */
        private String serviceId;
    }

    /**
     * admin trace log report url
     */
    private String reportUrl = "localhost:8060";
}
