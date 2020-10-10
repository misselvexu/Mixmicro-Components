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

import java.util.concurrent.ConcurrentHashMap;

import xyz.vopen.mixmicro.components.remoting.Connection;
import xyz.vopen.mixmicro.components.remoting.InvokeContext;
import xyz.vopen.mixmicro.components.remoting.Protocol;
import xyz.vopen.mixmicro.components.remoting.ProtocolCode;
import xyz.vopen.mixmicro.components.remoting.ProtocolManager;
import xyz.vopen.mixmicro.components.remoting.RemotingContext;
import xyz.vopen.mixmicro.components.remoting.rpc.protocol.UserProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Dispatch messages to corresponding protocol.
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 */
@ChannelHandler.Sharable
public class RpcHandler extends ChannelInboundHandlerAdapter {
  private boolean serverSide;

  private ConcurrentHashMap<String, UserProcessor<?>> userProcessors;

  public RpcHandler() {
    serverSide = false;
  }

  public RpcHandler(ConcurrentHashMap<String, UserProcessor<?>> userProcessors) {
    serverSide = false;
    this.userProcessors = userProcessors;
  }

  public RpcHandler(
      boolean serverSide, ConcurrentHashMap<String, UserProcessor<?>> userProcessors) {
    this.serverSide = serverSide;
    this.userProcessors = userProcessors;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ProtocolCode protocolCode = ctx.channel().attr(Connection.PROTOCOL).get();
    Protocol protocol = ProtocolManager.getProtocol(protocolCode);
    protocol
        .getCommandHandler()
        .handleCommand(
            new RemotingContext(ctx, new InvokeContext(), serverSide, userProcessors), msg);
  }
}
