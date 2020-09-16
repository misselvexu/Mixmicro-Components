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

import java.util.concurrent.ConcurrentHashMap;

/**
 *   Invoke context
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
public class InvokeContext {
  // ~~~ invoke context keys of client side
  public static final String CLIENT_LOCAL_IP = "mixmicro.remoting.client.local.ip";
  public static final String CLIENT_LOCAL_PORT = "mixmicro.remoting.client.local.port";
  public static final String CLIENT_REMOTE_IP = "mixmicro.remoting.client.remote.ip";
  public static final String CLIENT_REMOTE_PORT = "mixmicro.remoting.client.remote.port";
  /** time consumed during connection creating, this is a timespan */
  public static final String CLIENT_CONN_CREATETIME = "mixmicro.remoting.client.conn.createtime";

  // ~~~ invoke context keys of server side
  public static final String SERVER_LOCAL_IP = "mixmicro.remoting.server.local.ip";
  public static final String SERVER_LOCAL_PORT = "mixmicro.remoting.server.local.port";
  public static final String SERVER_REMOTE_IP = "mixmicro.remoting.server.remote.ip";
  public static final String SERVER_REMOTE_PORT = "mixmicro.remoting.server.remote.port";

  // ~~~ invoke context keys of bolt client and server side
  public static final String BOLT_INVOKE_REQUEST_ID = "mixmicro.remoting.invoke.request.id";
  /**
   * time consumed start from the time when request arrive, to the time when request be processed,
   * this is a timespan
   */
  public static final String BOLT_PROCESS_WAIT_TIME = "mixmicro.remoting.invoke.wait.time";

  public static final String BOLT_CUSTOM_SERIALIZER = "mixmicro.remoting.invoke.custom.serializer";
  public static final String BOLT_CRC_SWITCH = "mixmicro.remoting.invoke.crc.switch";

  // ~~~ constants
  public static final int INITIAL_SIZE = 8;

  /** context */
  private ConcurrentHashMap<String, Object> context;

  /** default construct */
  public InvokeContext() {
    this.context = new ConcurrentHashMap<String, Object>(INITIAL_SIZE);
  }

  /**
   * put if absent
   *
   * @param key
   * @param value
   */
  public void putIfAbsent(String key, Object value) {
    this.context.putIfAbsent(key, value);
  }

  /**
   * put
   *
   * @param key
   * @param value
   */
  public void put(String key, Object value) {
    this.context.put(key, value);
  }

  /**
   * get
   *
   * @param key
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> T get(String key) {
    return (T) this.context.get(key);
  }

  /**
   * get and use default if not found
   *
   * @param key
   * @param defaultIfNotFound
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> T get(String key, T defaultIfNotFound) {
    return this.context.get(key) != null ? (T) this.context.get(key) : defaultIfNotFound;
  }

  /** clear all mappings. */
  public void clear() {
    this.context.clear();
  }
}
