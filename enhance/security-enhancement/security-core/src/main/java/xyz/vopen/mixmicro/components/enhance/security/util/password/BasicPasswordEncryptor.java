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

import xyz.vopen.mixmicro.components.enhance.security.digest.StandardStringDigester;

/**
 * Utility class for easily performing password digesting and checking.
 *
 * <p>This class internally holds a {@link StandardStringDigester} configured this way:
 *
 * <ul>
 *   <li>Algorithm: <tt>MD5</tt>.
 *   <li>Salt size: <tt>8 bytes</tt>.
 *   <li>Iterations: <tt>1000</tt>.
 * </ul>
 *
 * <p>The required steps to use it are:
 *
 * <ol>
 *   <li>Create an instance (using <tt>new</tt>).
 *   <li>Perform the desired <tt>{@link #encryptPassword(String)}</tt> or <tt>{@link
 *       #checkPassword(String, String)}</tt> operations.
 * </ol>
 *
 * <p>This class is <i>thread-safe</i>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class BasicPasswordEncryptor implements PasswordEncryptor {

  // The internal digester used
  private final StandardStringDigester digester;

  /** Creates a new instance of <tt>BasicPasswordEncryptor</tt> */
  public BasicPasswordEncryptor() {
    super();
    this.digester = new StandardStringDigester();
    this.digester.initialize();
  }

  /**
   * Encrypts (digests) a password.
   *
   * @param password the password to be encrypted.
   * @return the resulting digest.
   * @see StandardStringDigester#digest(String)
   */
  @Override
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
  @Override
  public boolean checkPassword(final String plainPassword, final String encryptedPassword) {
    return this.digester.matches(plainPassword, encryptedPassword);
  }
}
