package xyz.vopen.framework.logging.client.global;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import xyz.vopen.framework.logging.client.LoggingFactoryBean;
import xyz.vopen.framework.logging.core.MixmicroGlobalLog;

import java.util.LinkedList;
import java.util.List;

/**
 * Use threadLocal to store all GlobalLogs in a request that need to be saved
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MixmicroLoggingThreadLocal implements ApplicationContextAware {

  public static final String BEAN_NAME = "loggingThreadLocal";

  private static ApplicationContext context;

  public static ApplicationContext getApplicationContext() {
    return context;
  }

  @Override
  public void setApplicationContext(ApplicationContext ctx) {
    Assert.notNull(ctx, "ApplicationContext must not be null");
    context = ctx;
  }

  /** GlobalLog {@link ThreadLocal} define */
  private static final TransmittableThreadLocal<List<MixmicroGlobalLog>> GLOBAL_LOGS =
      new TransmittableThreadLocal();

  /**
   * Get {@link MixmicroGlobalLog} List from ThreadLocal
   *
   * @return {@link MixmicroGlobalLog}
   */
  public static List<MixmicroGlobalLog> getGlobalLogs() {
    return GLOBAL_LOGS.get();
  }

  /**
   * Add {@link MixmicroGlobalLog} to ThreadLocal
   *
   * @param mixmicroGlobalLog {@link MixmicroGlobalLog}
   */
  public static void addGlobalLogs(MixmicroGlobalLog mixmicroGlobalLog) {
    ApplicationContext applicationContext = getApplicationContext();
    List<String> globalLogExecutePackages =
        applicationContext != null
            ? applicationContext.getBean(LoggingFactoryBean.class).getGlobalLogExecutePackage()
            : null;
    String callerClass = mixmicroGlobalLog.getCallerClass();
    mixmicroGlobalLog.getCallerMethod();
    if (CollectionUtils.isEmpty(globalLogExecutePackages)) {
      return;
    }
    if (globalLogExecutePackages.stream().noneMatch(callerClass::contains)) {
      return;
    }

    List<MixmicroGlobalLog> mixmicroGlobalLogs = getGlobalLogs();
    if (ObjectUtils.isEmpty(mixmicroGlobalLogs)) {
      mixmicroGlobalLogs = new LinkedList();
    }
    mixmicroGlobalLogs.add(mixmicroGlobalLog);
    GLOBAL_LOGS.set(mixmicroGlobalLogs);
  }

  /** Delete {@link MixmicroGlobalLog} list in ThreadLocal */
  public static void remove() {
    GLOBAL_LOGS.remove();
  }
}
