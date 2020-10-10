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
package xyz.vopen.mixmicro.kits.hessian;

import xyz.vopen.mixmicro.kits.hessian.internal.InternalNameBlackListFilter;

import static xyz.vopen.mixmicro.kits.hessian.HessianConstants.DEFAULT_SERIALIZE_BLACKLIST_ENABLE;
import static xyz.vopen.mixmicro.kits.hessian.HessianConstants.SERIALIZE_BLACKLIST_ENABLE;

/**
 * 类名处理器的构造器
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ClassNameResolverBuilder {

  /**
   * Build default ClassNameResolver
   *
   * @return Default ClassNameResolver
   */
  public static ClassNameResolver buildDefault() {

    String enable = System.getProperty(SERIALIZE_BLACKLIST_ENABLE, DEFAULT_SERIALIZE_BLACKLIST_ENABLE);
    if (Boolean.TRUE.toString().equalsIgnoreCase(enable)) {
      ClassNameResolver resolver = new ClassNameResolver();
      resolver.addFilter(new InternalNameBlackListFilter());
      return resolver;
    }
    return null;
  }
}
