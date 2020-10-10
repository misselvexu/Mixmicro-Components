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
package xyz.vopen.mixmicro.components.remoting.rpc;

import org.slf4j.Logger;
import xyz.vopen.mixmicro.components.remoting.AbstractLifeCycle;
import xyz.vopen.mixmicro.components.remoting.LifeCycleException;
import xyz.vopen.mixmicro.components.remoting.NamedThreadFactory;
import xyz.vopen.mixmicro.components.remoting.Scannable;
import xyz.vopen.mixmicro.components.remoting.log.RemotingLoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Scanner is used to do scan task.
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
public class RpcTaskScanner extends AbstractLifeCycle {

  private static final Logger logger = RemotingLoggerFactory.getLogger("MixmicroRemoting");

  private final List<Scannable> scanList;

  private ScheduledExecutorService scheduledService;

  public RpcTaskScanner() {
    this.scanList = new LinkedList<Scannable>();
  }

  @Override
  public void startup() throws LifeCycleException {
    super.startup();

    scheduledService =
        new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("RpcTaskScannerThread", true));
    scheduledService.scheduleWithFixedDelay(
        new Runnable() {

          @Override
          public void run() {
            for (Scannable scanned : scanList) {
              try {
                scanned.scan();
              } catch (Throwable t) {
                logger.error("Exception caught when scannings.", t);
              }
            }
          }
        },
        10000,
        10000,
        TimeUnit.MILLISECONDS);
  }

  @Override
  public void shutdown() throws LifeCycleException {
    super.shutdown();

    scheduledService.shutdown();
  }

  /** Use {@link RpcTaskScanner#startup()} instead */
  @Deprecated
  public void start() {
    startup();
  }

  /** Add scan target. */
  public void add(Scannable target) {
    scanList.add(target);
  }
}
