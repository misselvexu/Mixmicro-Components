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
package xyz.vopen.mixmicro.kits.event;

import xyz.vopen.mixmicro.kits.executor.AsyncRuntimeExecutor;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simply event bus for internal event transport.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class EventBus {

  /** 是否启动事件总线，关闭后，可能tracer等会失效，但是可以提高性能 */
  public static final String EVENT_BUS_ENABLE_KEY = "event.bus.enable";

  private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);
  /**
   * 是否允许携带上下文附件，关闭后只能传递"."开头的key，"_" 开头的Key将不被保持和传递。<br>
   * 在性能测试等场景可能关闭此传递功能。
   */
  private static final boolean EVENT_BUS_ENABLE =
      Boolean.parseBoolean(System.getProperty(EVENT_BUS_ENABLE_KEY, "true"));

  /** 某中事件的订阅者 */
  private static final ConcurrentMap<Class<? extends Event>, CopyOnWriteArraySet<Subscriber>>
      SUBSCRIBER_MAP =
          new ConcurrentHashMap<Class<? extends Event>, CopyOnWriteArraySet<Subscriber>>();

  /**
   * 是否开启事件总线功能
   *
   * @return 是否开启事件总线功能
   */
  public static boolean isEnable() {
    return EVENT_BUS_ENABLE;
  }

  /**
   * 是否开启事件总线功能
   *
   * @param eventClass 事件类型
   * @return 是否开启事件总线功能
   */
  public static boolean isEnable(Class<? extends Event> eventClass) {
    return EVENT_BUS_ENABLE && isNotEmpty(SUBSCRIBER_MAP.get(eventClass));
  }

  /**
   * 判断一个集合是否为非空
   *
   * @param collection 集合
   * @return 是否为非空
   */
  private static boolean isNotEmpty(Collection collection) {
    return collection != null && !collection.isEmpty();
  }

  /**
   * 注册一个订阅者
   *
   * @param eventClass 事件类型
   * @param subscriber 订阅者
   */
  public static void register(Class<? extends Event> eventClass, Subscriber subscriber) {
    CopyOnWriteArraySet<Subscriber> set = SUBSCRIBER_MAP.get(eventClass);
    if (set == null) {
      set = new CopyOnWriteArraySet<Subscriber>();
      CopyOnWriteArraySet<Subscriber> old = SUBSCRIBER_MAP.putIfAbsent(eventClass, set);
      if (old != null) {
        set = old;
      }
    }
    set.add(subscriber);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Register subscriber: {} of event: {}.", subscriber, eventClass);
    }
  }

  /**
   * 反注册一个订阅者
   *
   * @param eventClass 事件类型
   * @param subscriber 订阅者
   */
  public static void unRegister(Class<? extends Event> eventClass, Subscriber subscriber) {
    CopyOnWriteArraySet<Subscriber> set = SUBSCRIBER_MAP.get(eventClass);
    if (set != null) {
      set.remove(subscriber);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("UnRegister subscriber: {} of event: {}.", subscriber, eventClass);
      }
    }
  }

  /**
   * 给事件总线中丢一个事件
   *
   * @param event 事件
   */
  public static void post(final Event event) {
    if (!isEnable()) {
      return;
    }
    CopyOnWriteArraySet<Subscriber> subscribers = SUBSCRIBER_MAP.get(event.getClass());
    if (isNotEmpty(subscribers)) {
      for (final Subscriber subscriber : subscribers) {
        if (subscriber.isSync()) {
          handleEvent(subscriber, event);
        } else { // 异步
          AsyncRuntimeExecutor.getAsyncThreadPool()
              .execute(
                  new Runnable() {
                    @Override
                    public void run() {
                      try {
                        handleEvent(subscriber, event);
                      } catch (Exception e) {
                      }
                    }
                  });
        }
      }
    }
  }

  private static void handleEvent(final Subscriber subscriber, final Event event) {
    try {
      subscriber.onEvent(event);
    } catch (Throwable e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Handle " + event.getClass() + " error", e);
      }
    }
  }
}
