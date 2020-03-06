package xyz.vopen.mixmicro.components.enhance.cache.anno;

import java.time.Duration;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface CacheConsts {
  String DEFAULT_AREA = "default";
  boolean DEFAULT_ENABLED = true;
  int DEFAULT_EXPIRE = Integer.MAX_VALUE;
  CacheType DEFAULT_CACHE_TYPE = CacheType.REMOTE;
  int DEFAULT_LOCAL_LIMIT = 100;
  boolean DEFAULT_CACHE_NULL_VALUE = false;
  String DEFAULT_SERIAL_POLICY = SerialPolicy.JAVA;
  boolean DEFAULT_MULTI = false;

  Duration ASYNC_RESULT_TIMEOUT = Duration.ofMillis(1000);

  String UNDEFINED_STRING = "$$undefined$$";
  int UNDEFINED_INT = Integer.MIN_VALUE;
  long UNDEFINED_LONG = Long.MIN_VALUE;

  static boolean isUndefined(String value) {
    return UNDEFINED_STRING.equals(value);
  }

  static boolean isUndefined(int value) {
    return UNDEFINED_INT == value;
  }
}
