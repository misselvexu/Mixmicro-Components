package xyz.vopen.mixmicro.components.boot.security.resolver;

import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyDetector;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.detector.DefaultPropertyDetector;
import xyz.vopen.mixmicro.components.boot.security.exception.MixSecurityDecryptionException;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.EncryptionOperationNotPossibleException;

import java.util.Optional;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class DefaultPropertyResolver implements EncryptablePropertyResolver {

  private final Environment environment;
  private StringEncryptor encryptor;
  private EncryptablePropertyDetector detector;

  public DefaultPropertyResolver(StringEncryptor encryptor, Environment environment) {
    this(encryptor, new DefaultPropertyDetector(), environment);
  }

  public DefaultPropertyResolver(
      StringEncryptor encryptor, EncryptablePropertyDetector detector, Environment environment) {
    this.environment = environment;
    Assert.notNull(encryptor, "String encryptor can't be null");
    Assert.notNull(detector, "Encryptable Property detector can't be null");
    this.encryptor = encryptor;
    this.detector = detector;
  }

  @Override
  public String resolvePropertyValue(String value) {
    return Optional.ofNullable(value)
        .map(environment::resolvePlaceholders)
        .filter(detector::isEncrypted)
        .map(
            resolvedValue -> {
              try {
                String unwrappedProperty = detector.unwrapEncryptedValue(resolvedValue.trim());
                String resolvedProperty = environment.resolvePlaceholders(unwrappedProperty);
                return encryptor.decrypt(resolvedProperty);
              } catch (EncryptionOperationNotPossibleException e) {
                throw new MixSecurityDecryptionException(
                    "Unable to decrypt: "
                        + value
                        + ". Decryption of Properties failed,  make sure encryption/decryption "
                        + "passwords match",
                    e);
              }
            })
        .orElse(value);
  }
}
