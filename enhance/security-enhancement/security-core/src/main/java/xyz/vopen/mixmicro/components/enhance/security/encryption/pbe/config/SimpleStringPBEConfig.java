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

import xyz.vopen.mixmicro.components.enhance.security.commons.CommonUtils;

/**
 * Bean implementation for {@link StringPBEConfig}. This class allows the values for the
 * configuration parameters to be set via "standard" <tt>setX</tt> methods.
 *
 * <p>For any of the configuration parameters, if its <tt>setX</tt> method is not called, a
 * <tt>null</tt> value will be returned by the corresponding <tt>getX</tt> method.
 *
 * @since 1.3
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SimpleStringPBEConfig extends SimplePBEConfig implements StringPBEConfig {

  private String stringOutputType = null;

  /** Creates a new <tt>SimpleStringPBEConfig</tt> instance. */
  public SimpleStringPBEConfig() {
    super();
  }

  /**
   * Sets the the form in which String output will be encoded. Available encoding types are:
   *
   * <ul>
   *   <li><tt><b>base64</b></tt> (default)
   *   <li><tt><b>hexadecimal</b></tt>
   * </ul>
   *
   * <p>If not set, null will be returned.
   *
   * <p>Determines the result of: {@link #getStringOutputType()}
   *
   * @param stringOutputType the string output type.
   */
  public void setStringOutputType(final String stringOutputType) {
    this.stringOutputType = CommonUtils.getStandardStringOutputType(stringOutputType);
  }

  public String getStringOutputType() {
    return this.stringOutputType;
  }
}
