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
package xyz.vopen.mixmicro.components.enhance.security.util.digest;

import xyz.vopen.mixmicro.components.enhance.security.digest.StandardByteDigester;

import java.security.Provider;

/**
 * Utility class for creating digests without using a salt or iterating the hash function. This
 * means that digests created by this class will be compatible (and equivalent) to the ones which
 * could be created by the user by directly using a {@link java.security.MessageDigest} object.
 *
 * <p>This class can be thought of as convenience wrapper for {@link java.security.MessageDigest},
 * adding thread-safety and a more javabean-like interface to it. These two features enable a more
 * adequate use from an IoC container like Spring.
 *
 * <p>This class internally holds a {@link StandardByteDigester} configured this way:
 *
 * <ul>
 *   <li>Algorithm: <tt>MD5</tt> by default, but configurable.
 *   <li>Provider: Default JVM security provider, but configurable.
 *   <li>Salt size: <tt>0 bytes</tt>, no salt used.
 *   <li>Iterations: <tt>1</tt>, hash function will not be iterated.
 * </ul>
 *
 * <p>This class is <i>thread-safe</i>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class Digester {

  /** MD5 will be the default algorithm to be used if none is specified. */
  public static final String DEFAULT_ALGORITHM = "MD5";

  // The hash function will be applied only once
  private static final int ITERATIONS = 1;

  // No salt will be used
  private static final int SALT_SIZE_BYTES = 0;

  // The internal digester used
  private final StandardByteDigester digester;

  /**
   * Creates a new instance of <tt>Digester</tt>. It will use the default algorithm unless one is
   * specified with {@link #setAlgorithm(String)}.
   */
  public Digester() {
    super();
    this.digester = new StandardByteDigester();
    this.digester.setIterations(ITERATIONS);
    this.digester.setSaltSizeBytes(SALT_SIZE_BYTES);
  }

  /** Creates a new instance of <tt>Digester</tt>, specifying the algorithm to be used. */
  public Digester(final String algorithm) {
    super();
    this.digester = new StandardByteDigester();
    this.digester.setIterations(ITERATIONS);
    this.digester.setSaltSizeBytes(SALT_SIZE_BYTES);
    this.digester.setAlgorithm(algorithm);
  }

  /**
   * Creates a new instance of <tt>Digester</tt>, specifying the algorithm to be used.
   *
   * @since 1.3
   */
  public Digester(final String algorithm, final String providerName) {
    super();
    this.digester = new StandardByteDigester();
    this.digester.setIterations(ITERATIONS);
    this.digester.setSaltSizeBytes(SALT_SIZE_BYTES);
    this.digester.setAlgorithm(algorithm);
    this.digester.setProviderName(providerName);
  }

  /**
   * Creates a new instance of <tt>Digester</tt>, specifying the algorithm to be used.
   *
   * @since 1.3
   */
  public Digester(final String algorithm, final Provider provider) {
    super();
    this.digester = new StandardByteDigester();
    this.digester.setIterations(ITERATIONS);
    this.digester.setSaltSizeBytes(SALT_SIZE_BYTES);
    this.digester.setAlgorithm(algorithm);
    this.digester.setProvider(provider);
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
   * @throws AlreadyInitializedException if it has already been initialized, this is, if {@link
   *     #digest(byte[])} has been called at least once.
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
   *     #digest(byte[])} has been called at least once.
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
   *     #digest(byte[])} has been called at least once.
   */
  public void setProvider(final Provider provider) {
    this.digester.setProvider(provider);
  }

  /**
   * Creates a digest.
   *
   * @param binary the byte array to be digested.
   * @return the resulting digest.
   * @see StandardByteDigester#digest(byte[])
   */
  public byte[] digest(final byte[] binary) {
    return this.digester.digest(binary);
  }
}
