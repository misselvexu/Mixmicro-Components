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
package xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config;

/**
 * Common interface for all PBEConfig implementations that store passwords as char[] instead of
 * String and also allow this passwords to be set as char[] instead of Strings.
 *
 * @since 1.8
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface PBECleanablePasswordConfig {

  /**
   * Return the password set, as a char array.
   *
   * <p><b>Important</b>: the returned array MUST BE A COPY of the one stored in the configuration
   * object. The caller of this method is therefore be responsible for cleaning this resulting
   * char[].
   *
   * @since 1.8
   */
  public char[] getPasswordCharArray();

  /**
   * Clean the password stored in this configuration object.
   *
   * <p>A common implementation of this <i>cleaning</i> operation consists of iterating the array of
   * chars and setting each of its positions to <tt>(char)0</tt>.
   *
   * @since 1.8
   */
  public void cleanPassword();
}
