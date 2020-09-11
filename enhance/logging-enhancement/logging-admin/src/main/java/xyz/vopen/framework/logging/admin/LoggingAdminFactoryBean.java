package xyz.vopen.framework.logging.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.admin.endpoint.LoggingEndpoint;
import xyz.vopen.framework.logging.admin.listener.ReportLogJsonFormatListener;
import xyz.vopen.framework.logging.admin.listener.ReportLogStorageListener;
import xyz.vopen.framework.logging.admin.repository.LoggingDataRepository;
import xyz.vopen.framework.logging.admin.repository.impl.LoggingDataRepositoryImpl;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * MinBox Logging Admin FactoryBean Provide the parameter configuration needed by the management
 * side
 *
 * <p>Register the following beans to BeanFactory{@link
 * org.springframework.beans.factory.BeanFactory}： Register {@link LoggingDataRepository} bean
 * Register {@link ReportLogStorageListener} bean Register {@link ReportLogJsonFormatListener} bean
 * Register {@link LoggingEndpoint} bean
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.1
 */
public class LoggingAdminFactoryBean implements ApplicationContextAware {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(LoggingAdminFactoryBean.class);
    /**
     * Whether to display the logs reported on the console
     */
    private boolean showConsoleReportLog;
    /**
     * Does the console display the formatted log JSON
     */
    private boolean formatConsoleLogJson;
    /**
     * spring {@link ApplicationContext} application context
     */
    private ApplicationContext applicationContext;
    /**
     * {@link AdminUiSetting} setting logging admin ui config
     */
    private AdminUiSetting adminUiSetting;
    /**
     * {@link LoggingDataRepository} default datasource support
     */
    private LoggingDataRepository loggingDataRepository;
    /**
     * mongodb template support
     */
    private MongoTemplate mongoTemplate;

    /**
     * init default instance
     */
    public LoggingAdminFactoryBean() {
        this.adminUiSetting = new AdminUiSetting();
        this.loggingDataRepository = new LoggingDataRepositoryImpl(mongoTemplate);
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

    public LoggingDataRepository getLoggingDataRepository() {
        return loggingDataRepository;
    }

    public void setLoggingDataRepository(LoggingDataRepository loggingDataRepository) {
        this.loggingDataRepository = loggingDataRepository;
    }

    public AdminUiSetting getAdminUiSetting() {
        return adminUiSetting;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        logger.debug("ApplicationContext set complete.");
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Logging Admin Ui Setting
     */
    public static class AdminUiSetting {
        /**
         * page title
         */
        private String title = "MinBox Logging Admin";
        /**
         * Mixmicro Boot white logo
         */
        private String brand = "<img src=\"assets/img/icon-white.png\">";
        /**
         * notification filter enable
         */
        private boolean notificationFilterEnabled;
        /**
         * remember me enabled
         */
        private boolean rememberMeEnabled;
        /**
         * page routes
         */
        private List<String> routes = asList("/about/**", "/services/**", "/logs/**", "/wallboard/**");

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public boolean isNotificationFilterEnabled() {
            return notificationFilterEnabled;
        }

        public void setNotificationFilterEnabled(boolean notificationFilterEnabled) {
            this.notificationFilterEnabled = notificationFilterEnabled;
        }

        public boolean isRememberMeEnabled() {
            return rememberMeEnabled;
        }

        public void setRememberMeEnabled(boolean rememberMeEnabled) {
            this.rememberMeEnabled = rememberMeEnabled;
        }

        public List<String> getRoutes() {
            return routes;
        }

        public void setRoutes(List<String> routes) {
            this.routes = routes;
        }
    }
}
