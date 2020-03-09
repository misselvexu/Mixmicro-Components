package xyz.vopen.mixmicro.components.boot.security.encryptor;

import lombok.SneakyThrows;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.PBEByteEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.PBEStringEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SimplePBEStringEncryptor implements PBEStringEncryptor {

  private final PBEByteEncryptor delegate;

  public SimplePBEStringEncryptor(PBEByteEncryptor delegate) {
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

  @Override
  @SneakyThrows
  public void setPassword(String password) {
    throw new IllegalAccessException("Not Implemented, use delegate");
  }
}
