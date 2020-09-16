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

import xyz.vopen.mixmicro.components.remoting.util.RemotingUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * default biz context
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
public class DefaultBizContext implements BizContext {
  /** remoting context */
  private RemotingContext remotingCtx;

  /**
   * Constructor with RemotingContext
   *
   * @param remotingCtx
   */
  public DefaultBizContext(RemotingContext remotingCtx) {
    this.remotingCtx = remotingCtx;
  }

  /**
   * get remoting context
   *
   * @return RemotingContext
   */
  protected RemotingContext getRemotingCtx() {
    return this.remotingCtx;
  }

  /** @see BizContext#getRemoteAddress() */
  @Override
  public String getRemoteAddress() {
    if (null != this.remotingCtx) {
      ChannelHandlerContext channelCtx = this.remotingCtx.getChannelContext();
      Channel channel = channelCtx.channel();
      if (null != channel) {
        return RemotingUtil.parseRemoteAddress(channel);
      }
    }
    return "UNKNOWN_ADDRESS";
  }

  /** @see BizContext#getRemoteHost() */
  @Override
  public String getRemoteHost() {
    if (null != this.remotingCtx) {
      ChannelHandlerContext channelCtx = this.remotingCtx.getChannelContext();
      Channel channel = channelCtx.channel();
      if (null != channel) {
        return RemotingUtil.parseRemoteIP(channel);
      }
    }
    return "UNKNOWN_HOST";
  }

  /** @see BizContext#getRemotePort() */
  @Override
  public int getRemotePort() {
    if (null != this.remotingCtx) {
      ChannelHandlerContext channelCtx = this.remotingCtx.getChannelContext();
      Channel channel = channelCtx.channel();
      if (null != channel) {
        return RemotingUtil.parseRemotePort(channel);
      }
    }
    return -1;
  }

  /** @see BizContext#getConnection() */
  @Override
  public Connection getConnection() {
    if (null != this.remotingCtx) {
      return this.remotingCtx.getConnection();
    }
    return null;
  }

  /** @see BizContext#isRequestTimeout() */
  @Override
  public boolean isRequestTimeout() {
    return this.remotingCtx.isRequestTimeout();
  }

  /**
   * get the timeout value from rpc client.
   *
   * @return
   */
  @Override
  public int getClientTimeout() {
    return this.remotingCtx.getTimeout();
  }

  /**
   * get the arrive time stamp
   *
   * @return
   */
  @Override
  public long getArriveTimestamp() {
    return this.remotingCtx.getArriveTimestamp();
  }

  /** @see BizContext#put(java.lang.String, java.lang.String) */
  @Override
  public void put(String key, String value) {}

  /** @see BizContext#get(java.lang.String) */
  @Override
  public String get(String key) {
    return null;
  }

  /** @see BizContext#getInvokeContext() */
  @Override
  public InvokeContext getInvokeContext() {
    return this.remotingCtx.getInvokeContext();
  }
}
