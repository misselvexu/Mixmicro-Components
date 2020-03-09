package xyz.vopen.mixmicro.components.boot.security.encryptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import xyz.vopen.mixmicro.components.boot.security.properties.MixmicroSecurityEncryptorConfigurationProperties;
import xyz.vopen.mixmicro.components.boot.security.util.AsymmetricCryptography;
import xyz.vopen.mixmicro.components.boot.security.util.Singleton;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.PooledPBEStringEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config.SimpleStringPBEConfig;

import java.util.Optional;
import java.util.function.Supplier;

import static xyz.vopen.mixmicro.components.boot.security.util.Functional.tap;

/**
 * Default Lazy Encryptor that delegates to a custom {@link StringEncryptor} bean or creates a
 * default {@link PooledPBEStringEncryptor} or {@link SimpleAsymmetricStringEncryptor} based on what
 * properties are provided
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Slf4j
public class DefaultLazyEncryptor implements StringEncryptor {

  private final Singleton<StringEncryptor> singleton;

  public DefaultLazyEncryptor(
      final ConfigurableEnvironment e,
      final String customEncryptorBeanName,
      boolean isCustom,
      final BeanFactory bf) {
    singleton =
        new Singleton<>(
            () ->
                Optional.of(customEncryptorBeanName)
                    .filter(bf::containsBean)
                    .map(name -> (StringEncryptor) bf.getBean(name))
                    .map(
                        tap(
                            bean ->
                                log.info(
                                    "Found Custom Encryptor Bean {} with name: {}",
                                    bean,
                                    customEncryptorBeanName)))
                    .orElseGet(
                        () -> {
                          if (isCustom) {
                            throw new IllegalStateException(
                                String.format(
                                    "String Encryptor custom Bean not found with name '%s'",
                                    customEncryptorBeanName));
                          }
                          log.info(
                              "String Encryptor custom Bean not found with name '{}'. Initializing Default String Encryptor",
                              customEncryptorBeanName);
                          return createDefault(e);
                        }));
  }

  public DefaultLazyEncryptor(final ConfigurableEnvironment e) {
    singleton = new Singleton<>(() -> createDefault(e));
  }

  private StringEncryptor createDefault(ConfigurableEnvironment e) {
    MixmicroSecurityEncryptorConfigurationProperties configProps =
        MixmicroSecurityEncryptorConfigurationProperties.bindConfigProps(e);
    return Optional.of(configProps)
        .filter(DefaultLazyEncryptor::isPBEConfig)
        .map(this::createPBEDefault)
        .orElseGet(
            () ->
                Optional.of(configProps)
                    .filter(DefaultLazyEncryptor::isAsymmetricConfig)
                    .map(this::createAsymmetricDefault)
                    .orElseThrow(
                        () ->
                            new IllegalStateException(
                                "either 'mixmicro.security.encryptor.password' or one of ['mixmicro.security.encryptor.private-key-string', 'mixmicro.security.encryptor.private-key-location'] must be provided for Password-based or Asymmetric encryption")));
  }

  private StringEncryptor createAsymmetricDefault(
      MixmicroSecurityEncryptorConfigurationProperties configProps) {
    SimpleAsymmetricConfig config = new SimpleAsymmetricConfig();
    config.setPrivateKey(
        get(
            configProps::getPrivateKeyString,
            "mixmicro.security.encryptor.private-key-string",
            null));
    config.setPrivateKeyLocation(
        get(
            configProps::getPrivateKeyLocation,
            "mixmicro.security.encryptor.private-key-location",
            null));
    config.setPrivateKeyFormat(
        get(
            configProps::getPrivateKeyFormat,
            "mixmicro.security.encryptor.private-key-format",
            AsymmetricCryptography.KeyFormat.DER));
    return new SimpleAsymmetricStringEncryptor(config);
  }

  private StringEncryptor createPBEDefault(
      MixmicroSecurityEncryptorConfigurationProperties configProps) {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(
        getRequired(configProps::getPassword, "mixmicro.security.encryptor.password"));
    config.setAlgorithm(
        get(
            configProps::getAlgorithm,
            "mixmicro.security.encryptor.algorithm",
            "PBEWITHHMACSHA512ANDAES_256"));
    config.setKeyObtentionIterations(
        get(
            configProps::getKeyObtentionIterations,
            "mixmicro.security.encryptor.key-obtention-iterations",
            "1000"));
    config.setPoolSize(get(configProps::getPoolSize, "mixmicro.security.encryptor.pool-size", "1"));
    config.setProviderName(
        get(configProps::getProviderName, "mixmicro.security.encryptor.provider-name", null));
    config.setProviderClassName(
        get(
            configProps::getProviderClassName,
            "mixmicro.security.encryptor.provider-class-name",
            null));
    config.setSaltGeneratorClassName(
        get(
            configProps::getSaltGeneratorClassname,
            "mixmicro.security.encryptor.salt-generator-classname",
            "xyz.vopen.mixmicro.components.enhance.security.salt.RandomSaltGenerator"));
    config.setIvGeneratorClassName(
        get(
            configProps::getIvGeneratorClassname,
            "mixmicro.security.encryptor.iv-generator-classname",
            "xyz.vopen.mixmicro.components.enhance.security.iv.RandomIvGenerator"));
    config.setStringOutputType(
        get(
            configProps::getStringOutputType,
            "mixmicro.security.encryptor.string-output-type",
            "base64"));
    encryptor.setConfig(config);
    return encryptor;
  }

  private static boolean isAsymmetricConfig(
      MixmicroSecurityEncryptorConfigurationProperties config) {
    return config.getPrivateKeyString() != null || config.getPrivateKeyLocation() != null;
  }

  private static boolean isPBEConfig(MixmicroSecurityEncryptorConfigurationProperties config) {
    return config.getPassword() != null;
  }

  private static <T> T getRequired(Supplier<T> supplier, String key) {
    T value = supplier.get();
    if (value == null) {
      throw new IllegalStateException(
          String.format("Required Encryption core property missing: %s", key));
    }
    return value;
  }

  private static <T> T get(Supplier<T> supplier, String key, T defaultValue) {
    T value = supplier.get();
    if (value == defaultValue) {
      log.info("Encryptor config not found for property {}, using default value: {}", key, value);
    }
    return value;
  }

  @Override
  public String encrypt(final String message) {
    return singleton.get().encrypt(message);
  }

  @Override
  public String decrypt(final String encryptedMessage) {
    return singleton.get().decrypt(encryptedMessage);
  }
}
