package xyz.vopen.mixmicro.components.boot.security;

import org.springframework.core.env.PropertySource;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public interface EncryptablePropertySource<T> {

  PropertySource<T> getDelegate();

  Object getProperty(String name);

  void refresh();

  default Object getProperty(
      EncryptablePropertyResolver resolver,
      EncryptablePropertyFilter filter,
      PropertySource<T> source,
      String name) {
    Object value = source.getProperty(name);
    if (filter.shouldInclude(source, name) && value instanceof String) {
      String stringValue = String.valueOf(value);
      return resolver.resolvePropertyValue(stringValue);
    }
    return value;
  }
}
