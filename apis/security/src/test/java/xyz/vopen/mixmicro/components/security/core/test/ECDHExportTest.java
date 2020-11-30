package xyz.vopen.mixmicro.components.security.core.test;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.junit.Assert;
import org.junit.Test;
import xyz.vopen.mixmicro.components.security.core.ECDHPeer;
import xyz.vopen.mixmicro.components.security.core.KeyAgreementPeer;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * Unit test for exporting/importing curve25519 public keys.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ECDHExportTest {

  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  @Test
  public void testExportImport() throws GeneralSecurityException {

    // Create a curve25519 parameter spec
    X9ECParameters params = CustomNamedCurves.getByName("curve25519");
    ECParameterSpec ecParameterSpec =
        new ECParameterSpec(
            params.getCurve(), params.getG(), params.getN(), params.getH(), params.getSeed());

    // Create public key
    KeyAgreementPeer peer = new ECDHPeer(ecParameterSpec, null, "BC");
    ECPublicKey ecPublicKey = (ECPublicKey) peer.getPublicKey();

    // Export public key
    byte[] encoded = ecPublicKey.getQ().getEncoded(true);

    System.out.println(Arrays.toString(encoded));
    System.out.println("Encoded length: " + encoded.length);

    // Import public key
    ECPublicKey importedECPublicKey = loadPublicKey(encoded);

    Assert.assertArrayEquals(ecPublicKey.getEncoded(), importedECPublicKey.getEncoded());
  }

  /**
   * Loads and returns the elliptic-curve public key from the data byte array.
   *
   * @param data
   * @return
   * @throws NoSuchAlgorithmException
   * @throws NoSuchProviderException
   * @throws InvalidKeySpecException
   */
  public static ECPublicKey loadPublicKey(byte[] data)
      throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
    X9ECParameters params = CustomNamedCurves.getByName("curve25519");
    ECParameterSpec ecParameterSpec =
        new ECParameterSpec(
            params.getCurve(), params.getG(), params.getN(), params.getH(), params.getSeed());

    ECPublicKeySpec publicKey =
        new ECPublicKeySpec(ecParameterSpec.getCurve().decodePoint(data), ecParameterSpec);
    KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
    return (ECPublicKey) kf.generatePublic(publicKey);
  }
}
