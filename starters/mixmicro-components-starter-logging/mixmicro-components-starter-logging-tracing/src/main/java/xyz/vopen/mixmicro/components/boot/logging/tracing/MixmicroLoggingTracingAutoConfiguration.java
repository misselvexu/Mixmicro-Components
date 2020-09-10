package xyz.vopen.mixmicro.components.boot.logging.tracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.ObjectUtils;
import xyz.vopen.framework.logging.client.LoggingFactoryBean;
import xyz.vopen.framework.logging.client.admin.discovery.LoggingAdminDiscovery;
import xyz.vopen.framework.logging.client.admin.report.LoggingReportScheduled;
import xyz.vopen.framework.logging.spring.context.annotation.client.EnableLoggingClient;

import java.util.List;

/**
 * Mixmicro Boot Logging Auto Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass({LoggingFactoryBean.class, EnableLoggingClient.class})
@EnableConfigurationProperties(MixmicroLoggingTracingProperties.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ConditionalOnWebApplication
@EnableAsync
@Import({
    MixmicroLoggingTracingOpenfeignAutoConfiguration.class,
    MixmicroLoggingTracingRestTemplateAutoConfiguration.class,
    MixmicroLoggingTracingWebAutoConfiguration.class,
    MixmicroLoggingTracingGlobalLogStorageAutoConfiguration.class
})
public class MixmicroLoggingTracingAutoConfiguration {
  /**
   * logger instance
   */
  static Logger logger = LoggerFactory.getLogger(MixmicroLoggingTracingAutoConfiguration.class);
  /**
   * Mixmicro Boot Logging Properties
   */
  private final MixmicroLoggingTracingProperties mixmicroLoggingTracingProperties;

  public MixmicroLoggingTracingAutoConfiguration(MixmicroLoggingTracingProperties mixmicroLoggingTracingProperties) {
    this.mixmicroLoggingTracingProperties = mixmicroLoggingTracingProperties;
  }

  /**
   * logging factory bean {@link LoggingFactoryBean}
   *
   * @param loggingAdminDiscoveryObjectProvider Logging Admin Discovery Instance Provider
   * @return LoggingFactoryBean
   */
  @Bean
  @ConditionalOnMissingBean
  public LoggingFactoryBean loggingFactoryBean(
      ObjectProvider<LoggingAdminDiscovery> loggingAdminDiscoveryObjectProvider,
      ObjectProvider<List<LoggingFactoryBeanCustomizer>> customizerObjectProvider) {
    LoggingFactoryBean factoryBean = new LoggingFactoryBean();
    factoryBean.setIgnorePaths(mixmicroLoggingTracingProperties.getIgnorePaths());
    factoryBean.setIgnoreHttpStatus(mixmicroLoggingTracingProperties.getIgnoreHttpStatus());
    factoryBean.setReportAway(mixmicroLoggingTracingProperties.getReportWay());
    factoryBean.setNumberOfRequestLog(mixmicroLoggingTracingProperties.getReportNumberOfRequestLog());
    factoryBean.setReportInitialDelaySecond(mixmicroLoggingTracingProperties.getReportInitialDelaySecond());
    factoryBean.setReportIntervalSecond(mixmicroLoggingTracingProperties.getReportIntervalSecond());
    factoryBean.setLoggingAdminDiscovery(loggingAdminDiscoveryObjectProvider.getIfAvailable());
    factoryBean.setShowConsoleLog(mixmicroLoggingTracingProperties.isShowConsoleLog());
    factoryBean.setFormatConsoleLog(mixmicroLoggingTracingProperties.isFormatConsoleLogJson());

    List<LoggingFactoryBeanCustomizer> customizers = customizerObjectProvider.getIfAvailable();
    if (!ObjectUtils.isEmpty(customizers)) {
      customizers.forEach(customizer -> customizer.customize(factoryBean));
    }
    logger.info("【LoggingFactoryBean】init successfully.");
    return factoryBean;
  }

  /**
   * Logging Report Scheduled Task Job When the configuration parameter
   * "mixmicro.boot.logging.report-away=timing" is configured, the creation timing task is performed
   * to report log information to admin node {@link MixmicroLoggingTracingProperties} {@link
   * LoggingReportScheduled}
   *
   * @param factoryBean logging factory bean
   * @return LoggingReportScheduled
   */
  @Bean
  @ConditionalOnProperty(
      prefix = MixmicroLoggingTracingProperties.MIXMICRO_BOOT_LOGGING_PREFIX,
      name = "report-away",
      havingValue = "timing")
  @ConditionalOnMissingBean
  public LoggingReportScheduled loggingReportScheduled(LoggingFactoryBean factoryBean) {
    return new LoggingReportScheduled(factoryBean);
  }
}
