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
package xyz.vopen.mixmicro.components.enhance.security.util.numeric;

import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.StandardPBEBigIntegerEncryptor;
import xyz.vopen.mixmicro.components.enhance.security.iv.RandomIvGenerator;

import java.math.BigInteger;

/**
 * Utility class for easily performing normal-strength encryption of BigInteger objects.
 *
 * <p>This class internally holds a {@link StandardPBEBigIntegerEncryptor} configured this way:
 *
 * <ul>
 *   <li>Algorithm: <tt>PBEWithHMACSHA512AndAES_256</tt>.
 *   <li>Key obtention iterations: <tt>1000</tt>.
 * </ul>
 *
 * <p>The required steps to use it are:
 *
 * <ol>
 *   <li>Create an instance (using <tt>new</tt>).
 *   <li>Set a password (using <tt>{@link #setPassword(String)}</tt> or <tt>{@link
 *       #setPasswordCharArray(char[])}</tt>).
 *   <li>Perform the desired <tt>{@link #encrypt(BigInteger)}</tt> or <tt>{@link
 *       #decrypt(BigInteger)}</tt> operations.
 * </ol>
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * @since 1.9.3
 * @author Hoki Torres
 */
public final class AES256IntegerNumberEncryptor implements IntegerNumberEncryptor {

  // The internal encryptor
  private final StandardPBEBigIntegerEncryptor encryptor;

  /** Creates a new instance of <tt>StrongIntegerNumberEncryptor</tt>. */
  public AES256IntegerNumberEncryptor() {
    super();
    this.encryptor = new StandardPBEBigIntegerEncryptor();
    this.encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
    this.encryptor.setIvGenerator(new RandomIvGenerator());
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
   * @param password the password to be set.
   */
  public void setPasswordCharArray(final char[] password) {
    this.encryptor.setPasswordCharArray(password);
  }

  /**
   * Encrypts a number
   *
   * @param number the number to be encrypted.
   * @see StandardPBEBigIntegerEncryptor#encrypt(BigInteger)
   */
  public BigInteger encrypt(final BigInteger number) {
    return this.encryptor.encrypt(number);
  }

  /**
   * Decrypts a number.
   *
   * @param encryptedNumber the number to be decrypted.
   * @see StandardPBEBigIntegerEncryptor#decrypt(BigInteger)
   */
  public BigInteger decrypt(final BigInteger encryptedNumber) {
    return this.encryptor.decrypt(encryptedNumber);
  }
}
