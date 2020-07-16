package xyz.vopen.mixmicro.kits.redis.embedded;

import xyz.vopen.mixmicro.kits.redis.embedded.exceptions.EmbeddedRedisException;

import java.util.List;

/**
 * {@link Redis}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface Redis {

  /**
   * Check Redis Server is Active
   *
   * @return true/false
   */
  boolean isActive();

  void start() throws EmbeddedRedisException;

  void stop() throws EmbeddedRedisException;

  List<Integer> ports();
}
