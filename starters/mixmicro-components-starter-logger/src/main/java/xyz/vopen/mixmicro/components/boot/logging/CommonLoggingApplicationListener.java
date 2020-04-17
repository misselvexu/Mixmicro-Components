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
package xyz.vopen.mixmicro.components.boot.logging;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.logger.core.*;
import xyz.vopen.mixmicro.components.logger.core.env.LogEnvUtils;
import xyz.vopen.mixmicro.components.logger.core.factory.AbstractLoggerSpaceFactory;
import xyz.vopen.mixmicro.components.logger.core.factory.Log4j2LoggerSpaceFactory;
import xyz.vopen.mixmicro.components.logger.core.factory.LogbackLoggerSpaceFactory;
import xyz.vopen.mixmicro.components.logger.utils.ReportUtil;
import xyz.vopen.mixmicro.components.logger.utils.StringUtil;

import java.util.*;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.15
 */
public class CommonLoggingApplicationListener
    implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

  @Override
  public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {
    if (DefaultReInitializerChecker.isReInitialized.compareAndSet(false, true)) {
      reInitializeLog(loadApplicationEnvironment(event.getEnvironment()));
    }
  }

  public void setReInitialize(boolean value) {
    DefaultReInitializerChecker.isReInitialized.set(value);
  }

  public void reInitializeLog(Map<String, String> context) {
    for (String key : context.keySet()) {
      if (key.startsWith(Constants.MIXMICRO_CONFIG_PREFIX)
          && !key.equals(Constants.MIXMICRO_ALL_LOG_CONSOLE_SWITCH)
          && key.endsWith(Constants.CONSOLE_SUFFIX)) {
        int index = Constants.MIXMICRO_CONFIG_PREFIX.length();
        // minus length of .console
        int end = key.length() - Constants.CONSOLE_SUFFIX.length();
        String spaceId = key.substring(index, end);
        LoggerSpaceManager.getLoggerBySpace(spaceId, spaceId);
      }
    }

    for (Map.Entry<SpaceId, SpaceInfo> entry : MultiAppLoggerSpaceManager.getSpacesMap().entrySet()) {
      SpaceId spaceId = entry.getKey();
      SpaceInfo spaceInfo = entry.getValue();
      ReportUtil.reportDebug("Re-initialize log of " + spaceId.getSpaceName());
      AbstractLoggerSpaceFactory abstractLoggerSpaceFactory = spaceInfo.getAbstractLoggerSpaceFactory();

      if (abstractLoggerSpaceFactory instanceof LogbackLoggerSpaceFactory) {
        ((LogbackLoggerSpaceFactory) abstractLoggerSpaceFactory).reInitialize(context);
      }
      if (abstractLoggerSpaceFactory instanceof Log4j2LoggerSpaceFactory) {
        ((Log4j2LoggerSpaceFactory) abstractLoggerSpaceFactory).reInitialize(context);
      }
    }
  }

  /**
   * load log configuration in application.properties
   *
   * @param environment
   * @return
   */
  private Map<String, String> loadApplicationEnvironment(ConfigurableEnvironment environment) {
    Map<String, String> context = new HashMap<String, String>();
    readLogConfiguration(Constants.LOG_PATH, environment.getProperty(Constants.LOG_PATH), context, Constants.LOGGING_PATH_DEFAULT);
    readLogConfiguration(Constants.OLD_LOG_PATH, environment.getProperty(Constants.OLD_LOG_PATH), context, context.get(Constants.LOG_PATH));
    readLogConfiguration(Constants.LOG_ENCODING_PROP_KEY, environment.getProperty(Constants.LOG_ENCODING_PROP_KEY), context);
    LogEnvUtils.keepCompatible(context, true);

    Set<String> configKeys = new HashSet<String>();
    Iterator<PropertySource<?>> propertySourceIterator =
        environment.getPropertySources().iterator();
    while (propertySourceIterator.hasNext()) {
      PropertySource<?> propertySource = propertySourceIterator.next();
      if (propertySource instanceof EnumerablePropertySource) {
        configKeys.addAll(Arrays.asList(((EnumerablePropertySource<?>) propertySource).getPropertyNames()));
      }
    }
    for (String key : configKeys) {
      if (LogEnvUtils.filterAllLogConfig(key)) {
        addToGlobalSystemProperties(key, environment.getProperty(key));
        readLogConfiguration(key, environment.getProperty(key), context);
      }
    }
    return context;
  }

  private void addToGlobalSystemProperties(String key, String value) {
    if (!StringUtil.isBlank(key) && !StringUtil.isBlank(value)) {
      LogEnvUtils.processGlobalSystemLogProperties().put(key, value);
    }
  }

  private void readLogConfiguration(String key, String value, Map<String, String> context) {
    if (!StringUtil.isBlank(value)) {
      context.put(key, value);
    }
  }

  private void readLogConfiguration(
      String key, String value, Map<String, String> context, String defaultValue) {
    if (!StringUtil.isBlank(value)) {
      context.put(key, value);
    } else {
      context.put(key, defaultValue);
    }
  }

  @Override
  public int getOrder() {
    return PriorityOrdered.HIGHEST_PRECEDENCE + 20;
  }
}
