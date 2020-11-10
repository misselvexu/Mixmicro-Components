package xyz.vopen.mixmicro.components.security.core;

import javax.crypto.KeyAgreement;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;

/**
 * Abstract key agreement peer class.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class KeyAgreementPeer {

  /*
   * Attributes
   */

  private KeyAgreement keyAgreement;
  private KeyPair keyPair;

  /*
   * Constructor(s)
   */

  /**
   * Constructs a key agreement peer.
   *
   * @param keyAgreement
   * @throws GeneralSecurityException
   */
  public KeyAgreementPeer(KeyAgreement keyAgreement) throws GeneralSecurityException {
    this(keyAgreement, null);
  }

  /**
   * Constructs a key agreement peer.
   *
   * @param keyAgreement
   * @param keyPair
   * @throws GeneralSecurityException
   */
  public KeyAgreementPeer(KeyAgreement keyAgreement, KeyPair keyPair)
      throws GeneralSecurityException {
    this.keyAgreement = keyAgreement;
    this.keyPair = keyPair;
  }

  /*
   * Abstract methods
   */

  /**
   * Creates this peer's key pair if none has been provided upon construction.
   *
   * @return
   * @throws GeneralSecurityException
   */
  protected abstract KeyPair createKeyPair() throws GeneralSecurityException;

  /*
   * Class methods
   */

  /**
   * Creates a keypair and initializes the key agreement.
   *
   * @throws GeneralSecurityException
   */
  protected void initialize() throws GeneralSecurityException {
    if (keyPair == null) {
      keyPair = createKeyPair();
    }
    keyAgreement.init(keyPair.getPrivate());
  }

  /**
   * Computes the shared secret using the other peer's public key.
   *
   * @param key
   * @return
   * @throws InvalidKeyException
   */
  public byte[] computeSharedSecret(Key key) throws InvalidKeyException {
    keyAgreement.doPhase(key, true);
    return keyAgreement.generateSecret();
  }

  /**
   * Returns this peer's public key.
   *
   * @return
   */
  public Key getPublicKey() {
    return keyPair.getPublic();
  }

  /**
   * Returns this peer's <code>KeyAgreement</code> instance.
   *
   * @return
   */
  public KeyAgreement getKeyAgreement() {
    return keyAgreement;
  }
}
