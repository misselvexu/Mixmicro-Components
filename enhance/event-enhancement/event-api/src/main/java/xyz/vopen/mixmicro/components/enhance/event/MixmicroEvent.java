package xyz.vopen.mixmicro.components.enhance.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * {@link MixmicroEvent}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/18
 */
public class MixmicroEvent<T> extends RemoteApplicationEvent {

  private static final Logger log = LoggerFactory.getLogger("MIX-EVENT");

  protected T payload;

  public MixmicroEvent() {}

  public MixmicroEvent(
      T payload, Object source, String originService, String destinationService) {
    super(source, originService, destinationService);
    this.payload = payload;
  }

  public MixmicroEvent(T payload, Object source, String originService) {
    this(payload, source, originService, null);
  }
}
