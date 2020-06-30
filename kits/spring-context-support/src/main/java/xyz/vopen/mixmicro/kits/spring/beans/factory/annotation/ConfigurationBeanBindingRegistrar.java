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

import xyz.vopen.mixmicro.kits.spring.util.BeanRegistrar;
import xyz.vopen.mixmicro.kits.spring.util.PropertySourcesUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.core.annotation.AnnotationAttributes.fromMap;

/**
 * The {@link ImportBeanDefinitionRegistrar} implementation for {@link
 * EnableConfigurationBeanBinding @EnableConfigurationBinding}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.5.RC2
 */
public class ConfigurationBeanBindingRegistrar
    implements ImportBeanDefinitionRegistrar, EnvironmentAware {

  static final Class ENABLE_CONFIGURATION_BINDING_CLASS = EnableConfigurationBeanBinding.class;

  private static final String ENABLE_CONFIGURATION_BINDING_CLASS_NAME = ENABLE_CONFIGURATION_BINDING_CLASS.getName();

  private final Log log = LogFactory.getLog(getClass());

  private ConfigurableEnvironment environment;

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

    AnnotationAttributes attributes =
        fromMap(metadata.getAnnotationAttributes(ENABLE_CONFIGURATION_BINDING_CLASS_NAME));

    registerConfigurationBeanDefinitions(attributes, registry);
  }

  protected void registerConfigurationBeanDefinitions(AnnotationAttributes attributes, BeanDefinitionRegistry registry) {

    String prefix = environment.resolvePlaceholders(attributes.getString("prefix"));

    Class<?> configClass = attributes.getClass("type");

    boolean multiple = attributes.getBoolean("multiple");

    boolean ignoreUnknownFields = attributes.getBoolean("ignoreUnknownFields");

    boolean ignoreInvalidFields = attributes.getBoolean("ignoreInvalidFields");

    registerConfigurationBeans(prefix, configClass, multiple, ignoreUnknownFields, ignoreInvalidFields, registry);
  }

  private void registerConfigurationBeans(
      String prefix,
      Class<?> configClass,
      boolean multiple,
      boolean ignoreUnknownFields,
      boolean ignoreInvalidFields,
      BeanDefinitionRegistry registry) {

    Map<String, Object> configurationProperties =
        PropertySourcesUtils.getSubProperties(
            environment.getPropertySources(), environment, prefix);

    if (CollectionUtils.isEmpty(configurationProperties)) {
      if (log.isDebugEnabled()) {
        log.debug("There is no property for binding to configuration class [" + configClass.getName() + "] within prefix [" + prefix + "]");
      }
      return;
    }

    Set<String> beanNames =
        multiple
            ? resolveMultipleBeanNames(configurationProperties)
            : singleton(resolveSingleBeanName(configurationProperties, configClass, registry));

    for (String beanName : beanNames) {
      registerConfigurationBean(
          beanName,
          configClass,
          multiple,
          ignoreUnknownFields,
          ignoreInvalidFields,
          configurationProperties,
          registry);
    }

    registerConfigurationBindingBeanPostProcessor(registry);
  }

  private void registerConfigurationBean(
      String beanName,
      Class<?> configClass,
      boolean multiple,
      boolean ignoreUnknownFields,
      boolean ignoreInvalidFields,
      Map<String, Object> configurationProperties,
      BeanDefinitionRegistry registry) {

    BeanDefinitionBuilder builder = rootBeanDefinition(configClass);

    AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

    setSource(beanDefinition);

    Map<String, Object> subProperties =
        resolveSubProperties(multiple, beanName, configurationProperties);

    ConfigurationBeanBindingPostProcessor.initBeanMetadataAttributes(
        beanDefinition, subProperties, ignoreUnknownFields, ignoreInvalidFields);

    registry.registerBeanDefinition(beanName, beanDefinition);

    if (log.isInfoEnabled()) {
      log.info("The configuration bean definition [name : " + beanName + ", content : " + beanDefinition + "] has been registered.");
    }
  }

  private Map<String, Object> resolveSubProperties(
      boolean multiple, String beanName, Map<String, Object> configurationProperties) {
    if (!multiple) {
      return configurationProperties;
    }

    MutablePropertySources propertySources = new MutablePropertySources();

    propertySources.addLast(new MapPropertySource("_", configurationProperties));

    return PropertySourcesUtils.getSubProperties(
        propertySources, environment, PropertySourcesUtils.normalizePrefix(beanName));
  }

  private void setSource(AbstractBeanDefinition beanDefinition) {
    beanDefinition.setSource(ENABLE_CONFIGURATION_BINDING_CLASS);
  }

  private void registerConfigurationBindingBeanPostProcessor(BeanDefinitionRegistry registry) {
    BeanRegistrar.registerInfrastructureBean(
        registry,
        ConfigurationBeanBindingPostProcessor.BEAN_NAME,
        ConfigurationBeanBindingPostProcessor.class);
  }

  @Override
  public void setEnvironment(Environment environment) {

    Assert.isInstanceOf(ConfigurableEnvironment.class, environment);

    this.environment = (ConfigurableEnvironment) environment;
  }

  private Set<String> resolveMultipleBeanNames(Map<String, Object> properties) {

    Set<String> beanNames = new LinkedHashSet<String>();

    for (String propertyName : properties.keySet()) {

      int index = propertyName.indexOf(".");

      if (index > 0) {

        String beanName = propertyName.substring(0, index);

        beanNames.add(beanName);
      }
    }

    return beanNames;
  }

  private String resolveSingleBeanName(
      Map<String, Object> properties, Class<?> configClass, BeanDefinitionRegistry registry) {

    String beanName = (String) properties.get("id");

    if (!StringUtils.hasText(beanName)) {
      BeanDefinitionBuilder builder = rootBeanDefinition(configClass);
      beanName = BeanDefinitionReaderUtils.generateBeanName(builder.getRawBeanDefinition(), registry);
    }

    return beanName;
  }
}
