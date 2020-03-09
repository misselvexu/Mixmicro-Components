package xyz.vopen.mixmicro.components.boot.security.encryptor;

import lombok.SneakyThrows;
import xyz.vopen.mixmicro.components.enhance.security.encryption.ByteEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * String Encryptor that delegates always to a {@link ByteEncryptor} and converts results to/from
 * Base64 for string representation.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ByteEncryptorStringEncryptorDelegate implements StringEncryptor {
  private final ByteEncryptor delegate;

  public ByteEncryptorStringEncryptorDelegate(ByteEncryptor delegate) {
    this.delegate = delegate;
  }

  @Override
  @SneakyThrows
  public String encrypt(String message) {
    return Base64.getEncoder()
        .encodeToString(delegate.encrypt(message.getBytes(StandardCharsets.UTF_8)));
  }

  @Override
  @SneakyThrows
  public String decrypt(String encryptedMessage) {
    return new String(
        delegate.decrypt(Base64.getDecoder().decode(encryptedMessage)), StandardCharsets.UTF_8);
  }
}
