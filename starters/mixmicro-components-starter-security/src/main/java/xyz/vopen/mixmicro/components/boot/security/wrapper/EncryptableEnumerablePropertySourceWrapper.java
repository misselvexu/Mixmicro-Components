package xyz.vopen.mixmicro.components.boot.security.wrapper;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertySource;
import xyz.vopen.mixmicro.components.boot.security.caching.CachingDelegateEncryptablePropertySource;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class EncryptableEnumerablePropertySourceWrapper<T> extends EnumerablePropertySource<T>
    implements EncryptablePropertySource<T> {
  private final EncryptablePropertySource<T> encryptableDelegate;

  public EncryptableEnumerablePropertySourceWrapper(
      EnumerablePropertySource<T> delegate,
      EncryptablePropertyResolver resolver,
      EncryptablePropertyFilter filter) {
    super(delegate.getName(), delegate.getSource());
    encryptableDelegate =
        new CachingDelegateEncryptablePropertySource<>(delegate, resolver, filter);
  }

  @Override
  public Object getProperty(String name) {
    return encryptableDelegate.getProperty(name);
  }

  @Override
  public void refresh() {
    encryptableDelegate.refresh();
  }

  @Override
  public PropertySource<T> getDelegate() {
    return encryptableDelegate.getDelegate();
  }

  @Override
  public String[] getPropertyNames() {
    return ((EnumerablePropertySource) encryptableDelegate.getDelegate()).getPropertyNames();
  }
}
