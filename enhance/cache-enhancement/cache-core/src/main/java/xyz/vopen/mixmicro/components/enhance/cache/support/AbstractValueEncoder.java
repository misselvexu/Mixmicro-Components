package xyz.vopen.mixmicro.components.enhance.cache.support;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public abstract class AbstractValueEncoder implements Function<Object, byte[]> {

  protected boolean useIdentityNumber;

  public AbstractValueEncoder(boolean useIdentityNumber) {
    this.useIdentityNumber = useIdentityNumber;
  }

  protected void writeHeader(byte[] buf, int header) {
    buf[0] = (byte) (header >> 24 & 0xFF);
    buf[1] = (byte) (header >> 16 & 0xFF);
    buf[2] = (byte) (header >> 8 & 0xFF);
    buf[3] = (byte) (header & 0xFF);
  }

  public boolean isUseIdentityNumber() {
    return useIdentityNumber;
  }
}
