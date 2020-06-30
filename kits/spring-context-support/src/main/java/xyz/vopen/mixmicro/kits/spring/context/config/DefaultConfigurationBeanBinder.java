/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
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
package xyz.vopen.mixmicro.kits.spring.context.config;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Map;

/**
 * The default {@link ConfigurationBeanBinder} implementation
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see ConfigurationBeanBinder
 * @since 1.0.5.RC4
 */
public class DefaultConfigurationBeanBinder implements ConfigurationBeanBinder {

  @Override
  public void bind(
      Map<String, Object> configurationProperties,
      boolean ignoreUnknownFields,
      boolean ignoreInvalidFields,
      Object configurationBean) {
    DataBinder dataBinder = new DataBinder(configurationBean);
    // Set ignored*
    dataBinder.setIgnoreInvalidFields(ignoreUnknownFields);
    dataBinder.setIgnoreUnknownFields(ignoreInvalidFields);
    // Get properties under specified prefix from PropertySources
    // Convert Map to MutablePropertyValues
    MutablePropertyValues propertyValues = new MutablePropertyValues(configurationProperties);
    // Bind
    dataBinder.bind(propertyValues);
  }
}
