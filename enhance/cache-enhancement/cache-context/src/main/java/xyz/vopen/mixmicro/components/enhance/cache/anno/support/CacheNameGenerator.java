package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface CacheNameGenerator {

  String generateCacheName(Method method, Object targetObject);

  String generateCacheName(Field field);
}
