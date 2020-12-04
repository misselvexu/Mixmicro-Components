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

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;

import xyz.vopen.mixmicro.components.remoting.log.RemotingLoggerFactory;
import xyz.vopen.mixmicro.components.remoting.util.RemotingUtil;

/**
 * Processor to process remoting command.
 *
 * @author Elve.Xu (iskp.me@gmail.com)
 * @param <T>
 */
public abstract class AbstractRemotingProcessor<T extends RemotingCommand>
    implements RemotingProcessor<T> {
  private static final Logger logger = RemotingLoggerFactory.getLogger("CommonDefault");
  private ExecutorService executor;
  private CommandFactory commandFactory;

  /** Default constructor. */
  public AbstractRemotingProcessor() {}

  /** Constructor. */
  public AbstractRemotingProcessor(CommandFactory commandFactory) {
    this.commandFactory = commandFactory;
  }

  /**
   * Constructor.
   *
   * @param executor ExecutorService
   */
  public AbstractRemotingProcessor(ExecutorService executor) {
    this.executor = executor;
  }

  /**
   * Constructor.
   *
   * @param commandFactory CommandFactory
   * @param executor ExecutorService
   */
  public AbstractRemotingProcessor(CommandFactory commandFactory, ExecutorService executor) {
    this.commandFactory = commandFactory;
    this.executor = executor;
  }

  /**
   * Do the process.
   *
   * @param ctx RemotingContext
   * @param msg T
   */
  public abstract void doProcess(RemotingContext ctx, T msg) throws Exception;

  /**
   * Process the remoting command with its own executor or with the defaultExecutor if its own if
   * null.
   *
   * @param ctx RemotingContext
   * @param msg T
   * @param defaultExecutor ExecutorService, default executor
   */
  @Override
  public void process(RemotingContext ctx, T msg, ExecutorService defaultExecutor)
      throws Exception {
    ProcessTask task = new ProcessTask(ctx, msg);
    if (this.getExecutor() != null) {
      this.getExecutor().execute(task);
    } else {
      defaultExecutor.execute(task);
    }
  }

  /**
   * Getter method for property <tt>executor</tt>.
   *
   * @return property value of executor
   */
  @Override
  public ExecutorService getExecutor() {
    return executor;
  }

  /**
   * Setter method for property <tt>executor</tt>.
   *
   * @param executor value to be assigned to property executor
   */
  @Override
  public void setExecutor(ExecutorService executor) {
    this.executor = executor;
  }

  public CommandFactory getCommandFactory() {
    return commandFactory;
  }

  public void setCommandFactory(CommandFactory commandFactory) {
    this.commandFactory = commandFactory;
  }

  /**
   * Task for asynchronous process.
   *
   * @author Elve.Xu (iskp.me@gmail.com)
   */
  class ProcessTask implements Runnable {

    RemotingContext ctx;
    T msg;

    public ProcessTask(RemotingContext ctx, T msg) {
      this.ctx = ctx;
      this.msg = msg;
    }

    @Override
    public void run() {
      try {
        AbstractRemotingProcessor.this.doProcess(ctx, msg);
      } catch (Throwable e) {
        // protect the thread running this task
        String remotingAddress = RemotingUtil.parseRemoteAddress(ctx.getChannelContext().channel());
        logger.error(
            "Exception caught when process rpc request command in AbstractRemotingProcessor, Id="
                + msg.getId()
                + "! Invoke source address is ["
                + remotingAddress
                + "].",
            e);
      }
    }
  }
}
