/*
 * Copyright (c) 2018 VOPEN.XYZ
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package xyz.vopen.framework.logging.client.admin.discovery.support;

import org.apache.tomcat.util.codec.binary.Base64;
import xyz.vopen.framework.logging.client.admin.discovery.LoggingAdminDiscovery;

/**
 * Mixmicro Boot Logging Abstract Admin Discovery
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class LoggingAbstractAdminDiscovery implements LoggingAdminDiscovery {
  /** basic auth */
  private static final String BASIC_AUTH = "Basic %s";

  /**
   * get basic auth base64 string
   *
   * @param basicInfo basic info
   * @return basic auth base64 string
   */
  protected String getBasicBase64(String basicInfo) {
    String basicBase64 = Base64.encodeBase64String(basicInfo.getBytes());
    return String.format(BASIC_AUTH, basicBase64);
  }
}
