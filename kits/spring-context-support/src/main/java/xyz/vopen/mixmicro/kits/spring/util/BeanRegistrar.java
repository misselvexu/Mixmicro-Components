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
package xyz.vopen.mixmicro.kits.spring.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.AliasRegistry;

import static org.springframework.util.ObjectUtils.containsElement;
import static org.springframework.util.StringUtils.hasText;

/**
 * Bean Registrar
 *
 * @since 1.0.4.BUILD-SNAPSHOT
 */
public abstract class BeanRegistrar {

  private static final Log log = LogFactory.getLog(BeanRegistrar.class);

  /**
   * Register Infrastructure Bean
   *
   * @param beanDefinitionRegistry {@link BeanDefinitionRegistry}
   * @param beanType the type of bean
   * @param beanName the name of bean
   * @return if it's a first time to register, return <code>true</code>, or <code>false</code>
   */
  public static boolean registerInfrastructureBean(
      BeanDefinitionRegistry beanDefinitionRegistry, String beanName, Class<?> beanType) {

    boolean registered = false;

    if (!beanDefinitionRegistry.containsBeanDefinition(beanName)) {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
      beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
      beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
      registered = true;

      if (log.isInfoEnabled()) {
        log.info("The Infrastructure bean definition [" + beanDefinition + "with name [" + beanName + "] has been registered.");
      }
    }

    return registered;
  }

  /**
   * Detect the alias is present or not in the given bean name from {@link AliasRegistry}
   *
   * @param registry {@link AliasRegistry}
   * @param beanName the bean name
   * @param alias alias to test
   * @return if present, return <code>true</code>, or <code>false</code>
   */
  public static boolean hasAlias(AliasRegistry registry, String beanName, String alias) {
    return hasText(beanName) && hasText(alias) && containsElement(registry.getAliases(beanName), alias);
  }
}
