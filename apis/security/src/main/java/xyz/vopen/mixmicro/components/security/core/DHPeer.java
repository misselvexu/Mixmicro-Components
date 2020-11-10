package xyz.vopen.mixmicro.components.security.core;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;

/**
 * <code>KeyAgreementPeer</code> implementation that uses the Diffie-Hellman key agreement protocol.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DHPeer extends KeyAgreementPeer {

  private static final String ALGORITHM = "DH";

  /*
   * Attributes
   */

  private BigInteger p;
  private BigInteger g;

  /*
   * Constructor(s)
   */

  /**
   * Constructs a <code>DHPeer</code> instance using primes <code>p</code> and <code>g</code>.
   *
   * <p><b>Note:</b> Use {@link BigInteger#probablePrime(int, java.util.Random)} to create good
   * <code>p</code> and <code>g</code> candidates.
   *
   * @param p
   * @param g
   * @throws GeneralSecurityException
   */
  public DHPeer(BigInteger p, BigInteger g) throws GeneralSecurityException {
    this(p, g, null);
  }

  /**
   * Constructs a <code>DHPeer</code> instance using primes <code>p</code> and <code>g</code>.
   *
   * <p><b>Note:</b> Use {@link BigInteger#probablePrime(int, java.util.Random)} to create good
   * <code>p</code> and <code>g</code> candidates.
   *
   * @param p
   * @param g
   * @param keyPair
   * @throws GeneralSecurityException
   */
  public DHPeer(BigInteger p, BigInteger g, KeyPair keyPair) throws GeneralSecurityException {
    this(p, g, keyPair, null);
  }

  /**
   * Constructs a <code>DHPeer</code> instance using primes <code>p</code> and <code>g</code>.
   *
   * <p><b>Note:</b> Use {@link BigInteger#probablePrime(int, java.util.Random)} to create good
   * <code>p</code> and <code>g</code> candidates.
   *
   * @param p
   * @param g
   * @param keyPair
   * @throws GeneralSecurityException
   */
  public DHPeer(BigInteger p, BigInteger g, KeyPair keyPair, String provider)
      throws GeneralSecurityException {
    super(
        provider != null
            ? KeyAgreement.getInstance(ALGORITHM, provider)
            : KeyAgreement.getInstance(ALGORITHM),
        keyPair);
    this.p = p;
    this.g = g;
    initialize();
  }

  /*
   * Class methods
   */

  /**
   * Returns prime <code>p</code>.
   *
   * @return
   */
  public BigInteger getP() {
    return p;
  }

  /**
   * Returns prime <code>g</code>.
   *
   * @return
   */
  public BigInteger getG() {
    return g;
  }

  /*
   * Concrete methods
   */

  @Override
  protected KeyPair createKeyPair() throws GeneralSecurityException {
    return createKeyPair(p, g, getKeyAgreement().getProvider());
  }

  /*
   * Static methods
   */

  /**
   * Creates and returns a DH key pair for the given <code>p</code> and <code>g</code>.
   *
   * @param p
   * @param g
   * @return
   * @throws GeneralSecurityException
   */
  public static final KeyPair createKeyPair(BigInteger p, BigInteger g)
      throws GeneralSecurityException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
    keyGen.initialize(new DHParameterSpec(p, g), new SecureRandom());
    return keyGen.generateKeyPair();
  }

  /**
   * Creates and returns a DH key pair for the given <code>p</code>, <code>g</code> and provider.
   *
   * @param p
   * @param g
   * @param provider
   * @return
   * @throws GeneralSecurityException
   */
  public static final KeyPair createKeyPair(BigInteger p, BigInteger g, Provider provider)
      throws GeneralSecurityException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, provider);
    keyGen.initialize(new DHParameterSpec(p, g), new SecureRandom());
    return keyGen.generateKeyPair();
  }
}
