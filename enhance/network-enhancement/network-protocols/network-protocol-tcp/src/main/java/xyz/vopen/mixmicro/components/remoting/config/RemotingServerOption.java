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

/**
 * Supported options in server side.
 *
 * @author Elve.Xu (iskp.me@gmail.com) 2018-11-06 18:00
 */
public class RemotingServerOption<T> extends RemotingGenericOption<T> {

  public static final RemotingOption<Integer> TCP_SO_BACKLOG =
      valueOf("mixmicro.remoting.tcp.so.backlog", 1024);

  public static final RemotingOption<Boolean> NETTY_EPOLL_LT =
      valueOf("mixmicro.remoting.netty.epoll.lt", true);

  public static final RemotingOption<Integer> TCP_SERVER_IDLE =
      valueOf("mixmicro.remoting.tcp.server.idle.interval", 90 * 1000);

  private RemotingServerOption(String name, T defaultValue) {
    super(name, defaultValue);
  }
}
