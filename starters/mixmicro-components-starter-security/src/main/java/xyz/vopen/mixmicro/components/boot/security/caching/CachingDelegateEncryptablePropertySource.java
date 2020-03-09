package xyz.vopen.mixmicro.components.boot.security.caching;

import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertySource;

import java.util.HashMap;
import java.util.Map;

public class CachingDelegateEncryptablePropertySource<T> extends PropertySource<T>
    implements EncryptablePropertySource<T> {
  private final PropertySource<T> delegate;
  private final EncryptablePropertyResolver resolver;
  private final EncryptablePropertyFilter filter;
  private Map<String, Object> cache;

  public CachingDelegateEncryptablePropertySource(
      PropertySource<T> delegate,
      EncryptablePropertyResolver resolver,
      EncryptablePropertyFilter filter) {
    super(delegate.getName(), delegate.getSource());
    Assert.notNull(delegate, "PropertySource delegate cannot be null");
    Assert.notNull(resolver, "EncryptablePropertyResolver cannot be null");
    Assert.notNull(filter, "EncryptablePropertyFilter cannot be null");
    this.delegate = delegate;
    this.resolver = resolver;
    this.filter = filter;
    this.cache = new HashMap<>();
  }

  @Override
  public PropertySource<T> getDelegate() {
    return delegate;
  }

  @Override
  public Object getProperty(String name) {
    // Can be called recursively, so, we cannot use computeIfAbsent.
    synchronized (this) {
      if (!cache.containsKey(name)) {
        Object resolved = getProperty(resolver, filter, delegate, name);
        if (resolved != null) {
          cache.put(name, resolved);
        }
      }
      return cache.get(name);
    }
  }

  @Override
  public void refresh() {
    cache.clear();
  }
}
