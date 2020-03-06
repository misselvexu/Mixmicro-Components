package xyz.vopen.mixmicro.components.enhance.cache.support;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class JavaValueDecoder extends AbstractValueDecoder {

  public static final JavaValueDecoder INSTANCE = new JavaValueDecoder(true);

  public JavaValueDecoder(boolean useIdentityNumber) {
    super(useIdentityNumber);
  }

  @Override
  public Object doApply(byte[] buffer) throws Exception {
    ByteArrayInputStream in;
    if (useIdentityNumber) {
      in = new ByteArrayInputStream(buffer, 4, buffer.length - 4);
    } else {
      in = new ByteArrayInputStream(buffer);
    }
    ObjectInputStream ois = buildObjectInputStream(in);
    return ois.readObject();
  }

  protected ObjectInputStream buildObjectInputStream(ByteArrayInputStream in) throws IOException {
    return new ObjectInputStream(in);
  }
}
