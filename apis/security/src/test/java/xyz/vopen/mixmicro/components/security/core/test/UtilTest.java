package xyz.vopen.mixmicro.components.security.core.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import xyz.vopen.mixmicro.components.security.core.util.FileEncryptor;
import xyz.vopen.mixmicro.components.security.core.util.TextEncryptor;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertTrue;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class UtilTest {

  private static final String FILENAME = "test_picture.jpg";
  private static final String FILENAME_ENCRYPTED = "test_picture.jpg.encrypted";
  private static final String FILENAME_DECRYPTED = "test_picture_util.jpg";

  @Test
  public void testEncryptedFile() throws GeneralSecurityException, IOException {
    File srcFile = new File(FILENAME);
    File encryptedFile = new File(FILENAME_ENCRYPTED);
    File decryptedFile = new File(FILENAME_DECRYPTED);
    FileEncryptor fe = new FileEncryptor();
    fe.encrypt(srcFile, encryptedFile);
    fe.decrypt(encryptedFile, decryptedFile);
    assertTrue(FileUtils.contentEquals(srcFile, decryptedFile));
  }

  @Test
  public void testEncryptedText() throws GeneralSecurityException {
    String message = "This is a short test message";
    TextEncryptor te = new TextEncryptor();
    String encrypted = te.encrypt(message);
    String decrypted = te.decrypt(encrypted);
    assertTrue(decrypted.equals(message));
  }
}
