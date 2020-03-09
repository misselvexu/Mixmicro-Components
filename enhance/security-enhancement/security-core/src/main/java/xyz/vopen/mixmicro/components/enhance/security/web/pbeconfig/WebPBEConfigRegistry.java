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
package xyz.vopen.mixmicro.components.enhance.security.web.pbeconfig;

import xyz.vopen.mixmicro.components.enhance.security.encryption.pbe.config.WebPBEConfig;
import xyz.vopen.mixmicro.components.enhance.security.exceptions.EncryptionInitializationException;

import java.util.*;

/**
 * Registry for {@link WebPBEConfig} objects. <b>This class is intended for internal use only, and
 * should not be accessed from the user's code.</b>
 *
 * @since 1.3
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class WebPBEConfigRegistry {

  private final Set names = new HashSet();
  private final List configs = new ArrayList();
  private boolean webConfigurationDone = false;

  private static final WebPBEConfigRegistry instance = new WebPBEConfigRegistry();

  public static WebPBEConfigRegistry getInstance() {
    return instance;
  }

  private WebPBEConfigRegistry() {
    super();
  }

  public synchronized void registerConfig(final WebPBEConfig config) {
    if (this.webConfigurationDone) {
      throw new EncryptionInitializationException(
          "Cannot register: Web configuration is already done");
    }
    // Avoid duplication of encryptors because of the initialization
    // class being called more than once.
    if (!this.names.contains(config.getName())) {
      this.configs.add(config);
      this.names.add(config);
    }
  }

  public synchronized List getConfigs() {
    return Collections.unmodifiableList(this.configs);
  }

  public boolean isWebConfigurationDone() {
    return (this.webConfigurationDone || (this.configs.size() == 0));
  }

  public void setWebConfigurationDone(final boolean configurationDone) {
    this.webConfigurationDone = configurationDone;
  }
}
