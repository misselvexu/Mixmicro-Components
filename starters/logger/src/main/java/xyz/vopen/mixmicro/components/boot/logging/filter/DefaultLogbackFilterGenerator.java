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
package xyz.vopen.mixmicro.components.boot.logging.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import xyz.vopen.mixmicro.components.logger.core.spi.LogbackFilterGenerator;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @since 1.0.15
 */
public class DefaultLogbackFilterGenerator implements LogbackFilterGenerator {
  public static final TurboFilter FILTER =
      new TurboFilter() {

        @Override
        public FilterReply decide(
            Marker marker,
            ch.qos.logback.classic.Logger logger,
            Level level,
            String format,
            Object[] params,
            Throwable t) {
          return FilterReply.DENY;
        }
      };

  @Override
  public TurboFilter[] generatorFilters() {
    return new TurboFilter[] {FILTER};
  }
}
