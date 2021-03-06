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
import xyz.vopen.mixmicro.components.enhance.security.exceptions.AlreadyInitializedException;
import xyz.vopen.mixmicro.components.enhance.security.iv.IvGenerator;
import xyz.vopen.mixmicro.components.enhance.security.salt.SaltGenerator;
import xyz.vopen.mixmicro.components.enhance.security.iv.NoIvGenerator;
import xyz.vopen.mixmicro.components.enhance.security.salt.RandomSaltGenerator;

import java.security.Provider;

/**
 * Pooled implementation of {@link PBEStringEncryptor} that in fact contains an array of {@link
 * StandardPBEStringEncryptor} objects which are used to attend encrypt and decrypt requests in
 * round-robin. This should result in higher performance in multiprocessor systems.
 *
 * <p>Configuration of this class is equivalent to that of {@link StandardPBEStringEncryptor}.
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * @since 1.7
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class PooledPBEStringEncryptor implements PBEStringCleanablePasswordEncryptor {

  private final StandardPBEStringEncryptor firstEncryptor;

  private PBEConfig config = null;
  private int poolSize = 0;
  private boolean poolSizeSet = false;

  private StandardPBEStringEncryptor[] pool;
  private int roundRobin = 0;

  /*
   * Flag which indicates whether the digester has been initialized or not.
   *
   * Once initialized, no further modifications to its configuration will
   * be allowed.
   */
  private boolean initialized = false;

  /** Creates a new instance of <tt>PooledStandardPBEStringEncryptor</tt>. */
  public PooledPBEStringEncryptor() {
    super();
    this.firstEncryptor = new StandardPBEStringEncryptor();
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
   *   <li>Output type (base64, hexadecimal) (only <tt>StringPBEConfig</tt>)
   * </ul>
   *
   * <p>The non-null values it returns will override the default ones, <i>and will be overriden by
   * any values specified with a <tt>setX</tt> method</i>.
   *
   * @param config the <tt>PBEConfig</tt> object to be used as the source for configuration
   *     parameters.
   */
  public synchronized void setConfig(final PBEConfig config) {
    this.firstEncryptor.setConfig(config);
    this.config = config;
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
    this.firstEncryptor.setAlgorithm(algorithm);
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
    this.firstEncryptor.setPassword(password);
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
  public synchronized void setPasswordCharArray(char[] password) {
    this.firstEncryptor.setPasswordCharArray(password);
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
    this.firstEncryptor.setKeyObtentionIterations(keyObtentionIterations);
  }

  /**
   * Sets the salt generator to be used. If no salt generator is specified, an instance of {@link
   * RandomSaltGenerator} will be used.
   *
   * @param saltGenerator the salt generator to be used.
   */
  public void setSaltGenerator(final SaltGenerator saltGenerator) {
    this.firstEncryptor.setSaltGenerator(saltGenerator);
  }

  /**
   * Sets the IV generator to be used. If no IV generator is specified, an instance of {@link
   * NoIvGenerator} will be used.
   *
   * @param ivGenerator the IV generator to be used.
   */
  public void setIvGenerator(final IvGenerator ivGenerator) {
    this.firstEncryptor.setIvGenerator(ivGenerator);
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
   * @param providerName the name of the security provider to be asked for the encryption algorithm.
   */
  public void setProviderName(final String providerName) {
    this.firstEncryptor.setProviderName(providerName);
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
   * @param provider the provider to be asked for the chosen algorithm
   */
  public void setProvider(final Provider provider) {
    this.firstEncryptor.setProvider(provider);
  }

  /**
   * Sets the the form in which String output will be encoded. Available encoding types are:
   *
   * <ul>
   *   <li><tt><b>base64</b></tt> (default)
   *   <li><tt><b>hexadecimal</b></tt>
   * </ul>
   *
   * <p>If not set, null will be returned.
   *
   * @param stringOutputType the string output type.
   */
  public synchronized void setStringOutputType(final String stringOutputType) {
    this.firstEncryptor.setStringOutputType(stringOutputType);
  }

  /**
   * Sets the size of the pool of digesters to be created.
   *
   * <p>This parameter is <b>required</b>.
   *
   * @param poolSize size of the pool
   */
  public synchronized void setPoolSize(final int poolSize) {
    CommonUtils.validateIsTrue(poolSize > 0, "Pool size be > 0");
    if (isInitialized()) {
      throw new AlreadyInitializedException();
    }
    this.poolSize = poolSize;
    this.poolSizeSet = true;
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
    return this.initialized;
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
  public synchronized void initialize() {

    // Double-check to avoid synchronization issues
    if (!this.initialized) {

      if (this.config != null) {

        final Integer configPoolSize = this.config.getPoolSize();

        this.poolSize =
            ((this.poolSizeSet) || (configPoolSize == null))
                ? this.poolSize
                : configPoolSize.intValue();
      }

      if (this.poolSize <= 0) {
        throw new IllegalArgumentException("Pool size must be set and > 0");
      }

      this.pool = this.firstEncryptor.cloneAndInitializeEncryptor(this.poolSize);

      this.initialized = true;
    }
  }

  /**
   * Encrypts a message using the specified configuration. The Strings returned by this method are
   * BASE64-encoded (default) or HEXADECIMAL ASCII Strings.
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
   * @param message the String message to be encrypted
   * @return the result of encryption
   * @throws EncryptionOperationNotPossibleException if the encryption operation fails, ommitting
   *     any further information about the cause for security reasons.
   * @throws EncryptionInitializationException if initialization could not be correctly done (for
   *     example, no password has been set).
   */
  public String encrypt(final String message) {

    // Check initialization
    if (!isInitialized()) {
      initialize();
    }

    int poolPosition;
    synchronized (this) {
      poolPosition = this.roundRobin;
      this.roundRobin = (this.roundRobin + 1) % this.poolSize;
    }

    return this.pool[poolPosition].encrypt(message);
  }

  /**
   * Decrypts a message using the specified configuration.
   *
   * <p>This method expects to receive a BASE64-encoded (default) or HEXADECIMAL ASCII String.
   *
   * <p>The mechanisms applied to perform the decryption operation are described in <a
   * href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5:
   * Password-Based Cryptography Standard</a>.
   *
   * <p>If a random salt generator is used, this decryption operation will expect to find an
   * unencrypted salt at the beginning of the encrypted input, so that the decryption operation can
   * be correctly performed (there is no other way of knowing it).
   *
   * @param encryptedMessage the String message to be decrypted
   * @return the result of decryption
   * @throws EncryptionOperationNotPossibleException if the decryption operation fails, ommitting
   *     any further information about the cause for security reasons.
   * @throws EncryptionInitializationException if initialization could not be correctly done (for
   *     example, no password has been set).
   */
  public String decrypt(final String encryptedMessage) {

    // Check initialization
    if (!isInitialized()) {
      initialize();
    }

    int poolPosition;
    synchronized (this) {
      poolPosition = this.roundRobin;
      this.roundRobin = (this.roundRobin + 1) % this.poolSize;
    }

    return this.pool[poolPosition].decrypt(encryptedMessage);
  }
}
