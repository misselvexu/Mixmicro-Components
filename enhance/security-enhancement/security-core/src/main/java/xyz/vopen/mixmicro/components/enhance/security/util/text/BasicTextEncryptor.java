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
package xyz.vopen.mixmicro.components.enhance.security.util.text;

import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Utility class for easily performing normal-strength encryption of texts.
 *
 * <p>This class internally holds a {@link StandardPBEStringEncryptor} configured this way:
 *
 * <ul>
 *   <li>Algorithm: <tt>PBEWithMD5AndDES</tt>.
 *   <li>Key obtention iterations: <tt>1000</tt>.
 * </ul>
 *
 * <p>The required steps to use it are:
 *
 * <ol>
 *   <li>Create an instance (using <tt>new</tt>).
 *   <li>Set a password (using <tt>{@link #setPassword(String)}</tt> or <tt>{@link
 *       #setPasswordCharArray(char[])}</tt>).
 *   <li>Perform the desired <tt>{@link #encrypt(String)}</tt> or <tt>{@link #decrypt(String)}</tt>
 *       operations.
 * </ol>
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class BasicTextEncryptor implements TextEncryptor {

  // The internal encryptor
  private final StandardPBEStringEncryptor encryptor;

  /** Creates a new instance of <tt>BasicTextEncryptor</tt>. */
  public BasicTextEncryptor() {
    super();
    this.encryptor = new StandardPBEStringEncryptor();
    this.encryptor.setAlgorithm("PBEWithMD5AndDES");
  }

  /**
   * Sets a password.
   *
   * @param password the password to be set.
   */
  public void setPassword(final String password) {
    this.encryptor.setPassword(password);
  }

  /**
   * Sets a password, as a char[]
   *
   * @since 1.8
   * @param password the password to be set.
   */
  public void setPasswordCharArray(final char[] password) {
    this.encryptor.setPasswordCharArray(password);
  }

  /**
   * Encrypts a message.
   *
   * @param message the message to be encrypted.
   * @see StandardPBEStringEncryptor#encrypt(String)
   */
  @Override
  public String encrypt(final String message) {
    return this.encryptor.encrypt(message);
  }

  /**
   * Decrypts a message.
   *
   * @param encryptedMessage the message to be decrypted.
   * @see StandardPBEStringEncryptor#decrypt(String)
   */
  @Override
  public String decrypt(final String encryptedMessage) {
    return this.encryptor.decrypt(encryptedMessage);
  }
}
