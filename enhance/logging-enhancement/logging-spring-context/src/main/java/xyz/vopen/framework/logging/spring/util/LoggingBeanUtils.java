package xyz.vopen.framework.logging.spring.util;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import xyz.vopen.framework.logging.client.filter.LoggingBodyFilter;
import xyz.vopen.framework.logging.client.global.MixmicroLoggingThreadLocal;
import xyz.vopen.framework.logging.client.interceptor.web.LoggingWebInterceptor;
import xyz.vopen.framework.logging.client.notice.LoggingNoticeListener;
import xyz.vopen.framework.logging.client.notice.support.LoggingAdminNotice;
import xyz.vopen.framework.logging.client.notice.support.LoggingLocalNotice;
import xyz.vopen.framework.logging.core.mapping.LoggingRequestMappingHandlerMapping;
import xyz.vopen.framework.util.BeanUtils;

/**
 * Logging Bean Utilities classes
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.1
 */
public class LoggingBeanUtils {

  private LoggingBeanUtils() {}

  /**
   * Register LoggingAdmin beans {@link
   * BeanUtils#registerInfrastructureBeanIfAbsent(BeanDefinitionRegistry, String, Class, Object...)}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingAdminBeans(BeanDefinitionRegistry registry) {
    registerLoggingRequestMappingHandler(registry);
  }

  /**
   * Register logging client beans {@link
   * BeanUtils#registerInfrastructureBeanIfAbsent(BeanDefinitionRegistry, String, Class, Object...)}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingClientBeans(BeanDefinitionRegistry registry) {
    registerLoggingInterceptor(registry);
    registerLoggingBodyFilter(registry);
    registerLoggingNoticeListener(registry);
    registerLoggingLocalNotice(registry);
    registerLoggingAdminNotice(registry);
    registerLoggingThreadLocal(registry);
  }

  /**
   * Register logging request mapping handler Using the {@link LoggingRequestMappingHandlerMapping}
   * method to determine whether @Endpoint is configured on the class to load the request mapping
   * method bean name is use {@link LoggingRequestMappingHandlerMapping#BEAN_NAME}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingRequestMappingHandler(BeanDefinitionRegistry registry) {
    BeanUtils.registerInfrastructureBeanIfAbsent(
        registry,
        LoggingRequestMappingHandlerMapping.BEAN_NAME,
        LoggingRequestMappingHandlerMapping.class);
  }

  /**
   * Register logging interceptor {@link LoggingWebInterceptor} bean name is use {@link
   * LoggingWebInterceptor#BEAN_NAME}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingInterceptor(BeanDefinitionRegistry registry) {
    BeanUtils.registerInfrastructureBeanIfAbsent(
        registry, LoggingWebInterceptor.BEAN_NAME, LoggingWebInterceptor.class);
  }

  /**
   * Register logging body filter {@link LoggingBodyFilter} bean name is use {@link
   * LoggingBodyFilter#BEAN_NAME}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingBodyFilter(BeanDefinitionRegistry registry) {
    BeanUtils.registerInfrastructureBeanIfAbsent(
        registry, LoggingBodyFilter.BEAN_NAME, LoggingBodyFilter.class);
  }

  /**
   * Register logging notice listener {@link LoggingNoticeListener} bean name is use {@link
   * LoggingNoticeListener#BEAN_NAME}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingNoticeListener(BeanDefinitionRegistry registry) {
    BeanUtils.registerInfrastructureBeanIfAbsent(
        registry, LoggingNoticeListener.BEAN_NAME, LoggingNoticeListener.class);
  }

  /**
   * Register logging local notice {@link LoggingLocalNotice} bean name is use {@link
   * LoggingLocalNotice#BEAN_NAME}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingLocalNotice(BeanDefinitionRegistry registry) {
    BeanUtils.registerInfrastructureBeanIfAbsent(
        registry, LoggingLocalNotice.BEAN_NAME, LoggingLocalNotice.class);
  }

  /**
   * Register logging admin notice {@link LoggingAdminNotice} report request logging to admin bean
   * name is use {@link LoggingAdminNotice#BEAN_NAME}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingAdminNotice(BeanDefinitionRegistry registry) {
    BeanUtils.registerInfrastructureBeanIfAbsent(
        registry, LoggingAdminNotice.BEAN_NAME, LoggingAdminNotice.class);
  }

  /**
   * Register logging threadLocal {@link MixmicroLoggingThreadLocal} cache bean name is use {@link
   * MixmicroLoggingThreadLocal#BEAN_NAME}
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerLoggingThreadLocal(BeanDefinitionRegistry registry) {
    BeanUtils.registerInfrastructureBeanIfAbsent(
        registry, MixmicroLoggingThreadLocal.BEAN_NAME, MixmicroLoggingThreadLocal.class);
  }
}
