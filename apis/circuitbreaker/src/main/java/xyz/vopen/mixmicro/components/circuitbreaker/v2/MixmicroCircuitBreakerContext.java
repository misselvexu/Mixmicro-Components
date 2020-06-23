package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import xyz.vopen.mixmicro.components.circuitbreaker.v2.exception.MixmicroCircuitBreakerException;

/**
 * {@link MixmicroCircuitBreakerContext}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/23
 */
public interface MixmicroCircuitBreakerContext<Handler> {

  /**
   * Register Circuit Breaker Context Handler
   *
   * @param handler handler instance
   * @throws MixmicroCircuitBreakerException maybe thrown {@link MixmicroCircuitBreakerException}
   */
  void register(Handler handler) throws MixmicroCircuitBreakerException;
}
