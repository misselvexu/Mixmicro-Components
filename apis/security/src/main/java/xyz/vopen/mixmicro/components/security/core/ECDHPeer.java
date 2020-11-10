package xyz.vopen.mixmicro.components.security.core;

import org.bouncycastle.jce.ECNamedCurveTable;

import javax.crypto.KeyAgreement;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;

/**
 * <code>KeyAgreementPeer</code> implementation that uses the Elliptic Curve Diffie-Hellman key
 * agreement protocol.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ECDHPeer extends KeyAgreementPeer {

  private static final String ALGORITHM = "ECDH";

  /*
   * Attributes
   */

  private AlgorithmParameterSpec spec;

  /*
   * Constructor(s)
   */

  /**
   * Constructs a <code>ECDHPeer</code> instance using a named elliptic curve <code>curve</code>.
   *
   * <p><b>Note:</b> This constructor will use Bouncy Castle (BC) as its provider.
   *
   * @param curve
   * @throws GeneralSecurityException
   */
  public ECDHPeer(String curve) throws GeneralSecurityException {
    this(curve, null);
  }

  /**
   * Constructs a <code>ECDHPeer</code> instance using a named elliptic curve <code>curve</code>.
   *
   * <p><b>Note:</b> This constructor will use Bouncy Castle (BC) as its provider.
   *
   * @param curve
   * @param keyPair
   * @throws GeneralSecurityException
   */
  public ECDHPeer(String curve, KeyPair keyPair) throws GeneralSecurityException {
    this(ECNamedCurveTable.getParameterSpec(curve), keyPair, "BC");
  }

  /**
   * Constructs a <code>ECDHPeer</code> instance using a custom elliptic curve.
   *
   * @param p
   * @param a
   * @param b
   * @param xg
   * @param yg
   * @param n
   * @param h
   * @throws GeneralSecurityException
   */
  public ECDHPeer(
      BigInteger p, BigInteger a, BigInteger b, BigInteger xg, BigInteger yg, BigInteger n, int h)
      throws GeneralSecurityException {
    this(
        new ECParameterSpec(new EllipticCurve(new ECFieldFp(p), a, b), new ECPoint(xg, yg), n, h),
        null);
  }

  /**
   * Constructs a <code>ECDHPeer</code> instance using a custom elliptic curve.
   *
   * @param p
   * @param a
   * @param b
   * @param xg
   * @param yg
   * @param n
   * @param h
   * @param keyPair
   * @throws GeneralSecurityException
   */
  public ECDHPeer(
      BigInteger p,
      BigInteger a,
      BigInteger b,
      BigInteger xg,
      BigInteger yg,
      BigInteger n,
      int h,
      KeyPair keyPair)
      throws GeneralSecurityException {
    this(p, a, b, xg, yg, n, h, keyPair, null);
  }

  /**
   * Constructs a <code>ECDHPeer</code> instance using a custom elliptic curve.
   *
   * @param p
   * @param a
   * @param b
   * @param xg
   * @param yg
   * @param n
   * @param h
   * @param keyPair
   * @param provider
   * @throws GeneralSecurityException
   */
  public ECDHPeer(
      BigInteger p,
      BigInteger a,
      BigInteger b,
      BigInteger xg,
      BigInteger yg,
      BigInteger n,
      int h,
      KeyPair keyPair,
      String provider)
      throws GeneralSecurityException {
    this(
        new ECParameterSpec(new EllipticCurve(new ECFieldFp(p), a, b), new ECPoint(xg, yg), n, h),
        keyPair,
        provider);
  }

  /**
   * Constructs a <code>ECDHPeer</code> instance using an elliptic curve <code>
   * AlgorithmParameterSpec</code>.
   *
   * @param spec
   * @throws GeneralSecurityException
   */
  public ECDHPeer(AlgorithmParameterSpec spec) throws GeneralSecurityException {
    this(spec, null);
  }

  /**
   * Constructs a <code>ECDHPeer</code> instance using an elliptic curve <code>
   * AlgorithmParameterSpec</code>.
   *
   * @param spec
   * @param keyPair
   * @throws GeneralSecurityException
   */
  public ECDHPeer(AlgorithmParameterSpec spec, KeyPair keyPair) throws GeneralSecurityException {
    this(spec, keyPair, null);
  }

  /**
   * Constructs a <code>ECDHPeer</code> instance using an elliptic curve <code>
   * AlgorithmParameterSpec</code> and a custom provider.
   *
   * @param spec
   * @param keyPair
   * @param provider
   * @throws GeneralSecurityException
   */
  public ECDHPeer(AlgorithmParameterSpec spec, KeyPair keyPair, String provider)
      throws GeneralSecurityException {
    super(
        provider != null
            ? KeyAgreement.getInstance(ALGORITHM, provider)
            : KeyAgreement.getInstance(ALGORITHM),
        keyPair);
    this.spec = spec;
    initialize();
  }

  /*
   * Class methods
   */

  /**
   * Returns the inner <code>AlgorithmParameterSpec</code>.
   *
   * @return
   */
  public AlgorithmParameterSpec getAlgorithmParameterSpec() {
    return spec;
  }

  /*
   * Concrete methods
   */

  @Override
  protected KeyPair createKeyPair() throws GeneralSecurityException {
    return createKeyPair(spec, getKeyAgreement().getProvider());
  }

  /*
   * Static methods
   */

  /**
   * Creates and returns an ECDH key pair for the given parameter specification.
   *
   * @param spec
   * @return
   * @throws GeneralSecurityException
   */
  public static final KeyPair createKeyPair(AlgorithmParameterSpec spec)
      throws GeneralSecurityException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
    keyGen.initialize(spec, new SecureRandom());
    return keyGen.generateKeyPair();
  }

  /**
   * Creates and returns an ECDH key pair for the given parameter specification and provider.
   *
   * @param spec
   * @param provider
   * @return
   * @throws GeneralSecurityException
   */
  public static final KeyPair createKeyPair(AlgorithmParameterSpec spec, String provider)
      throws GeneralSecurityException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, provider);
    keyGen.initialize(spec, new SecureRandom());
    return keyGen.generateKeyPair();
  }

  /**
   * Creates and returns an ECDH key pair for the given parameter specification and provider.
   *
   * @param spec
   * @param provider
   * @return
   * @throws GeneralSecurityException
   */
  public static final KeyPair createKeyPair(AlgorithmParameterSpec spec, Provider provider)
      throws GeneralSecurityException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, provider);
    keyGen.initialize(spec, new SecureRandom());
    return keyGen.generateKeyPair();
  }
}
