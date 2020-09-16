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
 * Config interface.
 *
 * @author Elve.Xu (iskp.me@gmail.com) 2018-11-06 14:46
 */
public interface Configurable {

  /**
   * Get the option value.
   *
   * @param option target option
   * @return BoltOption
   */
  <T> T option(RemotingOption<T> option);

  /**
   * Allow to specify a {@link RemotingOption} which is used for the {@link Configurable} instances
   * once they got created. Use a value of {@code null} to remove a previous set {@link
   * RemotingOption}.
   *
   * @param option target option
   * @param value option value, null to remove the previous option
   * @return Configurable instance
   */
  <T> Configurable option(RemotingOption<T> option, T value);
}
