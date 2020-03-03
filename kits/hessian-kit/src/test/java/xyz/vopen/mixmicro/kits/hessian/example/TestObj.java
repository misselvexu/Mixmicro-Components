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
package xyz.vopen.mixmicro.kits.hessian.example;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhanggeng on 2018/4/3.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class TestObj implements Serializable {

  private String name;
  private int age;
  private List<String> nickNames;

  public String getName() {
    return name;
  }

  public TestObj setName(String name) {
    this.name = name;
    return this;
  }

  public int getAge() {
    return age;
  }

  public TestObj setAge(int age) {
    this.age = age;
    return this;
  }

  public List<String> getNickNames() {
    return nickNames;
  }

  public TestObj setNickNames(List<String> nickNames) {
    this.nickNames = nickNames;
    return this;
  }
}
