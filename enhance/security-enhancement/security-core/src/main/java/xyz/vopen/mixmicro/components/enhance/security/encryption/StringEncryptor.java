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
package xyz.vopen.mixmicro.components.enhance.security.encryption;

/**
 * Common interface for all Encryptors which receive a String message and return a String result.
 *
 * @since 1.0
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface StringEncryptor {

  /**
   * Encrypt the input message
   *
   * @param message the message to be encrypted
   * @return the result of encryption
   */
  public String encrypt(String message);

  /**
   * Decrypt an encrypted message
   *
   * @param encryptedMessage the encrypted message to be decrypted
   * @return the result of decryption
   */
  public String decrypt(String encryptedMessage);
}
