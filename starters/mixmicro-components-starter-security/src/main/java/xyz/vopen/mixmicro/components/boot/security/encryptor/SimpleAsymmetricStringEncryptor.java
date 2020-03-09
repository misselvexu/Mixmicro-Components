package xyz.vopen.mixmicro.components.boot.security.encryptor;

import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;

/**
 * {@link StringEncryptor} version of {@link SimpleAsymmetricByteEncryptor} that just relies on
 * delegation from {@link ByteEncryptorStringEncryptorDelegate} and provides a constructor for
 * {@link SimpleAsymmetricConfig}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SimpleAsymmetricStringEncryptor extends ByteEncryptorStringEncryptorDelegate {

  public SimpleAsymmetricStringEncryptor(SimpleAsymmetricByteEncryptor delegate) {
    super(delegate);
  }

  public SimpleAsymmetricStringEncryptor(SimpleAsymmetricConfig config) {
    super(new SimpleAsymmetricByteEncryptor(config));
  }
}
