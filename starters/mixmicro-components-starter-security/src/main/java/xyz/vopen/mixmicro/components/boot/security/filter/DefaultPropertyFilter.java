package xyz.vopen.mixmicro.components.boot.security.filter;

import org.springframework.core.env.PropertySource;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyFilter;

import java.util.List;

/**
 * Default Strategy for contemplating properties for decryption based on the following constructor
 * args:
 *
 * <p><b>includeSourceNames:</b> To include property sources by name, provide a list of regex. If
 * set, property sources that don't match will be excluded <b>includePropertyNames:</b> To include
 * properties by name, provide a list of regex. If set, properties that don't match will be excluded
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DefaultPropertyFilter implements EncryptablePropertyFilter {

  private final List<String> includeSourceNames;
  private final List<String> excludeSourceNames;
  private final List<String> includePropertyNames;
  private final List<String> excludePropertyNames;

  public DefaultPropertyFilter() {
    includeSourceNames = null;
    includePropertyNames = null;
    excludeSourceNames = null;
    excludePropertyNames = null;
  }

  public DefaultPropertyFilter(
      List<String> includeSourceNames,
      List<String> excludeSourceNames,
      List<String> includePropertyNames,
      List<String> excludePropertyNames) {
    this.includeSourceNames = includeSourceNames;
    this.excludeSourceNames = excludeSourceNames;
    this.includePropertyNames = includePropertyNames;
    this.excludePropertyNames = excludePropertyNames;
  }

  @Override
  public boolean shouldInclude(PropertySource<?> source, String name) {
    if (isIncludeAll()) {
      return true;
    }

    if (isMatch(source.getName(), excludeSourceNames) || isMatch(name, excludePropertyNames)) {
      return false;
    }

    return isIncludeUnset()
        || isMatch(source.getName(), includeSourceNames)
        || isMatch(name, includePropertyNames);
  }

  private boolean isIncludeAll() {
    return isIncludeUnset() && isExcludeUnset();
  }

  private boolean isIncludeUnset() {
    return isEmpty(includeSourceNames) && isEmpty(includePropertyNames);
  }

  private boolean isExcludeUnset() {
    return isEmpty(excludeSourceNames) && isEmpty(excludePropertyNames);
  }

  private boolean isEmpty(List<String> patterns) {
    return patterns == null || patterns.isEmpty();
  }

  private boolean isMatch(String name, List<String> patterns) {
    return name != null && !isEmpty(patterns) && patterns.stream().anyMatch(name::matches);
  }
}
