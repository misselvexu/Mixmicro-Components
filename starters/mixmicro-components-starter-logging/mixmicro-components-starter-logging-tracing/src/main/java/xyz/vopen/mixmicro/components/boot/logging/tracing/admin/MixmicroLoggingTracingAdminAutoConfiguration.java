package xyz.vopen.mixmicro.components.boot.logging.tracing.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import xyz.vopen.framework.logging.admin.LoggingAdminFactoryBean;
import xyz.vopen.framework.logging.admin.repository.LoggingDataSourceRepository;
import xyz.vopen.framework.logging.admin.repository.LoggingRepository;
import xyz.vopen.mixmicro.components.boot.logging.tracing.admin.ui.MixmicroLoggingTracingAdminUiAutoConfiguration;

import javax.sql.DataSource;

/**
 * Mixmicro Boot Logging Admin Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass(LoggingAdminFactoryBean.class)
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(MixmicroLoggingTracingAdminProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@Import({MixmicroLoggingTracingAdminUiAutoConfiguration.class})
@EnableAsync
public class MixmicroLoggingTracingAdminAutoConfiguration {
  /**
   * logger instance
   */
  static Logger logger = LoggerFactory.getLogger(MixmicroLoggingTracingAdminAutoConfiguration.class);
  /**
   * {@link MixmicroLoggingTracingAdminProperties}
   */
  private MixmicroLoggingTracingAdminProperties mixmicroLoggingTracingAdminProperties;

  public MixmicroLoggingTracingAdminAutoConfiguration(
      MixmicroLoggingTracingAdminProperties mixmicroLoggingTracingAdminProperties) {
    this.mixmicroLoggingTracingAdminProperties = mixmicroLoggingTracingAdminProperties;
  }

  /**
   * {@link LoggingRepository} database
   *
   * @param dataSource {@link DataSource}
   * @return {@link LoggingDataSourceRepository}
   */
  @Bean
  @ConditionalOnMissingBean
  public LoggingDataSourceRepository loggingDataSourceStorage(DataSource dataSource) {
    return new LoggingDataSourceRepository(dataSource);
  }

  /**
   * instantiation {@link LoggingAdminFactoryBean}
   *
   * @param loggingDataSourceStorage {@link LoggingDataSourceRepository}
   * @return LoggingAdminFactoryBean
   */
  @Bean
  @ConditionalOnMissingBean
  public LoggingAdminFactoryBean loggingAdminFactoryBean(
      LoggingDataSourceRepository loggingDataSourceStorage) {
    LoggingAdminFactoryBean factoryBean = new LoggingAdminFactoryBean();
    factoryBean.setLoggingRepository(loggingDataSourceStorage);
    factoryBean.setShowConsoleReportLog(mixmicroLoggingTracingAdminProperties.isShowConsoleReportLog());
    factoryBean.setFormatConsoleLogJson(mixmicroLoggingTracingAdminProperties.isFormatConsoleLogJson());
    logger.info("【LoggingAdminFactoryBean】init successfully.");
    return factoryBean;
  }

  /**
   * Verify that the {@link DataSource} exists and perform an exception alert when it does not exist
   */
  @Configuration
  @ConditionalOnMissingBean(DataSource.class)
  public static class DataSourceNotFoundConfiguration implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
      throw new BeanCreationException("No " + DataSource.class.getName() + " Found.");
    }
  }
}
