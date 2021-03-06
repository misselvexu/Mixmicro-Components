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
package com.alipay.stc.bl;

import xyz.vopen.mixmicro.kits.hessian.NameBlackListFilter;

import java.util.Arrays;
import java.util.List;

/** @author <a href="mailto:zhanggeng.zg@antfin.com">GengZhang</a> */
public class MockNameBlacklistFilter extends NameBlackListFilter {

  private static final List<String> INTERNAL_BLACK_LIST = Arrays.asList("com.alipay.stc.bl");

  public MockNameBlacklistFilter() {
    super(INTERNAL_BLACK_LIST);
  }

  protected Boolean inBlackList(String className) {
    for (String prefix : INTERNAL_BLACK_LIST) {
      if (className.startsWith(prefix)) {
        return Boolean.TRUE;
      }
    }
    return Boolean.FALSE;
  }
}
