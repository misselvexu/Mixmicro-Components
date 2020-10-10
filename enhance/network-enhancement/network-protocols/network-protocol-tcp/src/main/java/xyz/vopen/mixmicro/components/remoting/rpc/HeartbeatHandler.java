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

import xyz.vopen.mixmicro.components.remoting.Connection;
import xyz.vopen.mixmicro.components.remoting.Protocol;
import xyz.vopen.mixmicro.components.remoting.ProtocolCode;
import xyz.vopen.mixmicro.components.remoting.ProtocolManager;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Heart beat triggerd.
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
@Sharable
public class HeartbeatHandler extends ChannelDuplexHandler {

  /**
   * @see
   *     io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty.channel.ChannelHandlerContext,
   *     java.lang.Object)
   */
  @Override
  public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      ProtocolCode protocolCode = ctx.channel().attr(Connection.PROTOCOL).get();
      Protocol protocol = ProtocolManager.getProtocol(protocolCode);
      protocol.getHeartbeatTrigger().heartbeatTriggered(ctx);
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }
}
