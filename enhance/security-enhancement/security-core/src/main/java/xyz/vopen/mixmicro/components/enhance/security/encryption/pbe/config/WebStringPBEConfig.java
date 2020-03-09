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
 * Implementation for {@link StringPBEConfig} which can be used from the Web PBE Config
 * infrastructure (Filter + Servlet) to set the password for an encryptor from the web at
 * initialization time.
 *
 * <p>For an encryptor to be assigned a password from the web, it only has to be assigned a
 * WebPBEConfig object, which must be initialized with <b>both</b> a unique name an a validation
 * word. The name will identify the config object (and thus the encryptor) and the validation word
 * will make sure that only an authorized person (for example, the application deployer) sets the
 * passwords.
 *
 * <p>As this class extends {@link SimplePBEConfig}, parameter values can be also set with the usual
 * <tt>setX</tt> methods.
 *
 * <p>For any of the configuration parameters, if its <tt>setX</tt> method is not called, a
 * <tt>null</tt> value will be returned by the corresponding <tt>getX</tt> method.
 *
 * @since 1.3
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class WebStringPBEConfig extends WebPBEConfig implements StringPBEConfig {

  private String stringOutputType = null;

  /** Creates a new <tt>WebStringPBEConfig</tt> instance. */
  public WebStringPBEConfig() {
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
   * @param stringOutputType the string output type.
   */
  public void setStringOutputType(final String stringOutputType) {
    this.stringOutputType = CommonUtils.getStandardStringOutputType(stringOutputType);
  }

  public String getStringOutputType() {
    return this.stringOutputType;
  }
}
