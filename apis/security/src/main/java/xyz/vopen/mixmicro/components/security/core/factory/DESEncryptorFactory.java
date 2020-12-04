package xyz.vopen.mixmicro.components.security.core.factory;

import xyz.vopen.mixmicro.components.security.core.Encryptor;

import java.security.Key;

/**
 * Factory class for constructing DES <code>Encryptor</code> instances.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DESEncryptorFactory implements EncryptorFactory {

  @Override
  public final Encryptor messageEncryptor(Key key) {
    return new Encryptor(key, "DES/CBC/PKCS5Padding", 16);
  }

  @Override
  public final Encryptor streamEncryptor(Key key) {
    return new Encryptor(key, "DES/CTR/NoPadding", 16);
  }
}
