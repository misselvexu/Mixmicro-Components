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

import java.math.BigInteger;

/**
 * Common interface for all util classes aimed at integer number encryption.
 *
 * @since 1.2
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface IntegerNumberEncryptor {

  /**
   * Encrypts a BigInteger
   *
   * @param number the number to be encrypted.
   */
  public BigInteger encrypt(BigInteger number);

  /**
   * Decrypts a BigInteger.
   *
   * @param encryptedNumber the number to be decrypted.
   */
  public BigInteger decrypt(BigInteger encryptedNumber);
}
