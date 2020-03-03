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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Class to hold configuration keys.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> on 16/10/26.
 */
public interface Constants {
  // default logger, use ROOT logger if not configured in app.
  Logger DEFAULT_LOG = LoggerFactory.getLogger("xyz.vopen.mixmicro.components.logger");
  String LOG_START = "*";
  String LOG_DIRECTORY = "core";
  String LOG_XML_CONFIG_FILE_NAME = "core-conf.xml";
  String LOG_XML_CONFIG_FILE_ENV_PATTERN = "core-conf-%s.xml";
  String LOG_CONFIG_PROPERTIES = "config.properties";

  String LOG_PATH = "logging.path";
  String LOG_PATH_PREFIX = "logging.path.";

  String OLD_LOG_PATH = "loggingRoot";
  String LOG_LEVEL = "logging.level";
  String LOG_LEVEL_PREFIX = "logging.level.";
  String LOG_CONFIG_PREFIX = "logging.config.";
  String DEFAULT_MIDDLEWARE_SPACE_LOG_LEVEL = "INFO";
  String IS_DEFAULT_LOG_PATH = "isDefaultLogPath";
  String IS_DEFAULT_LOG_LEVEL = "isDefaultLogLevel";
  // specify core conf file according different environment, such as core-conf-dev.xml
  // value pattern is similar to core.env.suffix=spaceName:dev&spaceName:test
  String LOG_ENV_SUFFIX = "core.env.suffix";
  // file encoding configured by VM option
  String LOG_ENCODING_PROP_KEY = "file.encoding";
  // disable space core
  String MIXMICRO_LOG_DISABLE_PROP_KEY = "mixmicro.core.disable";
  // disable log4j bridge to commons logging
  String LOG4J_COMMONS_LOGGING_MIDDLEWARE_LOG_DISABLE_PROP_KEY =
      "log4j.commons.logging.mixmicro.core.disable";
  // disable log4j space factory
  String LOG4J_MIDDLEWARE_LOG_DISABLE_PROP_KEY = "log4j.mixmicro.core.disable";
  // disable log4j2 space factory
  String LOG4J2_MIDDLEWARE_LOG_DISABLE_PROP_KEY = "log4j2.mixmicro.core.disable";
  // disable logback space factory
  String LOGBACK_MIDDLEWARE_LOG_DISABLE_PROP_KEY = "logback.mixmicro.core.disable";
  // UTF-8 encoding
  String UTF8_STR = "UTF-8";
  // default core path
  String LOGGING_PATH_DEFAULT = System.getProperty("user.home") + File.separator + "logs";
  // default priority, Larger numbers indicate higher priority
  int DEFAULT_PRIORITY = 0;

  // PID key
  String PROCESS_MARKER = "PID";
  // config prefix
  String MIXMICRO_CONFIG_PREFIX = "mixmicro.core.";
  // global switch to core on console
  String MIXMICRO_ALL_LOG_CONSOLE_SWITCH = "mixmicro.core.console";
  // single space switch to core on console
  String MIXMICRO_SINGLE_LOG_CONSOLE_SWITCH = "mixmicro.core.%s.console";
  // console string
  String CONSOLE_SUFFIX = ".console";
  // acmedcare-common-tools 自身日志开关
  // internal core level config.
  String MIXMICRO_LOG_INTERNAL_LEVEL = "mixmicro.core.internal.level";
  // global console core level
  String MIXMICRO_ALL_LOG_CONSOLE_LEVEL = "mixmicro.core.console.level";
  // single space console core level
  String MIXMICRO_SINGLE_LOG_CONSOLE_LEVEL = "mixmicro.core.%s.console.level";
  // logback core pattern on console
  String MIXMICRO_LOG_CONSOLE_LOGBACK_PATTERN =
      "mixmicro.core.console.logback.pattern";
  // log4j2 core pattern on console
  String MIXMICRO_LOG_CONSOLE_LOG4J2_PATTERN = "mixmicro.core.console.log4j2.pattern";
  // default logback core pattern
  String MIXMICRO_LOG_CONSOLE_LOGBACK_PATTERN_DEFAULT =
      "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p ${PID:- } --- [%15.15t] %-40.40logger{39} : %m%n";
  // default log4j2 core pattern
  String MIXMICRO_LOG_CONSOLE_LOG4J2_PATTERN_DEFAULT =
      "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p %X{PID} --- [%15.15t] %-40.40logger{39} : %m%n";
  // logging path file
  String LOGGING_CONFIG_PATH = "logging.config.%s";
  // config marker
  String MIXMICRO_LOG_FIRST_INITIALIZE = "mixmicro.core.first.initialize";
}
