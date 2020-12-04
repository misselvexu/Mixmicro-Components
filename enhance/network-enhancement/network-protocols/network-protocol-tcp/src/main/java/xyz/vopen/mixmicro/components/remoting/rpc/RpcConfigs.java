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

/**
 * Constants for rpc.
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
public class RpcConfigs {
  /** Protocol key in url. */
  public static final String URL_PROTOCOL = "_PROTOCOL";

  /** Version key in url. */
  public static final String URL_VERSION = "_VERSION";

  /** Connection timeout key in url. */
  public static final String CONNECT_TIMEOUT_KEY = "_CONNECTTIMEOUT";

  /** Connection number key of each address */
  public static final String CONNECTION_NUM_KEY = "_CONNECTIONNUM";

  /** whether need to warm up connections */
  public static final String CONNECTION_WARMUP_KEY = "_CONNECTIONWARMUP";

  /** Whether to dispatch message list in default executor. */
  public static final String DISPATCH_MSG_LIST_IN_DEFAULT_EXECUTOR =
      "mixmicro.remoting.rpc.dispatch-msg-list-in-default-executor";

  public static final String DISPATCH_MSG_LIST_IN_DEFAULT_EXECUTOR_DEFAULT = "true";

  /** Whether to enable server SSL support, default is false(disabled). */
  public static final String SRV_SSL_ENABLE = "mixmicro.remoting.server.ssl.enable";
  /** Whether to enable server SSL client auth, default is false(disabled). */
  public static final String SRV_SSL_NEED_CLIENT_AUTH = "mixmicro.remoting.server.ssl.clientAuth";
  /** Server SSL keystore file path */
  public static final String SRV_SSL_KEYSTORE = "mixmicro.remoting.server.ssl.keystore";
  /** Server SSL keystore password */
  public static final String SRV_SSL_KEYSTORE_PASS =
      "mixmicro.remoting.server.ssl.keystore.password";
  /** Server SSL keystore type, JKS or pkcs12 for example. */
  public static final String SRV_SSL_KEYTSTORE_YPE = "mixmicro.remoting.server.ssl.keystore.type";
  /** Server SSL KeyManagerFactory algorithm. */
  public static final String SRV_SSL_KMF_ALGO = "mixmicro.remoting.server.ssl.kmf.algorithm";
  /** Whether to enable client SSL support, default is false(disabled). */
  public static final String CLI_SSL_ENABLE = "mixmicro.remoting.client.ssl.enable";
  /** Client SSL keystore file path */
  public static final String CLI_SSL_KEYSTORE = "mixmicro.remoting.client.ssl.keystore";
  /** Client SSL keystore password */
  public static final String CLI_SSL_KEYSTORE_PASS =
      "mixmicro.remoting.client.ssl.keystore.password";
  /** Client SSL keystore type, JKS pkcs12 for example. */
  public static final String CLI_SSL_KEYTSTORE_YPE = "mixmicro.remoting.client.ssl.keystore.type";
  /** Client SSL TrustManagerFactory algorithm. */
  public static final String CLI_SSL_TMF_ALGO = "mixmicro.remoting.client.ssl.tmf.algorithm";
}
