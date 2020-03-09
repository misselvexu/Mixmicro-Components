package xyz.vopen.mixmicro.components.boot.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyDetector;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.util.AsymmetricCryptography.KeyFormat;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.PBEStringEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config.SimpleStringPBEConfig;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config.StringPBEConfig;
import xyz.vopen.mixmicro.components.enhance.security.iv.IvGenerator;
import xyz.vopen.mixmicro.components.enhance.security.salt.SaltGenerator;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Partially used to load {@link EncryptablePropertyFilter} config.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings("ConfigurationProperties")
@ConfigurationProperties(prefix = "mixmicro.security.encryptor", ignoreUnknownFields = true)
@Data
public class MixmicroSecurityEncryptorConfigurationProperties {

  public static MixmicroSecurityEncryptorConfigurationProperties bindConfigProps(
      ConfigurableEnvironment environment) {
    final BindHandler handler = new IgnoreErrorsBindHandler(BindHandler.DEFAULT);
    final MutablePropertySources propertySources = environment.getPropertySources();
    final Binder binder =
        new Binder(
            ConfigurationPropertySources.from(propertySources),
            new PropertySourcesPlaceholdersResolver(propertySources),
            ApplicationConversionService.getSharedInstance());
    final MixmicroSecurityEncryptorConfigurationProperties config =
        new MixmicroSecurityEncryptorConfigurationProperties();

    final ResolvableType type =
        ResolvableType.forClass(MixmicroSecurityEncryptorConfigurationProperties.class);
    final Annotation annotation =
        AnnotationUtils.findAnnotation(
            MixmicroSecurityEncryptorConfigurationProperties.class, ConfigurationProperties.class);
    final Annotation[] annotations = new Annotation[] {annotation};
    final Bindable<?> target =
        Bindable.of(type).withExistingValue(config).withAnnotations(annotations);

    binder.bind("mixmicro.security.encryptor", target, handler);
    return config;
  }

  /**
   * Whether to use JDK/Cglib (depending on classpath availability) proxy with an AOP advice as a
   * decorator for existing {@link org.springframework.core.env.PropertySource} or just simply use
   * targeted wrapper Classes. Default Value is {@code false}.
   */
  private Boolean proxyPropertySources = false;

  /**
   * Define a list of {@link org.springframework.core.env.PropertySource} to skip from
   * wrapping/proxying. Properties held in classes on this list will not be eligible for decryption.
   * Default Value is {@code empty list}.
   */
  private List<String> skipPropertySources = Collections.emptyList();

  /**
   * Specify the name of bean to override enhance security core's default properties based {@link
   * StringEncryptor}. Default Value is {@code mixStringEncryptor}.
   */
  private String bean = "mixStringEncryptor";

  /**
   * Master Password used for Encryption/Decryption of properties.
   *
   * @see PBEStringEncryptor
   * @see StringPBEConfig#getPassword()
   */
  private String password;

  /**
   * Encryption/Decryption Algorithm to be used by MixSecurity. Default Value is {@code
   * "PBEWITHHMACSHA512ANDAES_256"}.
   *
   * @see PBEStringEncryptor
   * @see StringPBEConfig#getAlgorithm()
   */
  private String algorithm = "PBEWITHHMACSHA512ANDAES_256";

  /**
   * Number of hashing iterations to obtain the signing key. Default Value is {@code "1000"}.
   *
   * @see PBEStringEncryptor
   * @see StringPBEConfig#getKeyObtentionIterations()
   */
  private String keyObtentionIterations = "1000";

  /**
   * The size of the pool of encryptors to be created. Default Value is {@code "1"}.
   *
   * @see PBEStringEncryptor
   * @see StringPBEConfig#getPoolSize()
   */
  private String poolSize = "1";

  /**
   * The name of the {@link java.security.Provider} implementation to be used by the encryptor for
   * obtaining the encryption algorithm. Default Value is {@code null}.
   *
   * @see PBEStringEncryptor
   * @see StringPBEConfig#getProviderName()
   */
  private String providerName = null;

