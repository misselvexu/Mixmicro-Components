package xyz.vopen.framework.logging.client.global;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MixmicroLoggingThreadLocal.applicationContext = applicationContext;
    }

    /**
     * GlobalLog {@link ThreadLocal} define
     */
    private static final TransmittableThreadLocal<List<MixmicroGlobalLog>> GLOBAL_LOGS = new TransmittableThreadLocal();

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
        if (null == applicationContext) {
            return;
        }
        List<String> globalLogExecutePackages = applicationContext.getBean(LoggingFactoryBean.class).getGlobalLogExecutePackage();
        String callerClass = mixmicroGlobalLog.getCallerClass();
        if (!CollectionUtils.isEmpty(globalLogExecutePackages)
                && globalLogExecutePackages.stream().noneMatch(callerClass::contains)) {
            return;
        }

        List<MixmicroGlobalLog> mixmicroGlobalLogs = getGlobalLogs();
        if (ObjectUtils.isEmpty(mixmicroGlobalLogs)) {
            mixmicroGlobalLogs = new LinkedList();
        }
        mixmicroGlobalLogs.add(mixmicroGlobalLog);
        GLOBAL_LOGS.set(mixmicroGlobalLogs);
    }

    /**
     * Delete {@link MixmicroGlobalLog} list in ThreadLocal
     */
    public static void remove() {
        GLOBAL_LOGS.remove();
    }
}
