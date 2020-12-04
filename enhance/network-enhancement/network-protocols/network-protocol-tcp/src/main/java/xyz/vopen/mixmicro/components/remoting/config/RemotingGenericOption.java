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

import xyz.vopen.mixmicro.components.remoting.ConnectionSelectStrategy;

/**
 * Supported options both in client and server side.
 *
 * @author Elve.Xu (iskp.me@gmail.com) 2018-11-06 17:59
 */
public class RemotingGenericOption<T> extends RemotingOption<T> {

  /*------------ NETTY Config Start ------------*/
  public static final RemotingOption<Boolean> TCP_NODELAY =
      valueOf("mixmicro.remoting.tcp.nodelay", true);
  public static final RemotingOption<Boolean> TCP_SO_REUSEADDR =
      valueOf("mixmicro.remoting.tcp.so.reuseaddr", true);
  public static final RemotingOption<Boolean> TCP_SO_KEEPALIVE =
      valueOf("mixmicro.remoting.tcp.so.keepalive", true);

  public static final RemotingOption<Integer> TCP_SO_SNDBUF =
      valueOf("mixmicro.remoting.tcp.so.sndbuf");

  public static final RemotingOption<Integer> TCP_SO_RCVBUF =
      valueOf("mixmicro.remoting.tcp.so.rcvbuf");

  public static final RemotingOption<Integer> NETTY_IO_RATIO =
      valueOf("mixmicro.remoting.netty.io.ratio", 70);
  public static final RemotingOption<Boolean> NETTY_BUFFER_POOLED =
      valueOf("mixmicro.remoting.netty.buffer.pooled", true);

  public static final RemotingOption<Integer> NETTY_BUFFER_HIGH_WATER_MARK =
      valueOf("mixmicro.remoting.netty.buffer.high.watermark", 64 * 1024);
  public static final RemotingOption<Integer> NETTY_BUFFER_LOW_WATER_MARK =
      valueOf("mixmicro.remoting.netty.buffer.low.watermark", 32 * 1024);

  public static final RemotingOption<Boolean> NETTY_EPOLL_SWITCH =
      valueOf("mixmicro.remoting.netty.epoll.switch", true);

  public static final RemotingOption<Boolean> TCP_IDLE_SWITCH =
      valueOf("mixmicro.remoting.tcp.heartbeat.switch", true);
  /*------------ NETTY Config End ------------*/

  /*------------ Thread Pool Config Start ------------*/
  public static final RemotingOption<Integer> TP_MIN_SIZE = valueOf("mixmicro.remoting.tp.min", 20);
  public static final RemotingOption<Integer> TP_MAX_SIZE =
      valueOf("mixmicro.remoting.tp.max", 400);
  public static final RemotingOption<Integer> TP_QUEUE_SIZE =
      valueOf("mixmicro.remoting.tp.queue", 600);
  public static final RemotingOption<Integer> TP_KEEPALIVE_TIME =
      valueOf("mixmicro.remoting.tp.keepalive", 60);

  /*------------ Thread Pool Config End ------------*/

  public static final RemotingOption<ConnectionSelectStrategy> CONNECTION_SELECT_STRATEGY =
      valueOf("CONNECTION_SELECT_STRATEGY");

  protected RemotingGenericOption(String name, T defaultValue) {
    super(name, defaultValue);
  }
}
