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
package xyz.vopen.mixmicro.components.boot.logging.log4j2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

/**
 * https://logging.apache.org/log4j/log4j-2.6.1/manual/plugins.html Processed by {@literal
 * org.apache.logging.log4j.core.config.plugins.processor.PluginProcessor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.15
 */
@Plugin(name = "ConfigurationFactory", category = org.apache.logging.log4j.core.config.ConfigurationFactory.CATEGORY)
@Order(Integer.MAX_VALUE)
public class ConfigurationFactory extends org.apache.logging.log4j.core.config.ConfigurationFactory {

  private final String[] TYPES = {"log4j2/log-conf.xml", "log4j2/log-conf-custom.xml"};

  @Override
  protected String[] getSupportedTypes() {
    return TYPES;
  }

  @Override
  public org.apache.logging.log4j.core.config.Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
    if (source != null && source != ConfigurationSource.NULL_SOURCE) {
      return loggerContext.getExternalContext() != null
          ? new Configuration()
          : new XmlConfiguration(loggerContext, source);
    }
    return null;
  }

  public static final class Configuration extends DefaultConfiguration {
    private Configuration() {
      this.isShutdownHookEnabled = false;
      String levelName = System.getProperty(DefaultConfiguration.DEFAULT_LEVEL, Level.INFO.name());
      Level level = Level.valueOf(levelName);
      getRootLogger().setLevel(level != null ? level : Level.INFO);
    }
  }
}
