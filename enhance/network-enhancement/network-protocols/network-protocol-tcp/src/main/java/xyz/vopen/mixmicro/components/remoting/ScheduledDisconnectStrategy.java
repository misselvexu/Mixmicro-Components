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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import xyz.vopen.mixmicro.components.remoting.config.ConfigManager;
import xyz.vopen.mixmicro.components.remoting.config.Configs;
import xyz.vopen.mixmicro.components.remoting.log.RemotingLoggerFactory;
import xyz.vopen.mixmicro.components.remoting.util.FutureTaskUtil;
import xyz.vopen.mixmicro.components.remoting.util.RemotingUtil;
import xyz.vopen.mixmicro.components.remoting.util.RunStateRecordedFutureTask;

/**
 * An implemented strategy to monitor connections: <lu>
 * <li>each time scheduled, filter connections with {@link Configs#CONN_SERVICE_STATUS_OFF} at
 *     first.
 * <li>then close connections. </lu>
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
public class ScheduledDisconnectStrategy implements ConnectionMonitorStrategy {
  private static final Logger logger = RemotingLoggerFactory.getLogger("CommonDefault");

  private final int connectionThreshold;
  private final Random random;

  public ScheduledDisconnectStrategy() {
    this.connectionThreshold = ConfigManager.conn_threshold();
    this.random = new Random();
  }

  /**
   * This method only invoked in ScheduledDisconnectStrategy, so no need to be exposed. This method
   * will be remove in next version, do not use this method.
   *
   * <p>The user cannot call ScheduledDisconnectStrategy#filter, so modifying the implementation of
   * this method is safe.
   *
   * @param connections connections from a connection pool
   */
  @Deprecated
  @Override
  public Map<String, List<Connection>> filter(List<Connection> connections) {
    List<Connection> serviceOnConnections = new ArrayList<Connection>();
    List<Connection> serviceOffConnections = new ArrayList<Connection>();
    Map<String, List<Connection>> filteredConnections =
        new ConcurrentHashMap<String, List<Connection>>();

    for (Connection connection : connections) {
      if (isConnectionOn(connection)) {
        serviceOnConnections.add(connection);
      } else {
        serviceOffConnections.add(connection);
      }
    }

    filteredConnections.put(Configs.CONN_SERVICE_STATUS_ON, serviceOnConnections);
    filteredConnections.put(Configs.CONN_SERVICE_STATUS_OFF, serviceOffConnections);
    return filteredConnections;
  }

  @Override
  public void monitor(Map<String, RunStateRecordedFutureTask<ConnectionPool>> connPools) {
    try {
      if (connPools == null || connPools.size() == 0) {
        return;
      }

      for (Map.Entry<String, RunStateRecordedFutureTask<ConnectionPool>> entry :
          connPools.entrySet()) {
        String poolKey = entry.getKey();
        ConnectionPool pool = FutureTaskUtil.getFutureTaskResult(entry.getValue(), logger);

        List<Connection> serviceOnConnections = new ArrayList<Connection>();
        List<Connection> serviceOffConnections = new ArrayList<Connection>();
        for (Connection connection : pool.getAll()) {
          if (isConnectionOn(connection)) {
            serviceOnConnections.add(connection);
          } else {
            serviceOffConnections.add(connection);
          }
        }

        if (serviceOnConnections.size() > connectionThreshold) {
          Connection freshSelectConnect =
              serviceOnConnections.get(random.nextInt(serviceOnConnections.size()));
          freshSelectConnect.setAttribute(
              Configs.CONN_SERVICE_STATUS, Configs.CONN_SERVICE_STATUS_OFF);
          serviceOffConnections.add(freshSelectConnect);
        } else {
          if (logger.isInfoEnabled()) {
            logger.info(
                "serviceOnConnections({}) size[{}], CONNECTION_THRESHOLD[{}].",
                poolKey,
                serviceOnConnections.size(),
                connectionThreshold);
          }
        }

        for (Connection offConn : serviceOffConnections) {
          if (offConn.isInvokeFutureMapFinish()) {
            if (offConn.isFine()) {
              offConn.close();
            }
          } else {
            if (logger.isInfoEnabled()) {
              logger.info(
                  "Address={} won't close at this schedule turn",
                  RemotingUtil.parseRemoteAddress(offConn.getChannel()));
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("ScheduledDisconnectStrategy monitor error", e);
    }
  }

  private boolean isConnectionOn(Connection connection) {
    String serviceStatus = (String) connection.getAttribute(Configs.CONN_SERVICE_STATUS);
    return serviceStatus == null || Boolean.parseBoolean(serviceStatus);
  }
}
