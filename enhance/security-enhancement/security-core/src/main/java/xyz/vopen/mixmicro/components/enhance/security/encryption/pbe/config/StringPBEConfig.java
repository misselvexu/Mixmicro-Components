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

import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Common interface for config classes applicable to {@link
 * StandardPBEStringEncryptor} objects. This interface extends {@link
 * PBEConfig} to add config parameters specific to String encryption.
 *
 * <p>This interface lets the user create new <tt>PBEConfig</tt> classes which retrieve values for
 * this parameters from different (and maybe more secure) sources (remote servers, LDAP, other
 * databases...), and do this transparently for the encryptor object.
 *
 * <p>The config objects passed to an encryptor <u>will only be queried once</u> for each
 * configuration parameter, and this will happen during the initialization of the encryptor object.
 *
 * <p>For a default implementation, see {@link SimpleStringPBEConfig}.
 *
 * @since 1.3
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface StringPBEConfig extends PBEConfig {

  /**
   * This parameter lets the user specify the form in which String output will be encoded. Available
   * encoding types are:
   *
   * <ul>
   *   <li><tt><b>base64</b></tt> (default)
   *   <li><tt><b>hexadecimal</b></tt>
   * </ul>
   *
   * @return The name of the encoding type for String output
   */
  public String getStringOutputType();
}
