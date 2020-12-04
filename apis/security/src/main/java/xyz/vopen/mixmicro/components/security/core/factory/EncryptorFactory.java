package xyz.vopen.mixmicro.components.security.core.factory;

import xyz.vopen.mixmicro.components.security.core.Encryptor;

import java.security.Key;

/**
 * Factory interface for constructing <code>Encryptor</code> instances.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface EncryptorFactory {
  /** AES <code>EncryptorFactory</code> implementation */
  EncryptorFactory AES = new AESEncryptorFactory();
  /** DES <code>EncryptorFactory</code> implementation */
  EncryptorFactory DES = new DESEncryptorFactory();
  /**
   * Returns an <code>Encryptor</code> instance usable for message encryption.
   *
   * @param key
   * @return
   */
  Encryptor messageEncryptor(Key key);
  /**
   * Returns an <code>Encryptor</code> instance usable for stream encryption.
   *
   * @param key
   * @return
   */
  Encryptor streamEncryptor(Key key);
}
