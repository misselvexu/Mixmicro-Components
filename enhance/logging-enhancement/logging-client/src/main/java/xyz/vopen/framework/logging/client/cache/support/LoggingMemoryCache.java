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

package xyz.vopen.framework.logging.client.cache.support;

import xyz.vopen.framework.logging.client.MinBoxLoggingException;
import xyz.vopen.framework.logging.client.cache.LoggingCache;
import xyz.vopen.framework.logging.core.MixmicroLog;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Mixmicro Boot Logging Memory Away Cache
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingMemoryCache implements LoggingCache {
  /** Cache MinBoxLog Map For Batch Report */
  private static final ConcurrentMap<String, MixmicroLog> CACHE_LOGS = new ConcurrentHashMap();

  /**
   * Cache Single MinBoxLog
   *
   * @param log MinBoxLog
   * @throws MinBoxLoggingException Logging Exception
   */
  @Override
  public void cache(MixmicroLog log) throws MinBoxLoggingException {
    if (!ObjectUtils.isEmpty(log)) {
      CACHE_LOGS.put(UUID.randomUUID().toString(), log);
    }
  }

  /**
   * Get Any One MinBoxLog
   *
   * @return MinBoxLog
   * @throws MinBoxLoggingException Logging Exception
   */
  @Override
  public MixmicroLog getAnyOne() throws MinBoxLoggingException {
    List<MixmicroLog> logs = get(0);
    return logs.size() > 0 ? logs.get(0) : null;
  }

  /**
   * Gets the specified number of MinBoxLog
   *
   * @param count get count number
   * @return MinBoxLog Collection
   * @throws MinBoxLoggingException Logging Exception
   */
  @Override
  public List<MixmicroLog> getLogs(int count) throws MinBoxLoggingException {
    if (CACHE_LOGS.size() >= count) {
      return get(count);
    }
    return null;
  }

  /**
   * Gets All Of MinBoxLog
   *
   * @return MinBoxLog Collection
   * @throws MinBoxLoggingException Logging Exception
   */
  @Override
  public List<MixmicroLog> getAll() throws MinBoxLoggingException {
    return get(null);
  }

  /**
   * Get ApiBootLogs
   *
   * @param count get count number
   * @return MinBoxLog Collection
   * @throws MinBoxLoggingException Logging Exception
   */
  private List<MixmicroLog> get(Integer count) throws MinBoxLoggingException {
    List<MixmicroLog> logs = new ArrayList();
    Iterator<String> iterator = CACHE_LOGS.keySet().iterator();
    int index = 0;
    while (iterator.hasNext()) {
      String key = iterator.next();
      logs.add(CACHE_LOGS.get(key));
      CACHE_LOGS.remove(key);
      if (count != null && index >= count - 1) {
        break;
      }
      index++;
    }
    return logs;
  }
}
