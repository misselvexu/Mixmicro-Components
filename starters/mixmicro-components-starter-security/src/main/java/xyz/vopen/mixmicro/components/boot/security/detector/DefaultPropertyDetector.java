package xyz.vopen.mixmicro.components.boot.security.detector;

import org.springframework.util.Assert;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyDetector;

/**
 * Default property detector that detects encrypted property values with the format
 * "$prefix$encrypted_value$suffix" Default values are "MIXENC[" and "]" respectively.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DefaultPropertyDetector implements EncryptablePropertyDetector {

  private String prefix = "MIXENC[";
  private String suffix = "]";

  public DefaultPropertyDetector() {}

  public DefaultPropertyDetector(String prefix, String suffix) {
    Assert.notNull(prefix, "Prefix can't be null");
    Assert.notNull(suffix, "Suffix can't be null");
    this.prefix = prefix;
    this.suffix = suffix;
  }

  @Override
  public boolean isEncrypted(String property) {
    if (property == null) {
      return false;
    }
    final String trimmedValue = property.trim();
    return (trimmedValue.startsWith(prefix) && trimmedValue.endsWith(suffix));
  }

  @Override
  public String unwrapEncryptedValue(String property) {
    return property.substring(prefix.length(), (property.length() - suffix.length()));
  }
}
