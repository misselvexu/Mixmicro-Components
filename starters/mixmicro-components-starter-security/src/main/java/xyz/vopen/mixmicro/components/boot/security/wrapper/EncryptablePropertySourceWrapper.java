package xyz.vopen.mixmicro.components.boot.security.wrapper;

import org.springframework.core.env.PropertySource;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertySource;
import xyz.vopen.mixmicro.components.boot.security.caching.CachingDelegateEncryptablePropertySource;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;

/**
 * Wrapper for {@link PropertySource} instances that simply delegates the {@link #getProperty}
 * method to the {@link PropertySource} delegate instance to retrieve properties, while checking if
 * the resulting property is encrypted or not using the MixSecurity convention of surrounding
 * encrypted values with "ENC()".
 *
 * <p>When an encrypted property is detected, it is decrypted using the provided {@link
 * StringEncryptor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class EncryptablePropertySourceWrapper<T> extends PropertySource<T>
    implements EncryptablePropertySource<T> {
  private final EncryptablePropertySource<T> encryptableDelegate;

  public EncryptablePropertySourceWrapper(
      PropertySource<T> delegate,
      EncryptablePropertyResolver resolver,
      EncryptablePropertyFilter filter) {
    super(delegate.getName(), delegate.getSource());
    encryptableDelegate =
        new CachingDelegateEncryptablePropertySource<>(delegate, resolver, filter);
  }

  @Override
  public void refresh() {
    encryptableDelegate.refresh();
  }

  @Override
  public Object getProperty(String name) {
    return encryptableDelegate.getProperty(name);
  }

  @Override
  public PropertySource<T> getDelegate() {
    return encryptableDelegate.getDelegate();
  }
}
