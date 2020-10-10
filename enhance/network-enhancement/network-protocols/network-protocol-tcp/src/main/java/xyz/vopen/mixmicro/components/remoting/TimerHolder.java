/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
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
package xyz.vopen.mixmicro.components.remoting;

import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

/**
 * A singleton holder of the timer for timeout.
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
public class TimerHolder {

  private static final long defaultTickDuration = 10;

  private static class DefaultInstance {
    static final Timer INSTANCE =
        new HashedWheelTimer(
            new NamedThreadFactory("DefaultTimer" + defaultTickDuration, true),
            defaultTickDuration,
            TimeUnit.MILLISECONDS);
  }

  private TimerHolder() {}

  /**
   * Get a singleton instance of {@link Timer}. <br>
   * The tick duration is {@link #defaultTickDuration}.
   *
   * @return Timer
   */
  public static Timer getTimer() {
    return DefaultInstance.INSTANCE;
  }
}
