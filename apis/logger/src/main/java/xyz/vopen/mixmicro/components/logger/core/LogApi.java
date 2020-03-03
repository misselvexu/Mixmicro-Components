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
package xyz.vopen.mixmicro.components.logger.core;

import xyz.vopen.mixmicro.components.logger.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * This class used to output core statements from within the acmedcare-common-tools package.
 *
 * <p>And also it is used to output core statements to console appender.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.15
 */
public class LogApi {
  private static final String DEBUG_PREFIX = "Mixmicro-Log:DEBUG  ";
  private static final String INFO_PREFIX = "Mixmicro-Log:INFO ";
  private static final String ERR_PREFIX = "Mixmicro-Log:ERROR ";
  private static final String WARN_PREFIX = "Mixmicro-Log:WARN ";

  private static final Map<String, Level> LEVELS = new HashMap<String, Level>();

  private static volatile Level consoleLogLevel = Level.WARN;

  static {
    LEVELS.put("DEBUG", Level.DEBUG);
    LEVELS.put("INFO", Level.INFO);
    LEVELS.put("WARN", Level.WARN);
    LEVELS.put("ERROR", Level.ERROR);
  }

  public static void setConsoleLevel(String level) {
    Level value = LEVELS.get(level.toUpperCase());
    if (!StringUtil.isBlank(level) && value != null) {
      consoleLogLevel = value;
    }
  }

  public static void debug(String msg) {
    if (isDebug()) {
      System.out.println(DEBUG_PREFIX + msg);
    }
  }

  public static void info(String msg) {
    if (isInfo()) {
      System.out.println(INFO_PREFIX + msg);
    }
  }

  public static void warn(String msg) {
    if (isWarn()) {
      System.err.println(WARN_PREFIX + msg);
    }
  }

  public static void error(String msg, Throwable throwable) {
    if (isError()) {
      System.err.println(ERR_PREFIX + msg);
      if (throwable != null) {
        throwable.printStackTrace();
      }
    }
  }

  private static boolean isDebug() {
    setConsoleLevel(System.getProperty(Constants.MIXMICRO_LOG_INTERNAL_LEVEL, "WARN"));
    return consoleLogLevel.equals(Level.DEBUG);
  }

  private static boolean isInfo() {
    return isDebug() || consoleLogLevel.equals(Level.INFO);
  }

  private static boolean isWarn() {
    return isInfo() || consoleLogLevel.equals(Level.WARN);
  }

  private static boolean isError() {
    return isWarn() || consoleLogLevel.equals(Level.ERROR);
  }

  enum Level {
    DEBUG,
    INFO,
    WARN,
    ERROR
  }
}
