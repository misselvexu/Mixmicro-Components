package xyz.vopen.mixmicro.components.boot.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.env.*;
import xyz.vopen.mixmicro.components.boot.security.aop.EncryptableMutablePropertySourcesInterceptor;
import xyz.vopen.mixmicro.components.boot.security.aop.EncryptablePropertySourceMethodInterceptor;
import xyz.vopen.mixmicro.components.boot.security.core.EnvCopy;
import xyz.vopen.mixmicro.components.boot.security.wrapper.EncryptableEnumerablePropertySourceWrapper;
import xyz.vopen.mixmicro.components.boot.security.wrapper.EncryptableMapPropertySourceWrapper;
import xyz.vopen.mixmicro.components.boot.security.wrapper.EncryptablePropertySourceWrapper;
import xyz.vopen.mixmicro.components.boot.security.wrapper.EncryptableSystemEnvironmentPropertySourceWrapper;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
@Slf4j
public class EncryptablePropertySourceConverter {

  public static void convertPropertySources(
      InterceptionMode interceptionMode,
      List<Class<PropertySource<?>>> skipPropertySourceClasses,
      EncryptablePropertyResolver propertyResolver,
      EncryptablePropertyFilter propertyFilter,
      MutablePropertySources propSources) {
    StreamSupport.stream(propSources.spliterator(), false)
        .filter(ps -> !(ps instanceof EncryptablePropertySource))
        .map(
            ps ->
                makeEncryptable(
                    interceptionMode,
                    skipPropertySourceClasses,
                    propertyResolver,
                    propertyFilter,
                    ps))
        .collect(toList())
        .forEach(ps -> propSources.replace(ps.getName(), ps));
  }

  @SuppressWarnings("unchecked")
  public static <T> PropertySource<T> makeEncryptable(
      InterceptionMode interceptionMode,
      List<Class<PropertySource<?>>> skipPropertySourceClasses,
      EncryptablePropertyResolver propertyResolver,
      EncryptablePropertyFilter propertyFilter,
      PropertySource<T> propertySource) {
    if (propertySource instanceof EncryptablePropertySource
        || skipPropertySourceClasses.stream()
            .anyMatch(skipClass -> skipClass.equals(propertySource.getClass()))) {
      log.info(
          "Skipping PropertySource {} [{}", propertySource.getName(), propertySource.getClass());
      return propertySource;
    }
    PropertySource<T> encryptablePropertySource =
        convertPropertySource(interceptionMode, propertyResolver, propertyFilter, propertySource);
    log.info(
        "Converting PropertySource {} [{}] to {}",
        propertySource.getName(),
        propertySource.getClass().getName(),
        AopUtils.isAopProxy(encryptablePropertySource)
            ? "AOP Proxy"
            : encryptablePropertySource.getClass().getSimpleName());
    return encryptablePropertySource;
  }

  public static MutablePropertySources proxyPropertySources(
      InterceptionMode interceptionMode,
      List<Class<PropertySource<?>>> skipPropertySourceClasses,
      EncryptablePropertyResolver propertyResolver,
      EncryptablePropertyFilter propertyFilter,
      MutablePropertySources propertySources,
      EnvCopy envCopy) {
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setTarget(MutablePropertySources.class);
    proxyFactory.setProxyTargetClass(true);
    proxyFactory.addInterface(PropertySources.class);
    proxyFactory.setTarget(propertySources);
    proxyFactory.addAdvice(
        new EncryptableMutablePropertySourcesInterceptor(
            interceptionMode,
            skipPropertySourceClasses,
            propertyResolver,
            propertyFilter,
            envCopy));
    return (MutablePropertySources) proxyFactory.getProxy();
  }

  private static <T> PropertySource<T> convertPropertySource(
      InterceptionMode interceptionMode,
      EncryptablePropertyResolver propertyResolver,
      EncryptablePropertyFilter propertyFilter,
      PropertySource<T> propertySource) {
    return interceptionMode == InterceptionMode.PROXY
        ? proxyPropertySource(propertySource, propertyResolver, propertyFilter)
        : instantiatePropertySource(propertySource, propertyResolver, propertyFilter);
  }

  @SuppressWarnings("unchecked")
  private static <T> PropertySource<T> proxyPropertySource(
      PropertySource<T> propertySource,
      EncryptablePropertyResolver resolver,
      EncryptablePropertyFilter propertyFilter) {
    // Silly Chris Beams for making CommandLinePropertySource getProperty and containsProperty
    // methods final. Those methods
    // can't be proxied with CGLib because of it. So fallback to wrapper for Command Line Arguments
    // only.
    if (CommandLinePropertySource.class.isAssignableFrom(propertySource.getClass())
        // Other PropertySource classes like
        // org.springframework.boot.env.OriginTrackedMapPropertySource
        // are final classes as well
        || Modifier.isFinal(propertySource.getClass().getModifiers())) {
      return instantiatePropertySource(propertySource, resolver, propertyFilter);
    }
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setTargetClass(propertySource.getClass());
    proxyFactory.setProxyTargetClass(true);
    proxyFactory.addInterface(EncryptablePropertySource.class);
    proxyFactory.setTarget(propertySource);
    proxyFactory.addAdvice(
        new EncryptablePropertySourceMethodInterceptor<>(propertySource, resolver, propertyFilter));
    return (PropertySource<T>) proxyFactory.getProxy();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static <T> PropertySource<T> instantiatePropertySource(
      PropertySource<T> propertySource,
      EncryptablePropertyResolver resolver,
      EncryptablePropertyFilter propertyFilter) {
    PropertySource<T> encryptablePropertySource;
    if (needsProxyAnyway(propertySource)) {
      encryptablePropertySource = proxyPropertySource(propertySource, resolver, propertyFilter);
    } else if (propertySource instanceof SystemEnvironmentPropertySource) {
      encryptablePropertySource =
          (PropertySource<T>)
              new EncryptableSystemEnvironmentPropertySourceWrapper(
                  (SystemEnvironmentPropertySource) propertySource, resolver, propertyFilter);
    } else if (propertySource instanceof MapPropertySource) {
      encryptablePropertySource =
          (PropertySource<T>)
              new EncryptableMapPropertySourceWrapper(
                  (MapPropertySource) propertySource, resolver, propertyFilter);
    } else if (propertySource instanceof EnumerablePropertySource) {
      encryptablePropertySource =
          new EncryptableEnumerablePropertySourceWrapper<>(
              (EnumerablePropertySource) propertySource, resolver, propertyFilter);
    } else {
      encryptablePropertySource =
          new EncryptablePropertySourceWrapper<>(propertySource, resolver, propertyFilter);
    }
    return encryptablePropertySource;
  }

  @SuppressWarnings("unchecked")
  private static boolean needsProxyAnyway(PropertySource<?> ps) {
    return needsProxyAnyway((Class<? extends PropertySource<?>>) ps.getClass());
  }

  private static boolean needsProxyAnyway(Class<? extends PropertySource<?>> psClass) {
    return needsProxyAnyway(psClass.getName());
  }

  /**
   * Some Spring Boot code actually casts property sources to this specific type so must be proxied.
   */
  private static boolean needsProxyAnyway(String className) {
    return Stream.of(
            "org.springframework.boot.context.config.ConfigFileApplicationListener$ConfigurationPropertySources",
            "org.springframework.boot.context.properties.source.ConfigurationPropertySourcesPropertySource")
        .anyMatch(className::equals);
  }
}
