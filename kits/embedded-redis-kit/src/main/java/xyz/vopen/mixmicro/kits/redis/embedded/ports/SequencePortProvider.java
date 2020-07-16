package xyz.vopen.mixmicro.kits.redis.embedded.ports;

import xyz.vopen.mixmicro.kits.redis.embedded.PortProvider;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link SequencePortProvider}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SequencePortProvider implements PortProvider {
  private final AtomicInteger currentPort = new AtomicInteger(26379);

  public SequencePortProvider() {
  }

  public SequencePortProvider(int currentPort) {
    this.currentPort.set(currentPort);
  }

  public void setCurrentPort(int port) {
    currentPort.set(port);
  }

  @Override
  public int next() {
    return currentPort.getAndIncrement();
  }
}
