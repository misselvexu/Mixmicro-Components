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
package xyz.vopen.mixmicro.components.enhance.security.salt;

import xyz.vopen.mixmicro.components.enhance.security.commons.CommonUtils;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.EncryptionInitializationException;

import java.io.UnsupportedEncodingException;

/**
 * This implementation of {@link SaltGenerator} always returns a fixed salt set by the user as a
 * String, which is returned as salt bytes using the specified charset for conversion (UTF-8 by
 * default).
 *
 * <p>If the requested salt has a size in bytes smaller than the specified salt, the first n bytes
 * are returned. If it is larger, an exception is thrown.
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * @since 1.2
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @deprecated Deprecated in 1.9.2 in favour of {@link StringFixedSaltGenerator}, which implements
 *     the new {@link FixedSaltGenerator} interface and therefore is able to benefit from the
 *     performance improvements associated with it. This class will be removed in 1.10.0 (or 2.0.0).
 */
public class FixedStringSaltGenerator implements SaltGenerator {

  private static final String DEFAULT_CHARSET = "UTF-8";

  private String salt = null;
  private String charset = DEFAULT_CHARSET;

  private byte[] saltBytes = null;

  /** Creates a new instance of <tt>FixedStringSaltGenerator</tt> */
  public FixedStringSaltGenerator() {
    super();
  }

  /**
   * Sets the salt to be returned.
   *
   * @param salt the specified salt.
   */
  public synchronized void setSalt(final String salt) {
    CommonUtils.validateNotNull(salt, "Salt cannot be set null");
    this.salt = salt;
  }

  /**
   * Sets the charset to be applied to the salt for conversion into bytes.
   *
   * @param charset the specified charset
   */
  public synchronized void setCharset(final String charset) {
    CommonUtils.validateNotNull(charset, "Charset cannot be set null");
    this.charset = charset;
  }

  /**
   * Return salt with the specified byte length.
   *
   * @param lengthBytes length in bytes.
   * @return the generated salt.
   */
  public byte[] generateSalt(final int lengthBytes) {
    if (this.salt == null) {
      throw new EncryptionInitializationException("Salt has not been set");
    }
    if (this.saltBytes == null) {
      try {
        this.saltBytes = this.salt.getBytes(this.charset);
      } catch (UnsupportedEncodingException e) {
        throw new EncryptionInitializationException("Invalid charset specified: " + this.charset);
      }
    }
    if (this.saltBytes.length < lengthBytes) {
      throw new EncryptionInitializationException("Requested salt larger than set");
    }
    final byte[] generatedSalt = new byte[lengthBytes];
    System.arraycopy(this.saltBytes, 0, generatedSalt, 0, lengthBytes);
    return generatedSalt;
  }

  /**
   * As this salt generator provides a fixed salt, its inclusion unencrypted in encryption results
   * is not necessary, and in fact not desirable (so that it remains hidden).
   *
   * @return false
   */
  public boolean includePlainSaltInEncryptionResults() {
    return false;
  }
}
