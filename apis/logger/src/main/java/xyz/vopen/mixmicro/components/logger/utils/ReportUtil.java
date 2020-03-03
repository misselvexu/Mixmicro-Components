/*
 * Licensed to the Acmedcare+ Group under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.vopen.mixmicro.components.logger.utils;

import xyz.vopen.mixmicro.components.logger.core.LogApi;

/**
 * ReportUtil
 *
 * <p>参照:slf4j 输出信息到控制台及默认（业务app中配置的）日志
 *
 * <p>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> on 16/9/24.
 */
public class ReportUtil {
  /**
   * print Debug message
   *
   * @param msg
   */
  public static void reportDebug(String msg) {
    LogApi.debug(msg);
  }

  /**
   * keep compatible
   *
   * @param msg
   */
  @Deprecated
  public static void report(String msg) {
    reportDebug(msg);
  }

  /**
   * print Info message
   *
   * @param msg
   */
  public static void reportInfo(String msg) {
    LogApi.info(msg);
  }

  /**
   * print Warn message
   *
   * @param msg
   */
  public static void reportWarn(String msg) {
    LogApi.warn(msg);
  }

  public static void reportError(String msg, Throwable throwable) {
    LogApi.error(msg, throwable);
  }
}
