package xyz.vopen.mixmicro.components.boot.httpclient.util;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

public final class BeanExtendUtils {

  private BeanExtendUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static void populate(final Object bean, final Map<String, ?> properties) {
    // Do nothing unless both arguments have been specified
    if ((bean == null) || (properties == null)) {
      return;
    }
    // Loop through the property name/value pairs to be set
    for (final Map.Entry<String, ?> entry : properties.entrySet()) {
      // Identify the property name and value(s) to be assigned
      final String name = entry.getKey();
      if (name == null) {
        continue;
      }
      // Perform the assignment for this property
      setProperty(bean, name, entry.getValue());
    }
  }

  public static void setProperty(final Object bean, String name, final Object value) {
    Class<?> beanClass = bean.getClass();
    PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(beanClass, name);
    if (propertyDescriptor == null) {
      return;
    }
    Method writeMethod = propertyDescriptor.getWriteMethod();
    try {
      writeMethod.invoke(bean, value);
    } catch (Exception e) {
      // skip
    }
  }
}
