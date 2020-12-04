package xyz.vopen.mixmicro.components.security.core.factory;

import java.security.Key;

/**
 * Factory interface for generating keys.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface KeyFactory {
  /** AES <code>KeyFactory</code> implementation */
  KeyFactory AES = new AESKeyFactory();
  /** AES <code>KeyFactory</code> implementation */
  KeyFactory DES = new DESKeyFactory();
  /**
   * Derives and returns a strong key from a password.
   *
   * @param password
   * @return
   */
  Key keyFromPassword(char[] password);
  /**
   * Generates a strong random key.
   *
   * @return
   */
  Key randomKey();
  /**
   * Generates a random key of size <code>size</code>.
   *
   * @param size
   * @return
   */
  Key randomKey(int size);
}
