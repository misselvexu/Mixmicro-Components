/*
 * Licensed to the VOPEN+ Group under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.vopen.mixmicro.components.logger.core.proxy;

import xyz.vopen.mixmicro.components.logger.core.LoggerSpaceManager;
import xyz.vopen.mixmicro.components.logger.core.SpaceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> on 2016/12/5. */
public class TemporaryILoggerFactoryPool {

  private static final ConcurrentHashMap<SpaceIdWithClassloader, TemporaryILoggerFactory> SPACE_CLASSLOADER_TEMPORARY_MAP = new ConcurrentHashMap<SpaceIdWithClassloader, TemporaryILoggerFactory>();

  private static final Logger logger = LoggerFactory.getLogger(LoggerSpaceManager.class);

  public static TemporaryILoggerFactory get(String space, ClassLoader spaceClassLoader) {
    return get(new SpaceId(space), spaceClassLoader);
  }

  public static TemporaryILoggerFactory get(SpaceId spaceId, ClassLoader spaceClassLoader) {
    // get from local cache by key {spaceClassLoader+ spacename};

    TemporaryILoggerFactory factory = SPACE_CLASSLOADER_TEMPORARY_MAP.get(new SpaceIdWithClassloader(spaceId, spaceClassLoader));

    if (factory != null) {
      return factory;
    }

    // create new one
    factory = new TemporaryILoggerFactory(spaceId, spaceClassLoader, logger);

    // put it to cache ;
    SPACE_CLASSLOADER_TEMPORARY_MAP.putIfAbsent(new SpaceIdWithClassloader(spaceId, spaceClassLoader), factory);
    return factory;
  }

  private static class SpaceIdWithClassloader {
    private SpaceId spaceId;
    private ClassLoader classLoader;

    public SpaceIdWithClassloader() {}

    public SpaceIdWithClassloader(SpaceId spaceId, ClassLoader classLoader) {
      this.spaceId = spaceId;
      this.classLoader = classLoader;
    }

    public SpaceId getSpaceId() {
      return spaceId;
    }

    public void setSpaceId(SpaceId spaceId) {
      this.spaceId = spaceId;
    }

    public ClassLoader getClassLoader() {
      return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
      this.classLoader = classLoader;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      SpaceIdWithClassloader that = (SpaceIdWithClassloader) o;

      if (spaceId != null ? !spaceId.equals(that.spaceId) : that.spaceId != null) {
        return false;
      }
      return classLoader != null ? classLoader.equals(that.classLoader) : that.classLoader == null;
    }

    @Override
    public int hashCode() {
      int result = spaceId != null ? spaceId.hashCode() : 0;
      result = 31 * result + (classLoader != null ? classLoader.hashCode() : 0);
      return result;
    }
  }
}
