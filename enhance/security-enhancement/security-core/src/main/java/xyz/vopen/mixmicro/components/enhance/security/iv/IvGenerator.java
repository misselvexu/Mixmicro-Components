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
 * Common interface for all initialization vector (IV) generators which can be applied in encryption
 * operations.
 *
 * <p><b>Every implementation of this interface must be thread-safe</b>.
 *
 * @since 1.9.3
 * @author Hoki Torres
 */
public interface IvGenerator {

  /**
   * This method will be called for requesting the generation of a new IV of the specified length.
   *
   * @param lengthBytes the requested length for the IV.
   * @return the generated IV.
   */
  public byte[] generateIv(int lengthBytes);

  /**
   * Determines if the encrypted messages created with a specific IV generator will include
   * (prepended) the unencrypted IV itself, so that it can be used for decryption operations.
   *
   * <p>Generally, including the IV unencrypted in encryption results will be mandatory for randomly
   * generated IV, or for those generated in a non-predictable manner. Otherwise, decryption
   * operations will always fail. For fixed IV, inclusion will be optional (and in fact undesirable
   * if we want to hide the IV value).
   *
   * @return whether the plain (unencrypted) IV has to be included in encryption results or not.
   */
  public boolean includePlainIvInEncryptionResults();
}
