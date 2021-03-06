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
package xyz.vopen.mixmicro.components.enhance.security.util.binary;

import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.StandardPBEByteEncryptor;

/**
 * Utility class for easily performing high-strength encryption of binaries (byte arrays).
 *
 * <p>This class internally holds a {@link StandardPBEByteEncryptor} configured this way:
 *
 * <ul>
 *   <li>Algorithm: <tt>PBEWithMD5AndTripleDES</tt>.
 *   <li>Key obtention iterations: <tt>1000</tt>.
 * </ul>
 *
 * <p>The required steps to use it are:
 *
 * <ol>
 *   <li>Create an instance (using <tt>new</tt>).
 *   <li>Set a password (using <tt>{@link #setPassword(String)}</tt> or <tt>{@link
 *       #setPasswordCharArray(char[])}</tt>).
 *   <li>Perform the desired <tt>{@link #encrypt(byte[])}</tt> or <tt>{@link #decrypt(byte[])}</tt>
 *       operations.
 * </ol>
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * @since 1.2
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class StrongBinaryEncryptor implements BinaryEncryptor {

  // The internal encryptor
  private final StandardPBEByteEncryptor encryptor;

  /** Creates a new instance of <tt>StrongBinaryEncryptor</tt>. */
  public StrongBinaryEncryptor() {
    super();
    this.encryptor = new StandardPBEByteEncryptor();
    this.encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
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
   * Sets a password, as a char[].
   *
   * @since 1.8
   * @param password the password to be set.
   */
  public void setPasswordCharArray(final char[] password) {
    this.encryptor.setPasswordCharArray(password);
  }

  /**
   * Encrypts a byte array
   *
   * @param binary the byte array to be encrypted.
   * @see StandardPBEByteEncryptor#encrypt(byte[])
   */
  public byte[] encrypt(final byte[] binary) {
    return this.encryptor.encrypt(binary);
  }

  /**
   * Decrypts a byte array.
   *
   * @param encryptedBinary the byte array to be decrypted.
   * @see StandardPBEByteEncryptor#decrypt(byte[])
   */
  public byte[] decrypt(final byte[] encryptedBinary) {
    return this.encryptor.decrypt(encryptedBinary);
  }
}
