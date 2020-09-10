package xyz.vopen.mixmicro.components.boot.logging.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.vopen.framework.logging.admin.LoggingAdminFactoryBean;
import xyz.vopen.framework.logging.admin.repository.LoggingDataRepository;
import xyz.vopen.framework.logging.admin.repository.LoggingDataRepositoryImpl;
import xyz.vopen.framework.logging.spring.context.annotation.admin.EnableLoggingAdmin;

/**
 * Mixmicro Boot Logging Admin Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass(EnableLoggingAdmin.class)
@EnableConfigurationProperties(MixmicroLoggingTracingAdminProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MixmicroLoggingTracingAdminAutoConfiguration implements WebMvcConfigurer {
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
     * mongodb database template
     *
     * @param mongoTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public LoggingDataRepository loggingDataRepository(MongoTemplate mongoTemplate) {
        return new LoggingDataRepositoryImpl(mongoTemplate);
    }

    /**
     * instantiation {@link LoggingAdminFactoryBean}
     *
     * @param loggingDataRepository {@link LoggingDataRepository}
     * @return LoggingAdminFactoryBean
     */
    @Bean
    @ConditionalOnMissingBean
    public LoggingAdminFactoryBean loggingAdminFactoryBean(
            LoggingDataRepository loggingDataRepository) {
        LoggingAdminFactoryBean factoryBean = new LoggingAdminFactoryBean();
        factoryBean.setLoggingDataRepository(loggingDataRepository);
        factoryBean.setShowConsoleReportLog(mixmicroLoggingTracingAdminProperties.isShowConsoleReportLog());
        factoryBean.setFormatConsoleLogJson(mixmicroLoggingTracingAdminProperties.isFormatConsoleLogJson());
        logger.info("【LoggingAdminFactoryBean】init successfully.");
        return factoryBean;
    }
}
