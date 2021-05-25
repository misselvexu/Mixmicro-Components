/*
 * Copyright (c) 2018 VOPEN.XYZ
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package xyz.vopen.framework.logging.client;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.util.Assert;
import xyz.vopen.framework.logging.core.MixmicroLog;

/**
 * Using threadLocal to store log objects in multithreaded situations
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LogThreadLocal {

  private LogThreadLocal() {}

  /**
   * The Request Logs Solve the problem of the {@link MixmicroLog} object of the child parent thread
   *
   * @see TransmittableThreadLocal
   * @see MixmicroLog
   */
  private static final TransmittableThreadLocal<MixmicroLog> LOGS =
      new TransmittableThreadLocal<>();

  /**
   * Get This Thread Mixmicro Boot Log Object Instance
   *
   * @return This Thread Mixmicro Boot Log
   */
  public static MixmicroLog get() {
    return LOGS.get();
  }

  /**
   * Set This Thread Mixmicro Boot Log Object Instance
   *
   * @param mixmicroLog This Thread Mixmicro Boot Log
   */
  public static void set(MixmicroLog mixmicroLog) {
    LOGS.set(mixmicroLog);
  }

  /** Remove This Thread Mixmicro Boot Log Object Instance */
  public static void remove() {
    LOGS.remove();
  }

  /**
   * set custom traceId
   *
   * @param traceId custom traceId
   * @return true:success,false: failed
   */
  public static boolean setTraceId(String traceId) {
    Assert.notNull(traceId, "traceId cannot be null");
    MixmicroLog mixmicroLog = get();
    if (null != mixmicroLog) {
      mixmicroLog.setTraceId(traceId);
      return true;
    }
    return false;
  }
}
