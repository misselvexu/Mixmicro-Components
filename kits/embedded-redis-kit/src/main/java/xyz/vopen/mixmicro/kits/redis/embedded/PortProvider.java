package xyz.vopen.mixmicro.kits.redis.embedded;

/**
 * {@link PortProvider}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface PortProvider {

  /**
   * Fetch Next Available Port
   *
   * @return available port
   */
  int next();
}
