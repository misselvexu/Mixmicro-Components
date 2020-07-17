package xyz.vopen.mixmicro.kits.redis.embedded.ports;

import xyz.vopen.mixmicro.kits.redis.embedded.PortProvider;
import xyz.vopen.mixmicro.kits.redis.embedded.exceptions.RedisBuildingException;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link PredefinedPortProvider}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class PredefinedPortProvider implements PortProvider {
  private final List<Integer> ports = new LinkedList<Integer>();
  private final Iterator<Integer> current;

  public PredefinedPortProvider(Collection<Integer> ports) {
    this.ports.addAll(ports);
    this.current = this.ports.iterator();
  }

  @Override
  public synchronized int next() {
    if (!current.hasNext()) {
      throw new RedisBuildingException("Run out of Redis ports!");
    }
    return current.next();
  }
}
