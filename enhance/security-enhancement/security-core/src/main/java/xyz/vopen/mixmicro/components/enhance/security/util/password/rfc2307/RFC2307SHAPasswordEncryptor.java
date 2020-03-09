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
package xyz.vopen.mixmicro.components.enhance.security.util.password.rfc2307;

import xyz.vopen.mixmicro.components.enhance.security.digest.StandardStringDigester;
import xyz.vopen.mixmicro.components.enhance.security.util.password.PasswordEncryptor;

/**
 * Utility class for easily performing password digesting and checking according to {SHA}, a
 * password encryption scheme defined in RFC2307 and commonly found in LDAP systems.
 *
 * <p>This class internally holds a {@link StandardStringDigester} configured this way:
 *
 * <ul>
 *   <li>Algorithm: <tt>SHA-1</tt>.
 *   <li>Salt size: <tt>0 bytes</tt> (no salt).
 *   <li>Iterations: <tt>1</tt> (no hash iteration).
 *   <li>Prefix: <tt>{SHA}</tt>.
 * </ul>
 *
 * <p>This class is <i>thread-safe</i>
 *
 * @since 1.7
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class RFC2307SHAPasswordEncryptor implements PasswordEncryptor {

  // The internal digester used
  private final StandardStringDigester digester;

  /** Creates a new instance of <tt>RFC2307SHAPasswordEncryptor</tt> */
  public RFC2307SHAPasswordEncryptor() {
    super();
    this.digester = new StandardStringDigester();
    this.digester.setAlgorithm("SHA-1");
    this.digester.setIterations(1);
    this.digester.setSaltSizeBytes(0);
    this.digester.setPrefix("{SHA}");
  }

  /**
   * Sets the the form in which String output will be encoded. Available encoding types are:
   *
   * <ul>
   *   <li><tt><b>base64</b></tt> (default)
   *   <li><tt><b>hexadecimal</b></tt>
   * </ul>
   *
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
