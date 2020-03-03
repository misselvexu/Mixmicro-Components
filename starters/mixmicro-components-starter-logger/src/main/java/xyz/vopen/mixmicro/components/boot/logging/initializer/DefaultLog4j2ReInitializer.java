/*
 * Licensed to the VOPEN+ Group under one or more
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
package xyz.vopen.mixmicro.components.boot.logging.initializer;

import xyz.vopen.mixmicro.components.boot.logging.filter.DefaultLog4j2FilterGenerator;
import xyz.vopen.mixmicro.components.boot.logging.util.SystemPropertiesGetter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.Message;
import xyz.vopen.mixmicro.components.logger.core.Constants;
import xyz.vopen.mixmicro.components.logger.core.SpaceId;
import xyz.vopen.mixmicro.components.logger.core.spi.Log4j2ReInitializer;
import xyz.vopen.mixmicro.components.logger.utils.ResourceUtil;
import xyz.vopen.mixmicro.components.logger.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.15
 */
public class DefaultLog4j2ReInitializer implements Log4j2ReInitializer {

  private static final String FILE_PROTOCOL = "file";
  private static final String MIXMICRO_CONSOLE = "mixmicro-console";

  @Override
  public void reInitialize(
      final SpaceId spaceId,
      LoggerContext loggerContext,
      final Properties properties,
      URL confFile) {
    if (isAlreadyReInitialized(loggerContext)) {
      return;
    }
    loggerContext.removeFilter(DefaultLog4j2FilterGenerator.FILTER);
    initLogContext(properties);
    markAsReInitialized(loggerContext);
    if (isConsoleAppenderOpen(spaceId.getSpaceName(), properties)) {
      final ConsoleAppender consoleAppender = consoleAppender(properties);
      loggerContext.addFilter(
          new AbstractFilter() {
            @Override
            public Result filter(
                Logger logger, Level level, Marker marker, Message msg, Throwable t) {
              if (!logger.getAppenders().containsKey(MIXMICRO_CONSOLE)) {
                resetLog(logger);
              }
              return Result.NEUTRAL;
            }

            @Override
            public Result filter(
                Logger logger, Level level, Marker marker, Object msg, Throwable t) {
              if (!logger.getAppenders().containsKey(MIXMICRO_CONSOLE)) {
                resetLog(logger);
              }
              return Result.NEUTRAL;
            }

            @Override
            public Result filter(
                Logger logger, Level level, Marker marker, String msg, Object... params) {
              if (!logger.getAppenders().containsKey(MIXMICRO_CONSOLE)) {
                resetLog(logger);
              }
              return Result.NEUTRAL;
            }

            private void resetLog(Logger logger) {
              logger.getAppenders().clear();
              logger.addAppender(consoleAppender);
              logger.setLevel(getConsoleLevel(spaceId.getSpaceName(), properties));
              logger.setAdditive(false);
            }
          });
    } else {
      try {
        // remove first initialize
        loggerContext.setExternalContext(null);
        ConfigurationSource source = getConfigurationSource(confFile);
        Configuration config =
            ConfigurationFactory.getInstance().getConfiguration(loggerContext, source);
        for (Map.Entry entry : properties.entrySet()) {
          config.getProperties().put((String) entry.getKey(), (String) entry.getValue());
        }
        loggerContext.start(config);
      } catch (Throwable ex) {
        throw new IllegalStateException("log4j2 loggerSpaceFactory re-build error", ex);
      }
    }
  }

  private Level getConsoleLevel(String spaceId, Properties properties) {
    SystemPropertiesGetter propertiesGetter = new SystemPropertiesGetter(properties);
    String level = propertiesGetter.getProperty(Constants.MIXMICRO_ALL_LOG_CONSOLE_LEVEL);
    String defaultLevel = StringUtil.isBlank(level) ? "INFO" : level;
    level =
        propertiesGetter.getProperty(
            String.format(Constants.MIXMICRO_SINGLE_LOG_CONSOLE_LEVEL, spaceId), defaultLevel);
    return Level.toLevel(level, Level.INFO);
  }

  private ConsoleAppender consoleAppender(Properties properties) {
    SystemPropertiesGetter propertiesGetter = new SystemPropertiesGetter(properties);
    String logPattern =
        propertiesGetter.getProperty(
            Constants.MIXMICRO_LOG_CONSOLE_LOG4J2_PATTERN,
            Constants.MIXMICRO_LOG_CONSOLE_LOG4J2_PATTERN_DEFAULT);
    ConsoleAppender.Builder builder = ConsoleAppender.newBuilder();
    builder
        .withLayout(PatternLayout.newBuilder().withPattern(logPattern).build())
        .withName(MIXMICRO_CONSOLE);
    ConsoleAppender consoleAppender = builder.build();
    consoleAppender.start();
    return consoleAppender;
  }

  private ConfigurationSource getConfigurationSource(URL url) throws IOException {
    InputStream stream = url.openStream();
    if (FILE_PROTOCOL.equals(url.getProtocol())) {
      return new ConfigurationSource(stream, ResourceUtil.getFile(url));
    }
    return new ConfigurationSource(stream, url);
  }

  private boolean isAlreadyReInitialized(LoggerContext loggerContext) {
    if (loggerContext
            .getConfiguration()
            .getProperties()
            .get(DefaultLog4j2ReInitializer.class.getCanonicalName())
        != null) {
      return true;
    }
    return false;
  }

  private void markAsReInitialized(LoggerContext loggerContext) {
    loggerContext
        .getConfiguration()
        .getProperties()
        .put(DefaultLog4j2ReInitializer.class.getCanonicalName(), "");
  }

  private void initLogContext(Properties properties) {
    for (Map.Entry entry : properties.entrySet()) {
      ThreadContext.put((String) entry.getKey(), properties.getProperty((String) entry.getKey()));
    }
  }

  private boolean isConsoleAppenderOpen(String spaceId, Properties properties) {
    SystemPropertiesGetter propertiesGetter = new SystemPropertiesGetter(properties);
    String value =
        propertiesGetter.getProperty(
            String.format(Constants.MIXMICRO_SINGLE_LOG_CONSOLE_SWITCH, spaceId));
    if (StringUtil.isBlank(value)) {
      return "true"
          .equalsIgnoreCase(propertiesGetter.getProperty(Constants.MIXMICRO_ALL_LOG_CONSOLE_SWITCH));
    } else {
      return "true".equalsIgnoreCase(value);
    }
  }
}
