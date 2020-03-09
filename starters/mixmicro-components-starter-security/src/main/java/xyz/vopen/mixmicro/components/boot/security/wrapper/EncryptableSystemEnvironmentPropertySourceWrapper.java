package xyz.vopen.mixmicro.components.boot.security.wrapper;

import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertySource;
import xyz.vopen.mixmicro.components.boot.security.caching.CachingDelegateEncryptablePropertySource;

import java.util.Map;

/** @author Tomas Tulka (@ttulka) */
public class EncryptableSystemEnvironmentPropertySourceWrapper
    extends SystemEnvironmentPropertySource
    implements EncryptablePropertySource<Map<String, Object>> {

  private final EncryptablePropertySource<Map<String, Object>> encryptableDelegate;

  public EncryptableSystemEnvironmentPropertySourceWrapper(
      SystemEnvironmentPropertySource delegate,
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
  public PropertySource<Map<String, Object>> getDelegate() {
    return encryptableDelegate.getDelegate();
  }
}
