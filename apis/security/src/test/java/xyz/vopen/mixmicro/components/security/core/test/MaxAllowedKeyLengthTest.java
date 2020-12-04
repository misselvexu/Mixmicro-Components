package xyz.vopen.mixmicro.components.security.core.test;

import org.junit.Test;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;

/**
 * Tests whether the Java Cryptography Extension (JCE) unlimited strength jurisdiction policy files
 * have been installed.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MaxAllowedKeyLengthTest {

  @Test
  public void maxAllowedKeyLengthTest() throws GeneralSecurityException {
    String[] algorithms =
        new String[] {"AES", "DES", "RSA", "Blowfish", "RC2", "RC4", "RC5", "ARC4"};
    for (String algorithm : algorithms) {
      int maxKeyLength = Cipher.getMaxAllowedKeyLength(algorithm);
      System.out.println(algorithm + ": " + maxKeyLength);
      assertEquals(Integer.MAX_VALUE, maxKeyLength);
    }
  }
}
