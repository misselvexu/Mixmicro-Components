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

package xyz.vopen.framework.logging.client.notice;

import xyz.vopen.framework.logging.core.MixmicroLog;
import org.springframework.core.Ordered;

/**
 * Mixmicro Boot Logging Local Notice
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface LoggingNotice extends Ordered {
  /**
   * Local Notice Mixmicro Boot Log Instance
   *
   * @param mixmicroLog Log object for each HTTP request
   */
  void notice(MixmicroLog mixmicroLog);
}
