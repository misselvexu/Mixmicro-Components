package xyz.vopen.framework.logging.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.admin.endpoint.LoggingEndpoint;
import xyz.vopen.framework.logging.admin.listener.ReportLogJsonFormatListener;
import xyz.vopen.framework.logging.admin.listener.ReportLogStorageListener;
import xyz.vopen.framework.logging.admin.repository.GlobalLogRepository;
import xyz.vopen.framework.logging.admin.repository.LogServiceDetailRepository;
import xyz.vopen.framework.logging.admin.repository.RequestLogRepository;
import xyz.vopen.framework.logging.admin.service.LoggingDataService;

/**
 * MinBox Logging Admin FactoryBean Provide the parameter configuration needed by the management
 * side
 *
 * <p>Register the following beans to BeanFactory{@link
 * org.springframework.beans.factory.BeanFactory}： Register {@link LoggingDataService} bean Register
 * {@link ReportLogStorageListener} bean Register {@link ReportLogJsonFormatListener} bean Register
 * {@link LoggingEndpoint} bean
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.1
 */
public class LoggingAdminFactoryBean implements ApplicationContextAware {
  /** logger instance */
  static Logger logger = LoggerFactory.getLogger(LoggingAdminFactoryBean.class);
  /** Whether to display the logs reported on the console */
  private boolean showConsoleReportLog;
  /** Does the console display the formatted log JSON */
  private boolean formatConsoleLogJson;
  /** spring {@link ApplicationContext} application context */
  private ApplicationContext applicationContext;
  /** {@link LoggingDataService} default datasource support */
  private LoggingDataService loggingDataService;
  /** mongodb template support */
  private MongoTemplate mongoTemplate;

  /** Global log data repository interface */
  private GlobalLogRepository globalLogRepository;
  /** Mixmicro Boot Log repository interface */
  private RequestLogRepository requestLogRepository;
  /** logging service details repository interface */
  private LogServiceDetailRepository logServiceDetailRepository;

  public GlobalLogRepository getGlobalLogRepository() {
    return globalLogRepository;
  }

  public void setGlobalLogRepository(GlobalLogRepository globalLogRepository) {
    this.globalLogRepository = globalLogRepository;
  }

  public RequestLogRepository getRequestLogRepository() {
    return requestLogRepository;
  }

  public void setRequestLogRepository(RequestLogRepository requestLogRepository) {
    this.requestLogRepository = requestLogRepository;
  }

  public LogServiceDetailRepository getLogServiceDetailRepository() {
    return logServiceDetailRepository;
  }

  public void setLogServiceDetailRepository(LogServiceDetailRepository logServiceDetailRepository) {
    this.logServiceDetailRepository = logServiceDetailRepository;
  }

  public MongoTemplate getMongoTemplate() {
    return mongoTemplate;
  }

  public void setMongoTemplate(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public boolean isShowConsoleReportLog() {
    return showConsoleReportLog;
  }

  public void setShowConsoleReportLog(boolean showConsoleReportLog) {
    this.showConsoleReportLog = showConsoleReportLog;
  }

  public boolean isFormatConsoleLogJson() {
    return formatConsoleLogJson;
  }

  public void setFormatConsoleLogJson(boolean formatConsoleLogJson) {
    this.formatConsoleLogJson = formatConsoleLogJson;
  }

  public LoggingDataService getLoggingDataService() {
    return loggingDataService;
  }

  public void setLoggingDataService(LoggingDataService loggingDataService) {
    this.loggingDataService = loggingDataService;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    logger.debug("ApplicationContext set complete.");
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
