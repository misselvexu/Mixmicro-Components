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
package xyz.vopen.mixmicro.components.remoting.config;

import xyz.vopen.mixmicro.components.remoting.rpc.RpcClient;
import xyz.vopen.mixmicro.components.remoting.serialization.SerializerManager;

/**
 * Define the key for a certain config item using system property, and provide the default value for
 * that config item.
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
public class Configs {
  // ~~~ configs and default values for bootstrap

  /** TCP_NODELAY option */
  public static final String TCP_NODELAY = "mixmicro.remoting.tcp.nodelay";

  public static final String TCP_NODELAY_DEFAULT = "true";

  /** TCP SO_REUSEADDR option */
  public static final String TCP_SO_REUSEADDR = "mixmicro.remoting.tcp.so.reuseaddr";

  public static final String TCP_SO_REUSEADDR_DEFAULT = "true";

  /** TCP SO_BACKLOG option */
  public static final String TCP_SO_BACKLOG = "mixmicro.remoting.tcp.so.backlog";

  public static final String TCP_SO_BACKLOG_DEFAULT = "1024";

  /** TCP SO_KEEPALIVE option */
  public static final String TCP_SO_KEEPALIVE = "mixmicro.remoting.tcp.so.keepalive";

  public static final String TCP_SO_KEEPALIVE_DEFAULT = "true";

  /** TCP SO_SNDBUF option */
  public static final String TCP_SO_SNDBUF = "mixmicro.remoting.tcp.so.sndbuf";
  /** TCP SO_RCVBUF option */
  public static final String TCP_SO_RCVBUF = "mixmicro.remoting.tcp.so.rcvbuf";

  /** Netty ioRatio option */
  public static final String NETTY_IO_RATIO = "mixmicro.remoting.netty.io.ratio";

  public static final String NETTY_IO_RATIO_DEFAULT = "70";

  /** Netty buffer allocator, enabled as default */
  public static final String NETTY_BUFFER_POOLED = "mixmicro.remoting.netty.buffer.pooled";

  public static final String NETTY_BUFFER_POOLED_DEFAULT = "true";

  /** Netty buffer high watermark */
  public static final String NETTY_BUFFER_HIGH_WATERMARK =
      "mixmicro.remoting.netty.buffer.high.watermark";

  public static final String NETTY_BUFFER_HIGH_WATERMARK_DEFAULT = Integer.toString(64 * 1024);

  /** Netty buffer low watermark */
  public static final String NETTY_BUFFER_LOW_WATERMARK =
      "mixmicro.remoting.netty.buffer.low.watermark";

  public static final String NETTY_BUFFER_LOW_WATERMARK_DEFAULT = Integer.toString(32 * 1024);

  /** Netty epoll switch */
  public static final String NETTY_EPOLL_SWITCH = "mixmicro.remoting.netty.epoll.switch";

  public static final String NETTY_EPOLL_SWITCH_DEFAULT = "true";

  /** Netty epoll level trigger enabled */
  public static final String NETTY_EPOLL_LT = "mixmicro.remoting.netty.epoll.lt";

  public static final String NETTY_EPOLL_LT_DEFAULT = "true";

  // ~~~ configs and default values for idle

  /** TCP idle switch */
  public static final String TCP_IDLE_SWITCH = "mixmicro.remoting.tcp.heartbeat.switch";

  public static final String TCP_IDLE_SWITCH_DEFAULT = "true";

  /** TCP idle interval for client */
  public static final String TCP_IDLE = "mixmicro.remoting.tcp.heartbeat.interval";

  public static final String TCP_IDLE_DEFAULT = "15000";

  /** TCP idle triggered max times if no response */
  public static final String TCP_IDLE_MAXTIMES = "mixmicro.remoting.tcp.heartbeat.maxtimes";

  public static final String TCP_IDLE_MAXTIMES_DEFAULT = "3";

  /** TCP idle interval for server */
  public static final String TCP_SERVER_IDLE = "mixmicro.remoting.tcp.server.idle.interval";

  public static final String TCP_SERVER_IDLE_DEFAULT = "90000";

  // ~~~ configs and default values for connection manager

  /** Thread pool min size for the connection manager executor */
  public static final String CONN_CREATE_TP_MIN_SIZE = "mixmicro.remoting.conn.create.tp.min";

  public static final String CONN_CREATE_TP_MIN_SIZE_DEFAULT = "3";

  /** Thread pool max size for the connection manager executor */
  public static final String CONN_CREATE_TP_MAX_SIZE = "mixmicro.remoting.conn.create.tp.max";

  public static final String CONN_CREATE_TP_MAX_SIZE_DEFAULT = "8";

  /** Thread pool queue size for the connection manager executor */
  public static final String CONN_CREATE_TP_QUEUE_SIZE = "mixmicro.remoting.conn.create.tp.queue";

  public static final String CONN_CREATE_TP_QUEUE_SIZE_DEFAULT = "50";

  /** Thread pool keep alive time for the connection manager executor */
  public static final String CONN_CREATE_TP_KEEPALIVE_TIME =
      "mixmicro.remoting.conn.create.tp.keepalive";

  public static final String CONN_CREATE_TP_KEEPALIVE_TIME_DEFAULT = "60";

  /** Default connect timeout value, time unit: ms */
  public static final int DEFAULT_CONNECT_TIMEOUT = 1000;

  /** default connection number per url */
  public static final int DEFAULT_CONN_NUM_PER_URL = 1;

  /** max connection number of each url */
  public static final int MAX_CONN_NUM_PER_URL = 100 * 10000;

  // ~~~ configs for processor manager

  /** Thread pool min size for the default executor. */
  public static final String TP_MIN_SIZE = "mixmicro.remoting.tp.min";

  public static final String TP_MIN_SIZE_DEFAULT = "20";

  /** Thread pool max size for the default executor. */
  public static final String TP_MAX_SIZE = "mixmicro.remoting.tp.max";

  public static final String TP_MAX_SIZE_DEFAULT = "400";

  /** Thread pool queue size for the default executor. */
  public static final String TP_QUEUE_SIZE = "mixmicro.remoting.tp.queue";

  public static final String TP_QUEUE_SIZE_DEFAULT = "600";

  /** Thread pool keep alive time for the default executor */
  public static final String TP_KEEPALIVE_TIME = "mixmicro.remoting.tp.keepalive";

  public static final String TP_KEEPALIVE_TIME_DEFAULT = "60";

  // ~~~ configs and default values for reconnect manager

  /** Reconnection switch */
  public static final String CONN_RECONNECT_SWITCH = "mixmicro.remoting.conn.reconnect.switch";

  public static final String CONN_RECONNECT_SWITCH_DEFAULT = "false";

  // ~~~ configs and default values for connection monitor

  /**
   * Connection monitor switch
   *
   * <p>If switch on connection monitor, it may cause {@link RpcClient#oneway} to fail. Please try
   * to use other types of RPC methods
   */
  public static final String CONN_MONITOR_SWITCH = "mixmicro.remoting.conn.monitor.switch";

  public static final String CONN_MONITOR_SWITCH_DEFAULT = "false";

  /** Initial delay to execute schedule task for connection monitor */
  public static final String CONN_MONITOR_INITIAL_DELAY =
      "mixmicro.remoting.conn.monitor.initial.delay";

  public static final String CONN_MONITOR_INITIAL_DELAY_DEFAULT = "10000";

  /** Period of schedule task for connection monitor */
  public static final String CONN_MONITOR_PERIOD = "mixmicro.remoting.conn.monitor.period";

  public static final String CONN_MONITOR_PERIOD_DEFAULT = "180000";

  /** Connection threshold */
  public static final String CONN_THRESHOLD = "mixmicro.remoting.conn.threshold";

  public static final String CONN_THRESHOLD_DEFAULT = "3";

  /** Retry detect period for ScheduledDisconnectStrategy */
  @Deprecated
  public static final String RETRY_DETECT_PERIOD = "mixmicro.remoting.retry.delete.period";

  public static final String RETRY_DETECT_PERIOD_DEFAULT = "5000";

  /** Connection status */
  public static final String CONN_SERVICE_STATUS = "mixmicro.remoting.conn.service.status";

  public static final String CONN_SERVICE_STATUS_OFF = "off";
  public static final String CONN_SERVICE_STATUS_ON = "on";

  // ~~~ configs and default values for serializer

  /** Codec type */
  public static final String SERIALIZER = "mixmicro.remoting.serializer";

  public static final String SERIALIZER_DEFAULT = String.valueOf(SerializerManager.Hessian2);

  /** Charset */
  public static final String DEFAULT_CHARSET = "UTF-8";
}
