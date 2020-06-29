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

import xyz.vopen.mixmicro.kits.spring.beans.factory.annotation.ConfigurationBeanBindingPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

/**
 * The customizer for the configuration bean after {@link ConfigurationBeanBinder#bind its binding}.
 *
 * <p>If There are multiple {@link ConfigurationBeanCustomizer} beans in the Spring {@link
 * ApplicationContext context}, they are executed orderly, thus the subclass should be aware to
 * implement the {@link #getOrder()} method.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see ConfigurationBeanBinder
 * @see ConfigurationBeanBindingPostProcessor
 * @since 1.0.6.BUILD-SNAPSHOT
 */
public interface ConfigurationBeanCustomizer extends Ordered {

  /**
   * Customize the configuration bean
   *
   * @param beanName the name of the configuration bean
   * @param configurationBean the configuration bean
   */
  void customize(String beanName, Object configurationBean);
}
