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
package xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config;

import xyz.vopen.mixmicro.components.enhance.security.iv.IvGenerator;
import xyz.vopen.mixmicro.components.enhance.security.salt.SaltGenerator;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.StandardPBEBigDecimalEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.StandardPBEBigIntegerEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.StandardPBEByteEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.StandardPBEStringEncryptor;

import java.security.Provider;

/**
 * Common interface for config classes applicable to {@link
 * StandardPBEByteEncryptor}, {@link
 * StandardPBEStringEncryptor}, {@link
 * StandardPBEBigIntegerEncryptor} or {@link
 * StandardPBEBigDecimalEncryptor} objects.
 *
 * <p>This interface lets the user create new <tt>PBEConfig</tt> classes which retrieve values for
 * this parameters from different (and maybe more secure) sources (remote servers, LDAP, other
 * databases...), and do this transparently for the encryptor object.
 *
 * <p>The config objects passed to an encryptor <u>will only be queried once</u> for each
 * configuration parameter, and this will happen during the initialization of the encryptor object.
 *
 * <p>For a default implementation, see {@link SimplePBEConfig}.
 *
 * @since 1.0
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface PBEConfig {

  /**
   * Returns the algorithm to be used for encryption, like <tt>PBEWithMD5AndDES</tt>.
   *
   * <p>This algorithm has to be supported by the specified JCE provider (or the default one if no
   * provider has been specified) and, if the provider supports it, you can also specify <i>mode</i>
   * and <i>padding</i> for it, like <tt>ALGORITHM/MODE/PADDING</tt>.
   *
   * @return the name of the algorithm to be used.
   */
  public String getAlgorithm();

  /**
   * Returns the password to be used.
   *
   * <p><b>There is no default value for password</b>, so not setting this parameter either from a
   * {@link PBEConfig} object or from a call to
   * <tt>setPassword</tt> will result in an EncryptionInitializationException being thrown during
   * initialization.
   *
   * @return the password to be used.
   */
  public String getPassword();

  /**
   * Returns the number of hashing iterations applied to obtain the encryption key.
   *
   * <p>This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"
   * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   *
   * @return the number of iterations
   */
  public Integer getKeyObtentionIterations();

  /**
   * Returns a {@link SaltGenerator} implementation to be used by the encryptor.
   *
   * <p>If this method returns null, the encryptor will ignore the config object when deciding the
   * salt generator to be used.
   *
   * @return the salt generator, or null if this object will not want to set a specific
   *     SaltGenerator implementation.
   */
  public SaltGenerator getSaltGenerator();

  /**
   * Returns a {@link IvGenerator} implementation to be used by the encryptor.
   *
   * <p>If this method returns null, the encryptor will ignore the config object when deciding the
   * IV generator to be used.
   *
   * @return the IV generator, or null if this object will not want to set a specific IvGenerator
   *     implementation.
   */
  public IvGenerator getIvGenerator();

  /**
   * Returns the name of the <tt>java.security.Provider</tt> implementation to be used by the
   * encryptor for obtaining the encryption algorithm. This provider must have been registered
   * beforehand.
   *
   * <p>If this method returns null, the encryptor will ignore this parameter when deciding the name
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
   * Returns the <tt>java.security.Provider</tt> implementation object to be used by the encryptor
   * for obtaining the encryption algorithm.
   *
   * <p>If this method returns null, the encryptor will ignore this parameter when deciding the
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
   * Get the size of the pool of encryptors to be created.
   *
   * <p><b>This parameter will be ignored if used with a non-pooled encryptor</b>.
   *
   * @since 1.7
   * @return the size of the pool to be used if this configuration is used with a pooled encryptor
   */
  public Integer getPoolSize();
}
