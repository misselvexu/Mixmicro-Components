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

import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import xyz.vopen.mixmicro.components.remoting.config.ConfigManager;
import xyz.vopen.mixmicro.components.remoting.log.RemotingLoggerFactory;
import xyz.vopen.mixmicro.components.remoting.util.RunStateRecordedFutureTask;

/**
 * A default connection monitor that handle connections with strategies
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
public class DefaultConnectionMonitor extends AbstractLifeCycle {

  private static final Logger logger = RemotingLoggerFactory.getLogger("CommonDefault");

  private final DefaultConnectionManager connectionManager;
  private final ConnectionMonitorStrategy strategy;

  private ScheduledThreadPoolExecutor executor;

  public DefaultConnectionMonitor(
      ConnectionMonitorStrategy strategy, DefaultConnectionManager connectionManager) {
    if (strategy == null) {
      throw new IllegalArgumentException("null strategy");
    }

    if (connectionManager == null) {
      throw new IllegalArgumentException("null connectionManager");
    }

    this.strategy = strategy;
    this.connectionManager = connectionManager;
  }

  @Override
  public void startup() throws LifeCycleException {
    super.startup();

    /* initial delay to execute schedule task, unit: ms */
    long initialDelay = ConfigManager.conn_monitor_initial_delay();

    /* period of schedule task, unit: ms*/
    long period = ConfigManager.conn_monitor_period();

    this.executor =
        new ScheduledThreadPoolExecutor(
            1,
            new NamedThreadFactory("ConnectionMonitorThread", true),
            new ThreadPoolExecutor.AbortPolicy());
    this.executor.scheduleAtFixedRate(
        new Runnable() {
          @Override
          public void run() {
            try {
              Map<String, RunStateRecordedFutureTask<ConnectionPool>> connPools =
                  connectionManager.getConnPools();
              strategy.monitor(connPools);
            } catch (Exception e) {
              logger.warn("MonitorTask error", e);
            }
          }
        },
        initialDelay,
        period,
        TimeUnit.MILLISECONDS);
  }

  @Override
  public void shutdown() throws LifeCycleException {
    super.shutdown();

    executor.purge();
    executor.shutdown();
  }

  /** Start schedule task please use {@link DefaultConnectionMonitor#startup()} instead */
  @Deprecated
  public void start() {
    startup();
  }

  /**
   * cancel task and shutdown executor please use {@link DefaultConnectionMonitor#shutdown()}
   * instead
   */
  @Deprecated
  public void destroy() {
    shutdown();
  }
}
