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
package xyz.vopen.mixmicro.components.enhance.security.digest.config;

import xyz.vopen.mixmicro.components.enhance.security.salt.SaltGenerator;
import xyz.vopen.mixmicro.components.enhance.security.digest.PooledByteDigester;
import xyz.vopen.mixmicro.components.enhance.security.digest.PooledStringDigester;
import xyz.vopen.mixmicro.components.enhance.security.digest.StandardByteDigester;
import xyz.vopen.mixmicro.components.enhance.security.digest.StandardStringDigester;

import java.security.Provider;

/**
 * Common interface for config classes applicable to {@link StandardByteDigester},
 * {@link StandardStringDigester}, {@link PooledByteDigester} or
 * {@link PooledStringDigester} objects.
 *
 * <p>This interface lets the user create new <tt>DigesterConfig</tt> classes which retrieve values
 * for this parameters from different (and maybe more secure) sources (remote servers, LDAP, other
 * databases...), and do this transparently for the digester object.
 *
 * <p>The config objects passed to a digester <u>will only be queried once</u> for each
 * configuration parameter, and this will happen during the initialization of the digester object.
 *
 * <p>For a default implementation, see {@link SimpleDigesterConfig}.
 *
 * @since 1.0
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface DigesterConfig {

  /**
   * Returns the name of an algorithm to be used for hashing, like "MD5" or "SHA-1".
   *
   * <p>This algorithm has to be supported by your Java Virtual Machine, and it should be allowed as
   * an algorithm for creating java.security.MessageDigest instances.
   *
   * <p>If this method returns null, the digester will ignore the config object when deciding the
   * algorithm to be used.
   *
   * @return the name of the algorithm to be used, or null if this object will not want to set an
   *     algorithm. See Appendix A in the <a target="_blank"
   *     href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java
   *     Cryptography Architecture API Specification & Reference</a> for information about standard
   *     algorithm names.
   */
  public String getAlgorithm();

  /**
   * Returns the size of the salt to be used to compute the digest. This mechanism is explained in
   * <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5:
   * Password-Based Cryptography Standard</a>.
   *
   * <p>If salt size is set to zero, then no salt will be used.
   *
   * <p>If this method returns null, the digester will ignore the config object when deciding the
   * size of the salt to be used.
   *
   * @return the size of the salt to be used, in bytes, or null if this object will not want to set
   *     a size for salt.
   */
  public Integer getSaltSizeBytes();

  /**
   * Returns the number of times the hash function will be applied recursively. <br>
   * The hash function will be applied to its own results as many times as specified:
   * <i>h(h(...h(x)...))</i>
   *
   * <p>This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"
   * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   *
   * <p>If this method returns null, the digester will ignore the config object when deciding the
   * number of hashing iterations.
   *
   * @return the number of iterations, or null if this object will not want to set the number of
   *     iterations.
   */
  public Integer getIterations();

  /**
   * Returns a {@link SaltGenerator} implementation to be used by the digester.
   *
   * <p>If this method returns null, the digester will ignore the config object when deciding the
   * salt generator to be used.
   *
   * @since 1.2
   * @return the salt generator, or null if this object will not want to set a specific
   *     SaltGenerator implementation.
   */
  public SaltGenerator getSaltGenerator();

  /**
   * Returns the name of the <tt>java.security.Provider</tt> implementation to be used by the
   * digester for obtaining the digest algorithm. This provider must have been registered
   * beforehand.
   *
   * <p>If this method returns null, the digester will ignore this parameter when deciding the name
   * of the security provider to be used.
   *
   * <p>If this method does not return null, and neither does {@link #getProvider()},
   * <tt>providerName</tt> will be ignored, and the provider object returned by
   * <tt>getProvider()</tt> will be used.
   *
   * @since 1.3
   * @return the name of the security provider to be used.
   */
  public String getProviderName();

  /**
   * Returns the <tt>java.security.Provider</tt> implementation object to be used by the digester
   * for obtaining the digest algorithm.
   *
   * <p>If this method returns null, the digester will ignore this parameter when deciding the
   * security provider object to be used.
   *
   * <p>If this method does not return null, and neither does {@link #getProviderName()},
   * <tt>providerName</tt> will be ignored, and the provider object returned by
   * <tt>getProvider()</tt> will be used.
   *
   * <p>The provider returned by this method <b>does not need to be registered beforehand<b>, and
   * its use will not result in its being registered.
   *
   * @since 1.3
   * @return the security provider object to be asked for the digest algorithm.
   */
  public Provider getProvider();

  /**
   * Returns <tt>Boolean.TRUE</tt> if the salt bytes are to be appended after the message ones
   * before performing the digest operation on the whole. The default behaviour is to insert those
   * bytes before the message bytes, but setting this configuration item to <tt>true</tt> allows
   * compatibility with some external systems and specifications (e.g. LDAP {SSHA}).
   *
   * @since 1.7
   * @return whether salt will be appended after the message before applying the digest operation on
   *     the whole, instead of inserted before it (which is the default). If null is returned, the
   *     default behaviour will be applied.
   */
  public Boolean getInvertPositionOfSaltInMessageBeforeDigesting();

  /**
   * Returns <tt>Boolean.TRUE</tt> if the plain (not hashed) salt bytes are to be appended after the
   * digest operation result bytes. The default behaviour is to insert them before the digest
   * result, but setting this configuration item to <tt>true</tt> allows compatibility with some
   * external systems and specifications (e.g. LDAP {SSHA}).
   *
   * @since 1.7
   * @return whether plain salt will be appended after the digest operation result instead of
   *     inserted before it (which is the default). If null is returned, the default behaviour will
   *     be applied.
   */
  public Boolean getInvertPositionOfPlainSaltInEncryptionResults();

  /**
   * Returns <tt>Boolean.TRUE</tt> if digest matching operations will allow matching digests with a
   * salt size different to the one configured in the "saltSizeBytes" property. This is possible
   * because digest algorithms will produce a fixed-size result, so the remaining bytes from the
   * hashed input will be considered salt.
   *
   * <p>This will allow the digester to match digests produced in environments which do not
   * establish a fixed salt size as standard (for example, SSHA password encryption in LDAP
   * systems).
   *
   * <p>The value of this property will <b>not</b> affect the creation of digests, which will always
   * have a salt of the size established by the "saltSizeBytes" property. It will only affect digest
   * matching.
   *
   * <p>Setting this property to <tt>true</tt> is not compatible with {@link SaltGenerator}
   * implementations which return false for their {@link
   * SaltGenerator#includePlainSaltInEncryptionResults()} property.
   *
   * <p>Also, be aware that some algorithms or algorithm providers might not support knowing the
   * size of the digests beforehand, which is also incompatible with a lenient behaviour.
   *
   * <p><b>Default is <tt>FALSE</tt></b>.
   *
   * @since 1.7
   * @return whether the digester will allow matching of digests with different salt sizes than
   *     established or not (default is false).
   */
  public Boolean getUseLenientSaltSizeCheck();

  /**
   * Get the size of the pool of digesters to be created.
   *
   * <p><b>This parameter will be ignored if used with a non-pooled digester</b>.
   *
   * @since 1.7
   * @return the size of the pool to be used if this configuration is used with a pooled digester
   */
  public Integer getPoolSize();
}