  /**
   * The class name of the {@link java.security.Provider} implementation to be used by the encryptor
   * for obtaining the encryption algorithm. Default Value is {@code null}.
   *
   * @see PBEStringEncryptor
   * @see SimpleStringPBEConfig#setProviderClassName(String)
   */
  private String providerClassName = null;

  /**
   * A {@link SaltGenerator} implementation to be used by the encryptor. Default Value is {@code
   * "RandomSaltGenerator"}.
   *
   * @see PBEStringEncryptor
   * @see StringPBEConfig#getSaltGenerator()
   */
  private String saltGeneratorClassname = "xyz.vopen.mixmicro.components.enhance.security.salt.RandomSaltGenerator";

  /**
   * A {@link IvGenerator} implementation to be used by the encryptor. Default Value is {@code
   * "RandomIvGenerator"}.
   *
   * @see PBEStringEncryptor
   * @see StringPBEConfig#getIvGenerator()
   */
  private String ivGeneratorClassname = "xyz.vopen.mixmicro.components.enhance.security.iv.RandomIvGenerator";

  /**
   * Specify the form in which String output will be encoded. {@code "base64"} or {@code
   * "hexadecimal"}. Default Value is {@code "base64"}.
   *
   * @see PBEStringEncryptor
   * @see StringPBEConfig#getStringOutputType()
   */
  private String stringOutputType = "base64";

  /**
   * Specify a PEM/DER base64 encoded string. PEM encoded keys can simply omit the "BEGIN/END
   * PRIVATE KEY" header/footer and just specify the base64 encoded key. This property takes
   * precedence over {@link #setPrivateKeyLocation(String)}
   */
  private String privateKeyString = null;

  /**
   * Specify a PEM/DER private key location, in Spring's resource nomenclature (i.e.
   * classpath:resource/path or file://path/to/file)
   */
  private String privateKeyLocation = null;

  /** Specify the private key format to use: DER (default) or PEM */
  private KeyFormat privateKeyFormat = KeyFormat.DER;

  @NestedConfigurationProperty
  private PropertyConfigurationProperties property = new PropertyConfigurationProperties();

  @Data
  public static class PropertyConfigurationProperties {

    /**
     * Specify the name of the bean to be provided for a custom {@link EncryptablePropertyDetector}.
     * Default value is {@code "encryptablePropertyDetector"}
     */
    private String detectorBean = "encryptablePropertyDetector";

    /**
     * Specify the name of the bean to be provided for a custom {@link EncryptablePropertyResolver}.
     * Default value is {@code "encryptablePropertyResolver"}
     */
    private String resolverBean = "encryptablePropertyResolver";

    /**
     * Specify the name of the bean to be provided for a custom {@link EncryptablePropertyFilter}.
     * Default value is {@code "encryptablePropertyFilter"}
     */
    private String filterBean = "encryptablePropertyFilter";

    /**
     * Specify a custom {@link String} to identify as prefix of encrypted properties. Default value
     * is {@code "MIXENC["}
     */
    private String prefix = "MIXENC[";

    /**
     * Specify a custom {@link String} to identify as suffix of encrypted properties. Default value
     * is {@code "]"}
     */
    private String suffix = "]";

    @NestedConfigurationProperty
    private FilterConfigurationProperties filter = new FilterConfigurationProperties();

    @Data
    public static class FilterConfigurationProperties {

      /**
       * Specify the property sources name patterns to be included for decryption by{@link
       * EncryptablePropertyFilter}. Default value is {@code null}
       */
      private List<String> includeSources = null;

      /**
       * Specify the property sources name patterns to be EXCLUDED for decryption by{@link
       * EncryptablePropertyFilter}. Default value is {@code null}
       */
      private List<String> excludeSources = null;

      /**
       * Specify the property name patterns to be included for decryption by{@link
       * EncryptablePropertyFilter}. Default value is {@code null}
       */
      private List<String> includeNames = null;

      /**
       * Specify the property name patterns to be EXCLUDED for decryption by{@link
       * EncryptablePropertyFilter}. Default value is {@code mixmicro\\.security\\.encryptor\\.*}
       */
      private List<String> excludeNames = singletonList("^mixmicro\\.security\\.encryptor\\.*");
    }
  }
}
