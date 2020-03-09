package xyz.vopen.mixmicro.components.boot.security.encryptor;

import xyz.vopen.mixmicro.components.boot.security.util.AsymmetricCryptography;
import xyz.vopen.mixmicro.components.boot.security.util.Singleton;
import xyz.vopen.mixmicro.components.enhance.security.encryption.ByteEncryptor;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Vanilla implementation of an asymmetric encryptor that relies on {@link AsymmetricCryptography}
 * Keys are lazily loaded from {@link SimpleAsymmetricConfig}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SimpleAsymmetricByteEncryptor implements ByteEncryptor {

  private final AsymmetricCryptography crypto;
  private final Singleton<PublicKey> publicKey;
  private final Singleton<PrivateKey> privateKey;

  public SimpleAsymmetricByteEncryptor(SimpleAsymmetricConfig config) {
    crypto = new AsymmetricCryptography(config.getResourceLoader());
    privateKey =
        Singleton.fromLazy(
            crypto::getPrivateKey, config::loadPrivateKeyResource, config::getPrivateKeyFormat);
    publicKey =
        Singleton.fromLazy(
            crypto::getPublicKey, config::loadPublicKeyResource, config::getPublicKeyFormat);
  }

  @Override
  public byte[] encrypt(byte[] message) {
    return this.crypto.encrypt(message, publicKey.get());
  }

  @Override
  public byte[] decrypt(byte[] encryptedMessage) {
    return this.crypto.decrypt(encryptedMessage, privateKey.get());
  }
}
