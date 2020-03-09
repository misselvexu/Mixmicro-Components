package xyz.vopen.mixmicro.components.boot.security.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import xyz.vopen.mixmicro.components.boot.security.InterceptionMode;
import xyz.vopen.mixmicro.components.boot.security.wrapper.EncryptablePropertySourceWrapper;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config.StringPBEConfig;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration class that registers a {@link BeanFactoryPostProcessor} that wraps all {@link
 * PropertySource} defined in the {@link Environment} with {@link EncryptablePropertySourceWrapper}
 * and defines a default {@link StringEncryptor} for decrypting properties that can be configured
 * through the same properties it wraps.
 *
 * <p>The {@link StringEncryptor} bean is only defined when no other bean of type {@link
 * StringEncryptor} is present in the Application Context, thus allowing for custom definition if
 * required.
 *
 * <p>The default {@link StringEncryptor} can be configured through the following properties:
 *
 * <table border="1">
 *     <tr>
 *         <td>Key</td><td>Required</td><td>Default Value</td>
 *     </tr>
 *     <tr>
 *         <td>mixmicro.security.encryptor.password</td><td><b>True</b></td><td> - </td>
 *     </tr>
 *     <tr>
 *         <td>mixmicro.security.encryptor.algorithm</td><td>False</td><td>PBEWITHHMACSHA512ANDAES_256</td>
 *     </tr>
 *     <tr>
 *         <td>mixmicro.security.encryptor.keyObtentionIterations</td><td>False</td><td>1000</td>
 *     </tr>
 *     <tr>
 *         <td>mixmicro.security.encryptor.poolSize</td><td>False</td><td>1</td>
 *     </tr><tr>
 *         <td>mixmicro.security.encryptor.providerName</td><td>False</td><td>SunJCE</td>
 *     </tr>
 *     <tr>
 *         <td>mixmicro.security.encryptor.saltGeneratorClassname</td><td>False</td><td>RandomSaltGenerator</td>
 *     </tr>
 *     <tr>
 *         <td>mixmicro.security.encryptor.ivGeneratorClassname</td><td>False</td><td>RandomIvGenerator</td>
 *     </tr>
 *     <tr>
 *         <td>mixmicro.security.encryptor.stringOutputType</td><td>False</td><td>base64</td>
 *     </tr>
 * </table>
 *
 * <p>For mor information about the core properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see StringPBEConfig
 */
@Configuration
@Import({EncryptablePropertyResolverConfiguration.class, CachingConfiguration.class})
@Slf4j
public class EnableEncryptablePropertiesConfiguration {

  @SuppressWarnings("unchecked")
  @Bean
  public static EnableEncryptablePropertiesBeanFactoryPostProcessor
      enableEncryptablePropertySourcesPostProcessor(final ConfigurableEnvironment environment) {
    final boolean proxyPropertySources =
        environment.getProperty(
            "mixmicro.security.encryptor.proxy-property-sources", Boolean.TYPE, false);
    final List<String> skipPropertySources =
        (List<String>)
            environment.getProperty(
                "mixmicro.security.encryptor.skip-property-sources",
                List.class,
                Collections.EMPTY_LIST);
    final List<Class<PropertySource<?>>> skipPropertySourceClasses =
        skipPropertySources.stream()
            .map(EnableEncryptablePropertiesConfiguration::getPropertiesClass)
            .collect(Collectors.toList());
    final InterceptionMode interceptionMode =
        proxyPropertySources ? InterceptionMode.PROXY : InterceptionMode.WRAPPER;
    return new EnableEncryptablePropertiesBeanFactoryPostProcessor(
        environment, interceptionMode, skipPropertySourceClasses);
  }

  @SuppressWarnings("unchecked")
  private static Class<PropertySource<?>> getPropertiesClass(String className) {
    try {
      Class<?> clazz = Class.forName(className);
      if (PropertySource.class.isAssignableFrom(clazz)) {
        return (Class<PropertySource<?>>) clazz;
      }
      throw new IllegalArgumentException(
          String.format(
              "Invalid mixmicro.security.encryptor.skip-property-sources: Class %s does not implement %s",
              className, PropertySource.class.getName()));
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          String.format(
              "Invalid mixmicro.security.encryptor.skip-property-sources: Class %s not found",
              className),
          e);
    }
  }
}
