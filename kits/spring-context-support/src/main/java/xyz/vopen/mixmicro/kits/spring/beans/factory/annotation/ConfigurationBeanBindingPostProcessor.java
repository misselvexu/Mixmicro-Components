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
package xyz.vopen.mixmicro.kits.spring.beans.factory.annotation;

import xyz.vopen.mixmicro.kits.spring.context.config.ConfigurationBeanBinder;
import xyz.vopen.mixmicro.kits.spring.context.config.ConfigurationBeanCustomizer;
import xyz.vopen.mixmicro.kits.spring.context.config.DefaultConfigurationBeanBinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import java.util.*;

import static org.springframework.beans.factory.BeanFactoryUtils.beansOfTypeIncludingAncestors;
import static org.springframework.core.annotation.AnnotationAwareOrderComparator.sort;
import static org.springframework.util.ClassUtils.getUserClass;
import static org.springframework.util.ObjectUtils.nullSafeEquals;

/**
 * The {@link BeanPostProcessor} class to bind the configuration bean
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.5.BUILD-SNAPSHOT
 */
public class ConfigurationBeanBindingPostProcessor
    implements BeanFactoryPostProcessor, BeanPostProcessor {

  /** The bean name of {@link ConfigurationBeanBindingPostProcessor} */
  public static final String BEAN_NAME = "configurationBeanBindingPostProcessor";

  static final String CONFIGURATION_PROPERTIES_ATTRIBUTE_NAME = "configurationProperties";

  static final String IGNORE_UNKNOWN_FIELDS_ATTRIBUTE_NAME = "ignoreUnknownFields";

  static final String IGNORE_INVALID_FIELDS_ATTRIBUTE_NAME = "ignoreInvalidFields";

  private final Log log = LogFactory.getLog(getClass());

  private ConfigurableListableBeanFactory beanFactory;

  private ConfigurationBeanBinder configurationBeanBinder;

  private List<ConfigurationBeanCustomizer> configurationBeanCustomizers = Collections.emptyList();

  static void initBeanMetadataAttributes(
      AbstractBeanDefinition beanDefinition,
      Map<String, Object> configurationProperties,
      boolean ignoreUnknownFields,
      boolean ignoreInvalidFields) {
    beanDefinition.setAttribute(CONFIGURATION_PROPERTIES_ATTRIBUTE_NAME, configurationProperties);
    beanDefinition.setAttribute(IGNORE_UNKNOWN_FIELDS_ATTRIBUTE_NAME, ignoreUnknownFields);
    beanDefinition.setAttribute(IGNORE_INVALID_FIELDS_ATTRIBUTE_NAME, ignoreInvalidFields);
  }

  private static <T> T getAttribute(BeanDefinition beanDefinition, String attributeName) {
    return (T) beanDefinition.getAttribute(attributeName);
  }

  private static Map<String, Object> getConfigurationProperties(BeanDefinition beanDefinition) {
    return getAttribute(beanDefinition, CONFIGURATION_PROPERTIES_ATTRIBUTE_NAME);
  }

  private static boolean getIgnoreUnknownFields(BeanDefinition beanDefinition) {
    return getAttribute(beanDefinition, IGNORE_UNKNOWN_FIELDS_ATTRIBUTE_NAME);
  }

  private static boolean getIgnoreInvalidFields(BeanDefinition beanDefinition) {
    return getAttribute(beanDefinition, IGNORE_INVALID_FIELDS_ATTRIBUTE_NAME);
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {

    this.beanFactory = beanFactory;

    initConfigurationBeanBinder();

    initBindConfigurationBeanCustomizers();
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {

    BeanDefinition beanDefinition = getNullableBeanDefinition(beanName);

    if (isConfigurationBean(bean, beanDefinition)) {
      bindConfigurationBean(bean, beanDefinition);
      customize(beanName, bean);
    }

    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  public ConfigurationBeanBinder getConfigurationBeanBinder() {
    return configurationBeanBinder;
  }

  public void setConfigurationBeanBinder(ConfigurationBeanBinder configurationBeanBinder) {
    this.configurationBeanBinder = configurationBeanBinder;
  }

  private BeanDefinition getNullableBeanDefinition(String beanName) {
    return beanFactory.containsBeanDefinition(beanName) ? beanFactory.getBeanDefinition(beanName) : null;
  }

  private boolean isConfigurationBean(Object bean, BeanDefinition beanDefinition) {
    return beanDefinition != null
        && ConfigurationBeanBindingRegistrar.ENABLE_CONFIGURATION_BINDING_CLASS.equals(
            beanDefinition.getSource())
        && nullSafeEquals(getBeanClassName(bean), beanDefinition.getBeanClassName());
  }

  private String getBeanClassName(Object bean) {
    return getUserClass(bean.getClass()).getName();
  }

  private void bindConfigurationBean(Object configurationBean, BeanDefinition beanDefinition) {

    Map<String, Object> configurationProperties = getConfigurationProperties(beanDefinition);

    boolean ignoreUnknownFields = getIgnoreUnknownFields(beanDefinition);

    boolean ignoreInvalidFields = getIgnoreInvalidFields(beanDefinition);

    configurationBeanBinder.bind(
        configurationProperties, ignoreUnknownFields, ignoreInvalidFields, configurationBean);

    if (log.isInfoEnabled()) {
      log.info("The configuration bean [" + configurationBean + "] have been binding by the " + "configuration properties [" + configurationProperties + "]");
    }
  }

  private void initConfigurationBeanBinder() {

    if (configurationBeanBinder == null) {
      try {
        configurationBeanBinder = beanFactory.getBean(ConfigurationBeanBinder.class);
      } catch (BeansException ignored) {
        if (log.isDebugEnabled()) {
          log.debug("configurationBeanBinder Bean can't be found in ApplicationContext.");
        }
        // Use Default implementation
        configurationBeanBinder = defaultConfigurationBeanBinder();
      }
    }
  }

  private void initBindConfigurationBeanCustomizers() {

    Collection<ConfigurationBeanCustomizer> customizers =
        beansOfTypeIncludingAncestors(beanFactory, ConfigurationBeanCustomizer.class).values();

    this.configurationBeanCustomizers = new ArrayList<ConfigurationBeanCustomizer>(customizers);

    sort(this.configurationBeanCustomizers);
  }

  private void customize(String beanName, Object configurationBean) {

    for (ConfigurationBeanCustomizer customizer : configurationBeanCustomizers) {
      customizer.customize(beanName, configurationBean);
    }
  }

  /**
   * Create {@link ConfigurationBeanBinder} instance.
   *
   * @return {@link DefaultConfigurationBeanBinder}
   */
  private ConfigurationBeanBinder defaultConfigurationBeanBinder() {
    return new DefaultConfigurationBeanBinder();
  }
}
