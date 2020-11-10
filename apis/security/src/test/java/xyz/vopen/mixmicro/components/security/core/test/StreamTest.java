package xyz.vopen.mixmicro.components.security.core.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import xyz.vopen.mixmicro.components.security.core.Encryptor;
import xyz.vopen.mixmicro.components.security.core.factory.KeyFactory;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.Security;

import static org.junit.Assert.assertTrue;

/**
 * Unit test that tests AES streaming encryption with the <code>Encryptor</code> class.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class StreamTest {

  private static final String FILENAME = "test_picture.jpg";
  private static final String FILENAME_ENCRYPTED = "test_picture.jpg.encrypted";
  private static final String FILENAME_DECRYPTED = "test_picture_stream.jpg";
  private static final int AES_IV_SIZE = 16;
  private static final int DES_IV_SIZE = 8;
  private static final int BUFFER_SIZE = 4 * 1024;

  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  @Test
  public void testAES_CTR() throws GeneralSecurityException, IOException {
    Key key = KeyFactory.AES.randomKey();
    Encryptor encryptor = new Encryptor(key, "AES/CTR/NoPadding", AES_IV_SIZE);
    encryptor.setAlgorithmProvider("BC");

    InputStream is = null;
    OutputStream os = null;
    try {
      is = new FileInputStream(FILENAME);
      os = encryptor.wrapOutputStream(new FileOutputStream(FILENAME_ENCRYPTED));
      byte[] buffer = new byte[BUFFER_SIZE];
      int nRead;
      while ((nRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, nRead);
      }
      os.flush();
    } finally {
      if (is != null) {
        is.close();
      }
      if (os != null) {
        os.close();
      }
    }

    try {
      encryptor = new Encryptor(key, "AES/CTR/NoPadding", AES_IV_SIZE);
      encryptor.setAlgorithmProvider("BC");
      is = encryptor.wrapInputStream(new FileInputStream(FILENAME_ENCRYPTED));
      os = new FileOutputStream(FILENAME_DECRYPTED);
      byte[] buffer = new byte[BUFFER_SIZE];
      int nRead;
      while ((nRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, nRead);
      }
      os.flush();
    } finally {
      if (is != null) {
        is.close();
      }
      if (os != null) {
        os.close();
      }
    }

    assertTrue(FileUtils.contentEquals(new File(FILENAME), new File(FILENAME_DECRYPTED)));
  }

  @Test
  public void testDES_CTR() throws GeneralSecurityException, IOException {
    Key key = KeyFactory.DES.randomKey();
    Encryptor encryptor = new Encryptor(key, "DES/CTR/NoPadding", DES_IV_SIZE);
    encryptor.setAlgorithmProvider("BC");

    InputStream is = null;
    OutputStream os = null;
    try {
      is = new FileInputStream(FILENAME);
      os = encryptor.wrapOutputStream(new FileOutputStream(FILENAME_ENCRYPTED));
      byte[] buffer = new byte[BUFFER_SIZE];
      int nRead;
      while ((nRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, nRead);
      }
      os.flush();
    } finally {
      if (is != null) {
        is.close();
      }
      if (os != null) {
        os.close();
      }
    }

    try {
      encryptor = new Encryptor(key, "DES/CTR/NoPadding", DES_IV_SIZE);
      encryptor.setAlgorithmProvider("BC");
      is = encryptor.wrapInputStream(new FileInputStream(FILENAME_ENCRYPTED));
      os = new FileOutputStream(FILENAME_DECRYPTED);
      byte[] buffer = new byte[BUFFER_SIZE];
      int nRead;
      while ((nRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, nRead);
      }
      os.flush();
    } finally {
      if (is != null) {
        is.close();
      }
      if (os != null) {
        os.close();
      }
    }

    assertTrue(FileUtils.contentEquals(new File(FILENAME), new File(FILENAME_DECRYPTED)));
  }
}
