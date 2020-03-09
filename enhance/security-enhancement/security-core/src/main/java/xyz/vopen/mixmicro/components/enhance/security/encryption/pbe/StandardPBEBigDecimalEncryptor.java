/*
 * =============================================================================
 *
 *   Copyright (c) 2017-2019, VOPEN.XYZ (http://vopen.xyz)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package xyz.vopen.mixmicro.components.enhance.security.encryption.pbe;

import xyz.vopen.mixmicro.components.enhance.security.commons.CommonUtils;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config.PBEConfig;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.EncryptionInitializationException;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.EncryptionOperationNotPossibleException;
import xyz.vopen.mixmicro.components.enhance.security.iv.IvGenerator;
import xyz.vopen.mixmicro.components.enhance.security.salt.SaltGenerator;
import xyz.vopen.mixmicro.components.enhance.security.iv.NoIvGenerator;
import xyz.vopen.mixmicro.components.enhance.security.salt.RandomSaltGenerator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Provider;

/**
 * Standard implementation of the {@link PBEBigDecimalEncryptor} interface. This class lets the user
 * specify the algorithm (and provider) to be used for encryption, the password to use, the number
 * of hashing iterations and the salt generator that will be applied for obtaining the encryption
 * key.
 *
 * <p><b>Important</b>: The size of the result of encrypting a number, depending on the algorithm,
 * may be much bigger (in bytes) than the size of the encrypted number itself. For example,
 * encrypting a 4-byte integer can result in an encrypted 16-byte number. This can lead the user
 * into problems if the encrypted values are to be stored and not enough room has been provided.
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * <p><br>
 * <b><u>Configuration</u></b>
 *
 * <p>The algorithm, provider, password, key-obtention iterations and salt generator can take values
 * in any of these ways:
 *
 * <ul>
 *   <li>Using its default values (except for password).
 *   <li>Setting a <tt>{@link PBEConfig}</tt> object which provides
 *       new configuration values.
 *   <li>Calling the corresponding <tt>setAlgorithm(...)</tt>, <tt>setProvider(...)</tt>,
 *       <tt>setProviderName(...)</tt>, <tt>setPassword(...)</tt>,
 *       <tt>setKeyObtentionIterations(...)</tt> or <tt>setSaltGenerator(...)</tt> methods.
 * </ul>
 *
 * And the actual values to be used for initialization will be established by applying the following
 * priorities:
 *
 * <ol>
 *   <li>First, the default values are considered (except for password).
 *   <li>Then, if a <tt>{@link PBEConfig}</tt> object has been set
 *       with <tt>setConfig(...)</tt>, the non-null values returned by its <tt>getX()</tt> methods
 *       override the default values.
 *   <li>Finally, if the corresponding <tt>setX(...)</tt> method has been called on the encryptor
 *       itself for any of the configuration parameters, the values set by these calls override all
 *       of the above.
 * </ol>
 *
 * <p><br>
 * <b><u>Initialization</u></b>
 *
 * <p>Before it is ready to encrypt, an object of this class has to be <i>initialized</i>.
 * Initialization happens:
 *
 * <ul>
 *   <li>When <tt>initialize()</tt> is called.
 *   <li>When <tt>encrypt(...)</tt> or <tt>decrypt(...)</tt> are called for the first time, if
 *       <tt>initialize()</tt> has not been called before.
 * </ul>
 *
 * Once an encryptor has been initialized, trying to change its configuration will result in an
 * <tt>AlreadyInitializedException</tt> being thrown.
 *
 * <p><br>
 * <b><u>Usage</u></b>
 *
 * <p>An encryptor may be used for:
 *
 * <ul>
 *   <li><i>Encrypting messages</i>, by calling the <tt>encrypt(...)</tt> method.
 *   <li><i>Decrypting messages</i>, by calling the <tt>decrypt(...)</tt> method.
 * </ul>
 *
 * <b>If a random salt generator is used, two encryption results for the same message will always be
 * different (except in the case of random salt coincidence)</b>. This may enforce security by
 * difficulting brute force attacks on sets of data at a time and forcing attackers to perform a
 * brute force attack on each separate piece of encrypted data.
 *
 * <p>To learn more about the mechanisms involved in encryption, read <a
 * href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5:
 * Password-Based Cryptography Standard</a>.
 *
 * @since 1.2
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class StandardPBEBigDecimalEncryptor
    implements PBEBigDecimalCleanablePasswordEncryptor {

  // The StandardPBEByteEncryptor that will be internally used.
  private final StandardPBEByteEncryptor byteEncryptor;

  /** Creates a new instance of <tt>StandardPBEBigDecimalEncryptor</tt>. */
  public StandardPBEBigDecimalEncryptor() {
    super();
    this.byteEncryptor = new StandardPBEByteEncryptor();
  }

  /*
   * Creates a new instance of <tt>StandardPBEBigDecimalEncryptor</tt> using
   * the specified byte digester (constructor used for cloning)
   */
  private StandardPBEBigDecimalEncryptor(final StandardPBEByteEncryptor standardPBEByteEncryptor) {
    super();
    this.byteEncryptor = standardPBEByteEncryptor;
  }

  /**
   * Sets a <tt>{@link PBEConfig}</tt> object for the encryptor. If
   * this config object is set, it will be asked values for:
   *
   * <ul>
   *   <li>Algorithm
   *   <li>Security Provider (or provider name)
   *   <li>Password
   *   <li>Hashing iterations for obtaining the encryption key
   *   <li>Salt generator
   * </ul>
   *
   * <p>The non-null values it returns will override the default ones, <i>and will be overriden by
   * any values specified with a <tt>setX</tt> method</i>.
   *
   * @param config the <tt>PBEConfig</tt> object to be used as the source for configuration
   *     parameters.
   */
  public void setConfig(final PBEConfig config) {
    this.byteEncryptor.setConfig(config);
  }

  /**
   * Sets the algorithm to be used for encryption, like <tt>PBEWithMD5AndDES</tt>.
   *
   * <p>This algorithm has to be supported by your JCE provider (if you specify one, or the default
   * JVM provider if you don't) and, if it is supported, you can also specify <i>mode</i> and
   * <i>padding</i> for it, like <tt>ALGORITHM/MODE/PADDING</tt>.
   *
   * @param algorithm the name of the algorithm to be used.
   */
  public void setAlgorithm(final String algorithm) {
    this.byteEncryptor.setAlgorithm(algorithm);
  }

  /**
   * Sets the password to be used.
   *
   * <p><b>There is no default value for password</b>, so not setting this parameter either from a
   * {@link PBEConfig} object or from a call to
   * <tt>setPassword</tt> will result in an EncryptionInitializationException being thrown during
   * initialization.
   *
   * @param password the password to be used.
   */
  public void setPassword(final String password) {
    this.byteEncryptor.setPassword(password);
  }

  /**
   * Sets the password to be used, as a char[].
   *
   * <p>This allows the password to be specified as a <i>cleanable</i> char[] instead of a String,
   * in extreme security conscious environments in which no copy of the password as an immutable
   * String should be kept in memory.
   *
   * <p><b>Important</b>: the array specified as a parameter WILL BE COPIED in order to be stored as
   * encryptor configuration. The caller of this method will therefore be responsible for its
   * cleaning (security will only clean the internally stored copy).
   *
   * <p><b>There is no default value for password</b>, so not setting this parameter either from a
   * {@link PBEConfig} object or from a call to
   * <tt>setPassword</tt> will result in an EncryptionInitializationException being thrown during
   * initialization.
   *
   * @since 1.8
   * @param password the password to be used.
   */
  public void setPasswordCharArray(char[] password) {
    this.byteEncryptor.setPasswordCharArray(password);
  }

  /**
   * Set the number of hashing iterations applied to obtain the encryption key.
   *
   * <p>This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"
   * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   *
   * @param keyObtentionIterations the number of iterations
   */
  public void setKeyObtentionIterations(final int keyObtentionIterations) {
    this.byteEncryptor.setKeyObtentionIterations(keyObtentionIterations);
  }

  /**
   * Sets the salt generator to be used. If no salt generator is specified, an instance of {@link
   * RandomSaltGenerator} will be used.
   *
   * @param saltGenerator the salt generator to be used.
   */
  public void setSaltGenerator(final SaltGenerator saltGenerator) {
    this.byteEncryptor.setSaltGenerator(saltGenerator);
  }

  /**
   * Sets the IV generator to be used. If no IV generator is specified, an instance of {@link
   * NoIvGenerator} will be used.
   *
   * @param ivGenerator the IV generator to be used.
   */
  public void setIvGenerator(final IvGenerator ivGenerator) {
    this.byteEncryptor.setIvGenerator(ivGenerator);
  }

  /**
   * Sets the name of the security provider to be asked for the encryption algorithm. This security
   * provider has to be registered beforehand at the JVM security framework.
   *
   * <p>The provider can also be set with the {@link #setProvider(Provider)} method, in which case
   * it will not be necessary neither registering the provider beforehand, nor calling this {@link
   * #setProviderName(String)} method to specify a provider name.
   *
   * <p>Note that a call to {@link #setProvider(Provider)} overrides any value set by this method.
   *
   * <p>If no provider name / provider is explicitly set, the default JVM provider will be used.
   *
   * @since 1.3
   * @param providerName the name of the security provider to be asked for the encryption algorithm.
   */
  public void setProviderName(final String providerName) {
    this.byteEncryptor.setProviderName(providerName);
  }

  /**
   * Sets the security provider to be asked for the encryption algorithm. The provider does not have
   * to be registered at the security infrastructure beforehand, and its being used here will not
   * result in its being registered.
   *
   * <p>If this method is called, calling {@link #setProviderName(String)} becomes unnecessary.
   *
   * <p>If no provider name / provider is explicitly set, the default JVM provider will be used.
   *
   * @since 1.3
   * @param provider the provider to be asked for the chosen algorithm
   */
  public void setProvider(final Provider provider) {
    this.byteEncryptor.setProvider(provider);
  }

  /*
   * Clone this encryptor 'size' times and initialize it.
   * This encryptor will be at position 0 itself.
   * Clones will NOT be initialized.
   */
  synchronized StandardPBEBigDecimalEncryptor[] cloneAndInitializeEncryptor(final int size) {

    final StandardPBEByteEncryptor[] byteEncryptorClones =
        this.byteEncryptor.cloneAndInitializeEncryptor(size);

    final StandardPBEBigDecimalEncryptor[] clones = new StandardPBEBigDecimalEncryptor[size];

    clones[0] = this;

    for (int i = 1; i < size; i++) {
      clones[i] = new StandardPBEBigDecimalEncryptor(byteEncryptorClones[i]);
    }

    return clones;
  }

  /**
   * Returns true if the encryptor has already been initialized, false if not.<br>
   * Initialization happens:
   *
   * <ul>
   *   <li>When <tt>initialize</tt> is called.
   *   <li>When <tt>encrypt</tt> or <tt>decrypt</tt> are called for the first time, if
   *       <tt>initialize</tt> has not been called before.
   * </ul>
   *
   * <p>Once an encryptor has been initialized, trying to change its configuration will result in an
   * <tt>AlreadyInitializedException</tt> being thrown.
   *
   * @return true if the encryptor has already been initialized, false if not.
   */
  public boolean isInitialized() {
    return this.byteEncryptor.isInitialized();
  }

  /**
   * Initialize the encryptor.
   *
   * <p>This operation will consist in determining the actual configuration values to be used, and
   * then initializing the encryptor with them. <br>
   * These values are decided by applying the following priorities:
   *
   * <ol>
   *   <li>First, the default values are considered (except for password).
   *   <li>Then, if a <tt>{@link PBEConfig}</tt> object has been
   *       set with <tt>setConfig</tt>, the non-null values returned by its <tt>getX</tt> methods
   *       override the default values.
   *   <li>Finally, if the corresponding <tt>setX</tt> method has been called on the encryptor
   *       itself for any of the configuration parameters, the values set by these calls override
   *       all of the above.
   * </ol>
   *
   * <p>Once an encryptor has been initialized, trying to change its configuration will result in an
   * <tt>AlreadyInitializedException</tt> being thrown.
   *
   * @throws EncryptionInitializationException if initialization could not be correctly done (for
   *     example, no password has been set).
   */
  public void initialize() {
    this.byteEncryptor.initialize();
  }

  /**
   * Encrypts a message using the specified configuration.
   *
   * <p>The resulting BigDecimal will have the same scale as the original one (although the total
   * number of bytes will be higher).
   *
   * <p><b>Important</b>: The size of the result of encrypting a number, depending on the algorithm,
   * may be much bigger (in bytes) than the size of the encrypted number itself. For example,
   * encrypting a 4-byte integer can result in an encrypted 16-byte number. This can lead the user
   * into problems if the encrypted values are to be stored and not enough room has been provided.
   *
   * <p>The mechanisms applied to perform the encryption operation are described in <a
   * href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5:
   * Password-Based Cryptography Standard</a>.
   *
   * <p>This encryptor uses a salt for each encryption operation. The size of the salt depends on
   * the algorithm being used. This salt is used for creating the encryption key and, if generated
   * by a random generator, it is also appended unencrypted at the beginning of the results so that
   * a decryption operation can be performed.
   *
   * <p><b>If a random salt generator is used, two encryption results for the same message will
   * always be different (except in the case of random salt coincidence)</b>. This may enforce
   * security by difficulting brute force attacks on sets of data at a time and forcing attackers to
   * perform a brute force attack on each separate piece of encrypted data.
   *
   * @param message the BigDecimal message to be encrypted
   * @return the result of encryption
   * @throws EncryptionOperationNotPossibleException if the encryption operation fails, ommitting
   *     any further information about the cause for security reasons.
   * @throws EncryptionInitializationException if initialization could not be correctly done (for
   *     example, no password has been set).
   */
  public BigDecimal encrypt(final BigDecimal message) {

    if (message == null) {
      return null;
    }

    try {

      // Get the scale of the decimal number
      final int scale = message.scale();

      // Get the number in binary form (without scale)
      final BigInteger unscaledMessage = message.unscaledValue();
      final byte[] messageBytes = unscaledMessage.toByteArray();

      // The StandardPBEByteEncryptor does its job.
      final byte[] encryptedMessage = this.byteEncryptor.encrypt(messageBytes);

      // The length of the encrypted message will be stored
      // with the result itself so that we can correctly rebuild
      // the complete byte array when decrypting (BigInteger will
      // ignore all "0x0" bytes in the leftmost side, and also "-0x1"
      // in the leftmost side will be translated as signum).
      final byte[] encryptedMessageLengthBytes =
          NumberUtils.byteArrayFromInt(encryptedMessage.length);

      // Append the length bytes to the encrypted message
      final byte[] encryptionResult =
          CommonUtils.appendArrays(encryptedMessage, encryptedMessageLengthBytes);

      // Finally, return a new number built from the encrypted bytes
      return new BigDecimal(new BigInteger(encryptionResult), scale);

    } catch (EncryptionInitializationException e) {
      throw e;
    } catch (EncryptionOperationNotPossibleException e) {
      throw e;
    } catch (Exception e) {
      // If encryption fails, it is more secure not to return any
      // information about the cause in nested exceptions. Simply fail.
      throw new EncryptionOperationNotPossibleException();
    }
  }

  /**
   * Decrypts a message using the specified configuration.
   *
   * <p>The mechanisms applied to perform the decryption operation are described in <a
   * href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5:
   * Password-Based Cryptography Standard</a>.
   *
   * <p>If a random salt generator is used, this decryption operation will expect to find an
   * unencrypted salt at the beginning of the encrypted input, so that the decryption operation can
   * be correctly performed (there is no other way of knowing it).
   *
   * @param encryptedMessage the BigDecimal message to be decrypted
   * @return the result of decryption
   * @throws EncryptionOperationNotPossibleException if the decryption operation fails, ommitting
   *     any further information about the cause for security reasons.
   * @throws EncryptionInitializationException if initialization could not be correctly done (for
   *     example, no password has been set).
   */
  public BigDecimal decrypt(BigDecimal encryptedMessage) {

    if (encryptedMessage == null) {
      return null;
    }

    try {

      // Get the scale
      int scale = encryptedMessage.scale();

      // Get the number (unscaled) in binary form
      BigInteger unscaledEncryptedMessage = encryptedMessage.unscaledValue();
      byte[] encryptedMessageBytes = unscaledEncryptedMessage.toByteArray();

      // Process the encrypted byte array (check size, pad if needed...)
      encryptedMessageBytes =
          NumberUtils.processBigIntegerEncryptedByteArray(
              encryptedMessageBytes, encryptedMessage.signum());

      // Let the byte encyptor decrypt
      byte[] message = this.byteEncryptor.decrypt(encryptedMessageBytes);

      // Finally, return a new number built from the decrypted bytes
      return new BigDecimal(new BigInteger(message), scale);

    } catch (EncryptionInitializationException e) {
      throw e;
    } catch (EncryptionOperationNotPossibleException e) {
      throw e;
    } catch (Exception e) {
      // If decryption fails, it is more secure not to return any
      // information about the cause in nested exceptions. Simply fail.
      throw new EncryptionOperationNotPossibleException();
    }
  }
}
