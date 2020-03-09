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
package xyz.vopen.mixmicro.components.enhance.security.util.password;

import xyz.vopen.mixmicro.components.enhance.security.digest.StandardByteDigester;
import xyz.vopen.mixmicro.components.enhance.security.digest.StandardStringDigester;
import xyz.vopen.mixmicro.components.enhance.security.digest.config.DigesterConfig;

import java.security.Provider;

/**
 * Utility class for easily performing password digesting and checking.
 *
 * <p>This class internally holds a {@link StandardStringDigester} which can be configured by the
 * user by optionally choosing the algorithm to be used, the output format (BASE64 or hexadecimal)
 * the mechanism of encryption (plain digests vs. use of random salt and iteration count (default))
 * and even use a {@link DigesterConfig} object for more advanced configuration.
 *
 * <p>The results obtained when encoding with this class are encoded in BASE64 form.
 *
 * <p>The required steps to use it are:
 *
 * <ol>
 *   <li>Create an instance (using <tt>new</tt>).
 *   <li>Configure if needed with the <tt>setX()</tt> methods.
 *   <li>Perform the desired <tt>{@link #encryptPassword(String)}</tt> or <tt>{@link
 *       #checkPassword(String, String)}</tt> operations.
 * </ol>
 *
 * <p>This class is <i>thread-safe</i>
 *
 * @since 1.2
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class ConfigurablePasswordEncryptor implements PasswordEncryptor {

  // The internal digester used
  private final StandardStringDigester digester;

  /** Creates a new instance of <tt>ConfigurablePasswordEncryptor</tt> */
  public ConfigurablePasswordEncryptor() {
    super();
    this.digester = new StandardStringDigester();
  }

  /**
   * Lets the user configure this encryptor with a {@link DigesterConfig} object, like if he/she
   * were using a {@link StandardStringDigester} object directly.
   *
   * @param config the DigesterConfig object to be set for configuration.
   * @see StandardStringDigester#setConfig(DigesterConfig)
   */
  public void setConfig(final DigesterConfig config) {
    this.digester.setConfig(config);
  }

  /**
   * Sets the algorithm to be used for digesting, like <tt>MD5</tt> or <tt>SHA-1</tt>.
   *
   * <p>This algorithm has to be supported by your security infrastructure, and it should be allowed
   * as an algorithm for creating java.security.MessageDigest instances.
   *
   * <p>If you are specifying a security provider with {@link #setProvider(Provider)} or {@link
   * #setProviderName(String)}, this algorithm should be supported by your specified provider.
   *
   * <p>If you are not specifying a provider, you will be able to use those algorithms provided by
   * the default security provider of your JVM vendor. For valid names in the Sun JVM, see <a
   * target="_blank"
   * href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java
   * Cryptography Architecture API Specification & Reference</a>.
   *
   * @param algorithm the name of the algorithm to be used.
   * @see StandardStringDigester#setAlgorithm(String)
   */
  public void setAlgorithm(final String algorithm) {
    this.digester.setAlgorithm(algorithm);
  }

  /**
   * Sets the name of the security provider to be asked for the digest algorithm. This security
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
   * @param providerName the name of the security provider to be asked for the digest algorithm.
   * @throws AlreadyInitializedException if it has already been initialized, this is, if {@link
   *     #encryptPassword(String)} or {@link #checkPassword(String, String)} have been called at
   *     least once.
   */
  public void setProviderName(final String providerName) {
    this.digester.setProviderName(providerName);
  }

  /**
   * Sets the security provider to be asked for the digest algorithm. The provider does not have to
   * be registered at the security infrastructure beforehand, and its being used here will not
   * result in it being registered.
   *
   * <p>If this method is called, calling {@link #setProviderName(String)} becomes unnecessary.
   *
   * <p>If no provider name / provider is explicitly set, the default JVM provider will be used.
   *
   * @since 1.3
   * @param provider the provider to be asked for the chosen algorithm
   * @throws AlreadyInitializedException if it has already been initialized, this is, if {@link
   *     #encryptPassword(String)} or {@link #checkPassword(String, String)} have been called at
   *     least once.
   */
  public void setProvider(final Provider provider) {
    this.digester.setProvider(provider);
  }

  /**
   * Lets the user specify if he/she wants a plain digest used as an encryption mechanism (no salt
   * or iterations, as with {@link java.security.MessageDigest}), or rather use the security's usual
   * stronger mechanism for password encryption (based on the use of a salt and the iteration of the
   * hash function).
   *
   * @param plainDigest true for using plain digests, false for the strong salt and iteration count
   *     based mechanism.
   */
  public void setPlainDigest(final boolean plainDigest) {
    if (plainDigest) {
      this.digester.setIterations(1);
      this.digester.setSaltSizeBytes(0);
    } else {
      this.digester.setIterations(StandardByteDigester.DEFAULT_ITERATIONS);
      this.digester.setSaltSizeBytes(StandardByteDigester.DEFAULT_SALT_SIZE_BYTES);
    }
  }

  /**
   * Sets the the form in which String output will be encoded. Available encoding types are:
   *
   * <ul>
   *   <li><tt><b>base64</b></tt> (default)
   *   <li><tt><b>hexadecimal</b></tt>
   * </ul>
   *
   * @since 1.3
   * @param stringOutputType the string output type.
   */
  public void setStringOutputType(final String stringOutputType) {
    this.digester.setStringOutputType(stringOutputType);
  }

  /**
   * Encrypts (digests) a password.
   *
   * @param password the password to be encrypted.
   * @return the resulting digest.
   * @see StandardStringDigester#digest(String)
   */
  public String encryptPassword(final String password) {
    return this.digester.digest(password);
  }

  /**
   * Checks an unencrypted (plain) password against an encrypted one (a digest) to see if they
   * match.
   *
   * @param plainPassword the plain password to check.
   * @param encryptedPassword the digest against which to check the password.
   * @return true if passwords match, false if not.
   * @see StandardStringDigester#matches(String, String)
   */
  public boolean checkPassword(final String plainPassword, final String encryptedPassword) {
    return this.digester.matches(plainPassword, encryptedPassword);
  }
}
