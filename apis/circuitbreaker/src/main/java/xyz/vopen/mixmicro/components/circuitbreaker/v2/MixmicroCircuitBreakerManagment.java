package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.kits.lang.Nullable;

import java.util.Map;

/**
 * {@link MixmicroCircuitBreakerManagment}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/23
 */
public class MixmicroCircuitBreakerManagment {

  private static final Logger log = LoggerFactory.getLogger(MixmicroCircuitBreakerManagment.class);

  /**
   * Cached Circuit Breaker Instance(s)
   *
   * <p>
   */
  private static final Map<CircuitBreakerType, MixmicroCircuitBreakable> CONTEXTS = Maps.newConcurrentMap();

  /**
   * Register Circuit Breaker Instance
   *
   * @param type type of {@link CircuitBreakerType}
   * @param breakable instance of {@link MixmicroCircuitBreakable}
   */
  public static @Nullable MixmicroCircuitBreakable register(CircuitBreakerType type, MixmicroCircuitBreakable breakable) {
    return CONTEXTS.put(type, breakable);
  }

  /**
   * Get Circuit Breaker Instance By {@link CircuitBreakerType}
   *
   * @param type type of {@link CircuitBreakerType}
   * @return instance of {@link MixmicroCircuitBreakable}
   */
  public static MixmicroCircuitBreakable get(CircuitBreakerType type) {
    return CONTEXTS.get(type);
  }

}
