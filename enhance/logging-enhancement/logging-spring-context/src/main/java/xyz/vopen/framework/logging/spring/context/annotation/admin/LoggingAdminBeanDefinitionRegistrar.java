package xyz.vopen.framework.logging.spring.context.annotation.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import xyz.vopen.framework.logging.admin.endpoint.LoggingEndpoint;
import xyz.vopen.framework.logging.admin.listener.ReportLogJsonFormatListener;
import xyz.vopen.framework.logging.admin.listener.ReportLogStorageListener;
import xyz.vopen.framework.logging.admin.repository.LoggingDataRepository;
import xyz.vopen.framework.logging.spring.util.LoggingBeanUtils;

/**
 * Register logging admin beans{@link
 * LoggingBeanUtils#registerLoggingAdminBeans(BeanDefinitionRegistry)} register {@link
 * LoggingDataRepository} register {@link
 * ReportLogStorageListener} register {@link
 * ReportLogJsonFormatListener} register {@link
 * LoggingEndpoint}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingAdminBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(LoggingAdminBeanDefinitionRegistrar.class);

    @Override
    public void registerBeanDefinitions(
            AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        LoggingBeanUtils.registerLoggingAdminBeans(registry);
        logger.info("Logging admin beans register successfully.");
    }
}
