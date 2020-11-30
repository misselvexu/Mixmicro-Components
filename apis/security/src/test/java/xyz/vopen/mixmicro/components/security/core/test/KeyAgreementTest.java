package xyz.vopen.mixmicro.components.security.core.test;

import org.junit.Test;
import xyz.vopen.mixmicro.components.security.core.DHPeer;
import xyz.vopen.mixmicro.components.security.core.ECDHPeer;
import xyz.vopen.mixmicro.components.security.core.KeyAgreementPeer;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.Security;

import static org.junit.Assert.assertArrayEquals;

/**
 * Unit test that tests key agreement algorithms such as Diffie-Hellman and Elliptic Curve
 * Diffie-Hellman.
 *
 * <p><b>Note:</b> This unit test does not contain MQV and ECMQV test methods because these
 * algorithms are considered insecure and thus obsolete.
 *
 * @see https://en.wikipedia.org/wiki/Diffie%E2%80%93Hellman_key_exchange
 * @see https://en.wikipedia.org/wiki/Elliptic_curve_Diffie%E2%80%93Hellman
 * @see https://en.wikipedia.org/wiki/MQV
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class KeyAgreementTest {

  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  /**
   * Tests Diffie-Hellman key exchange.
   *
   * <p>Use at least a <code>p</code> of 2048 bits. Better pre-determined values for <code>p</code>
   * can be found at the link below.
   *
   * @see https://tools.ietf.org/html/rfc3526
   * @throws GeneralSecurityException
   */
  @Test
  public void testDH() throws GeneralSecurityException {

    // Create primes p & g
    // Tip: You don't need to regenerate p; Use a fixed value in your application
    int bits = 2048;
    BigInteger p = BigInteger.probablePrime(bits, new SecureRandom());
    BigInteger g = new BigInteger("2");

    // Create two peers
    KeyAgreementPeer peerA = new DHPeer(p, g);
    KeyAgreementPeer peerB = new DHPeer(p, g);

    // Exchange public keys and compute shared secret
    byte[] sharedSecretA = peerA.computeSharedSecret(peerB.getPublicKey());
    byte[] sharedSecretB = peerB.computeSharedSecret(peerA.getPublicKey());

    assertArrayEquals(sharedSecretA, sharedSecretB);
  }

  @Test
  public void testECDH() throws GeneralSecurityException {

    String algorithm = "brainpoolp512r1";

    // Create two peers
    KeyAgreementPeer peerA = new ECDHPeer(algorithm);
    KeyAgreementPeer peerB = new ECDHPeer(algorithm);

    // Exchange public keys and compute shared secret
    byte[] sharedSecretA = peerA.computeSharedSecret(peerB.getPublicKey());
    byte[] sharedSecretB = peerB.computeSharedSecret(peerA.getPublicKey());

    assertArrayEquals(sharedSecretA, sharedSecretB);
  }

  // MQV and ECMQV are not safe!

  /*@Test public void testECMQV() throws GeneralSecurityException {

  	String algorithm = "brainpoolp512r1";

  	// Create two peers
  	KeyAgreementPeer peerA = new ECMQVPeer(algorithm);
  	KeyAgreementPeer peerB = new ECMQVPeer(algorithm);

  	// Exchange public keys and compute shared secret
  	byte[] sharedSecretA = peerA.computeSharedSecret(peerB.getPublicKey());
  	byte[] sharedSecretB = peerB.computeSharedSecret(peerA.getPublicKey());

  	assertArrayEquals(sharedSecretA, sharedSecretB);
  }*/
}
