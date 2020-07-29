package xyz.vopen.framework.logging.client.global;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.util.ObjectUtils;
import xyz.vopen.framework.logging.core.MixmicroGlobalLog;

import java.util.LinkedList;
import java.util.List;

/**
 * Use threadLocal to store all GlobalLogs in a request that need to be saved
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MixmicroLoggingThreadLocal {
  /** GlobalLog {@link ThreadLocal} define */
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
