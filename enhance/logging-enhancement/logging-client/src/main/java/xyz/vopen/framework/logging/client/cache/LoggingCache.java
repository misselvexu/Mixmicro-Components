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

package xyz.vopen.framework.logging.client.cache;

import xyz.vopen.framework.logging.client.MinBoxLoggingException;
import xyz.vopen.framework.logging.core.MixmicroLog;

import java.util.List;

/**
 * Mixmicro Logging Cache
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface LoggingCache {
  /**
   * Cache Single MinBoxLog
   *
   * @param log MinBoxLog
   * @throws MinBoxLoggingException Logging Exception
   */
  void cache(MixmicroLog log) throws MinBoxLoggingException;

  /**
   * Get Any One MinBoxLog
   *
   * @return MinBoxLog
   * @throws MinBoxLoggingException Logging Exception
   */
  MixmicroLog getAnyOne() throws MinBoxLoggingException;

  /**
   * Gets the specified number of MinBoxLog
   *
   * @param count get count number
   * @return MinBoxLog Collection
   * @throws MinBoxLoggingException Logging Exception
   */
  List<MixmicroLog> getLogs(int count) throws MinBoxLoggingException;

  /**
   * Gets All Of MinBoxLog
   *
   * @return MinBoxLog Collection
   * @throws MinBoxLoggingException Logging Exception
   */
  List<MixmicroLog> getAll() throws MinBoxLoggingException;
}
