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
package xyz.vopen.mixmicro.components.enhance.security.iv;

/**
 * This implementation of {@link IvGenerator} always returns a initialization vector (IV) of length
 * 0.
 *
 * <p>This class is <i>thread-safe</i>.
 *
 * @since 1.9.3
 * @author Hoki Torres
 */
public class NoIvGenerator implements IvGenerator {

  /** Creates a new instance of <tt>NoIvGenerator</tt> */
  public NoIvGenerator() {
    super();
  }

  /**
   * Return IV with 0 byte length.
   *
   * @param lengthBytes length in bytes.
   * @return the generated IV.
   */
  public byte[] generateIv(final int lengthBytes) {
    return new byte[0];
  }

  /**
   * As this IV generator provides an empty vector, its inclusion unencrypted in encryption results
   * is not necessary.
   *
   * @return false
   */
  public boolean includePlainIvInEncryptionResults() {
    return false;
  }
}
