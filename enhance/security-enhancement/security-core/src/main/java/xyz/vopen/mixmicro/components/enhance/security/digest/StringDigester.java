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
package xyz.vopen.mixmicro.components.enhance.security.digest;

/**
 * Common interface for all digesters which receive a String message and return a String digest.
 *
 * <p>For a default implementation, see {@link StandardStringDigester}.
 *
 * @since 1.0
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface StringDigester {

  /**
   * Create a digest of the input message.
   *
   * @param message the message to be digested
   * @return the digest
   */
  public String digest(String message);

  /**
   * Check whether a message matches a digest, managing aspects like salt, hashing iterations, etc.
   * (if applicable).
   *
   * @param message the message to check
   * @param digest the digest to check
   * @return TRUE if the message matches the digest, FALSE if not.
   */
  public boolean matches(String message, String digest);
}
