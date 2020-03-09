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

import java.util.Arrays;

/**
 * This implementation of {@link SaltGenerator} always returns a salt of the required length, filled
 * with <i>zero</i> bytes.
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * @since 1.4
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ZeroSaltGenerator implements SaltGenerator {

  /** Creates a new instance of <tt>ZeroSaltGenerator</tt> */
  public ZeroSaltGenerator() {
    super();
  }

  /**
   * Return salt with the specified byte length. This will return an array of <i>zero</i> bytes,
   * with the specified length.
   *
   * @param lengthBytes length in bytes.
   * @return the generated salt.
   */
  public byte[] generateSalt(final int lengthBytes) {
    final byte[] result = new byte[lengthBytes];
    Arrays.fill(result, (byte) 0);
    return result;
  }

  /**
   * As this salt generator provides a predictable salt, its inclusion unencrypted in encryption
   * results is not necessary, and in fact not desirable (so that it remains hidden).
   *
   * @return false
   */
  public boolean includePlainSaltInEncryptionResults() {
    return false;
  }
}
