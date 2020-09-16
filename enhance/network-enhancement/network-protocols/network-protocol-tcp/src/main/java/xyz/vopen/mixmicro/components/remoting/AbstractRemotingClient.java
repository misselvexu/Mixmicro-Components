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

import xyz.vopen.mixmicro.components.remoting.config.RemotingOption;
import xyz.vopen.mixmicro.components.remoting.config.RemotingOptions;
import xyz.vopen.mixmicro.components.remoting.config.ConfigManager;
import xyz.vopen.mixmicro.components.remoting.config.Configurable;
import xyz.vopen.mixmicro.components.remoting.config.ConfigurableInstance;
import xyz.vopen.mixmicro.components.remoting.config.configs.ConfigContainer;
import xyz.vopen.mixmicro.components.remoting.config.configs.ConfigItem;
import xyz.vopen.mixmicro.components.remoting.config.configs.ConfigType;
import xyz.vopen.mixmicro.components.remoting.config.configs.DefaultConfigContainer;
import xyz.vopen.mixmicro.components.remoting.config.switches.GlobalSwitch;

/** @author Elve.Xu (iskp.me@gmail.com) 2018-11-07 15:22 */
public abstract class AbstractRemotingClient extends AbstractLifeCycle
    implements RemotingClient, ConfigurableInstance {

  private final RemotingOptions options;
  private final ConfigType configType;
  private final GlobalSwitch globalSwitch;
  private final ConfigContainer configContainer;

  public AbstractRemotingClient() {
    this.options = new RemotingOptions();
    this.configType = ConfigType.CLIENT_SIDE;
    this.globalSwitch = new GlobalSwitch();
    this.configContainer = new DefaultConfigContainer();
  }

  @Override
  public <T> T option(RemotingOption<T> option) {
    return options.option(option);
  }

  @Override
  public <T> Configurable option(RemotingOption<T> option, T value) {
    options.option(option, value);
    return this;
  }

  @Override
  public ConfigContainer conf() {
    return this.configContainer;
  }

  @Override
  public GlobalSwitch switches() {
    return this.globalSwitch;
  }

  @Override
  public void initWriteBufferWaterMark(int low, int high) {
    this.configContainer.set(configType, ConfigItem.NETTY_BUFFER_LOW_WATER_MARK, low);
    this.configContainer.set(configType, ConfigItem.NETTY_BUFFER_HIGH_WATER_MARK, high);
  }

  @Override
  public int netty_buffer_low_watermark() {
    Object config = configContainer.get(configType, ConfigItem.NETTY_BUFFER_LOW_WATER_MARK);
    if (config != null) {
      return (Integer) config;
    } else {
      return ConfigManager.netty_buffer_low_watermark();
    }
  }

  @Override
  public int netty_buffer_high_watermark() {
    Object config = configContainer.get(configType, ConfigItem.NETTY_BUFFER_HIGH_WATER_MARK);
    if (config != null) {
      return (Integer) config;
    } else {
      return ConfigManager.netty_buffer_high_watermark();
    }
  }
}
