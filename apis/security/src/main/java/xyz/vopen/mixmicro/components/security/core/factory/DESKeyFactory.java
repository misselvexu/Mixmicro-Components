package xyz.vopen.mixmicro.components.security.core.factory;

/**
 * Factory class for creating secure DES keys.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DESKeyFactory extends AbsKeyFactory {

  public static final String ALGORITHM = "DES";
  public static final int MAXIMUM_KEY_LENGTH = 64;

  public DESKeyFactory() {
    super(ALGORITHM, MAXIMUM_KEY_LENGTH);
  }
}
