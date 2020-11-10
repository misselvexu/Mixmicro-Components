package xyz.vopen.mixmicro.components.security.core.factory;

/**
 * Factory class for creating secure AES keys.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class AESKeyFactory extends AbsKeyFactory {

  public static final String ALGORITHM = "AES";
  public static final int MAXIMUM_KEY_LENGTH = 256;

  public AESKeyFactory() {
    super(ALGORITHM, MAXIMUM_KEY_LENGTH);
  }
}
