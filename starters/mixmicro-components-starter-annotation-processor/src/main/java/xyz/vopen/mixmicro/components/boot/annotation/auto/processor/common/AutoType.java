package xyz.vopen.mixmicro.components.boot.annotation.auto.processor.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.vopen.mixmicro.components.boot.annotation.auto.processor.annotation.*;

/**
 * 注解类型
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Getter
@RequiredArgsConstructor
public enum AutoType {

  /** Component，组合注解，添加到 spring.factories */
  COMPONENT(
      "org.springframework.stereotype.Component",
      "org.springframework.boot.autoconfigure.EnableAutoConfiguration"),

  /** ApplicationContextInitializer 添加到 spring.factories */
  CONTEXT_INITIALIZER(
      MixmicroAutoContextInitializer.class.getName(),
      "org.springframework.context.ApplicationContextInitializer"),

  /** ApplicationListener 添加到 spring.factories */
  LISTENER(MixmicroAutoListener.class.getName(), "org.springframework.context.ApplicationListener"),

  /** SpringApplicationRunListener 添加到 spring.factories */
  RUN_LISTENER(
      MixmicroAutoRunListener.class.getName(), "org.springframework.boot.SpringApplicationRunListener"),

  /** EnvironmentPostProcessor 添加到 spring.factories */
  ENV_POST_PROCESSOR(
      MixmicroAutoEnvPostProcessor.class.getName(),
      "org.springframework.boot.env.EnvironmentPostProcessor"),

  /** FailureAnalyzer 添加到 spring.factories */
  FAILURE_ANALYZER(
      MixmicroAutoFailureAnalyzer.class.getName(), "org.springframework.boot.diagnostics.FailureAnalyzer"),

  /** AutoConfigurationImportFilter spring.factories */
  AUTO_CONFIGURATION_IMPORT_FILTER(
      MixmicroAutoConfigImportFilter.class.getName(),
      "org.springframework.boot.autoconfigure.AutoConfigurationImportFilter"),

  /** TemplateAvailabilityProvider 添加到 spring.factories */
  TEMPLATE_AVAILABILITY_PROVIDER(
      MixmicroAutoTemplateProvider.class.getName(),
      "org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider");

  // properties & fields.

  private final String annotation;
  private final String configureKey;
}
