/*
 * Copyright (c) 2018 VOPEN.XYZ
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package xyz.vopen.framework.logging.core;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Mixmicro Boot Logging Client Notice Object
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Data
public class LoggingClientNotice implements Serializable {
  /** Client Service Id */
  private String clientServiceId;
  /** Client Service Ip Address */
  private String clientServiceIp;
  /** Client Service Port */
  private Integer clientServicePort;
  /** Report Time Millis */
  private Long reportTimeMillis = System.currentTimeMillis();
  /** Mixmicro Boot Logging Request Log */
  private List<MixmicroLog> loggers = new ArrayList<>();

  /**
   * Create new {@link LoggingClientNotice} instance
   *
   * @param clientServiceId {@link #clientServiceId}
   * @param clientServiceIp {@link #clientServiceIp}
   * @param clientServicePort {@link #clientServicePort}
   * @param loggers {@link #loggers}
   * @return {@link LoggingClientNotice}
   */
  public static LoggingClientNotice instance(
      String clientServiceId,
      String clientServiceIp,
      Integer clientServicePort,
      List<MixmicroLog> loggers) {
    LoggingClientNotice notice = new LoggingClientNotice();
    notice.setClientServiceId(clientServiceId);
    notice.setClientServiceIp(clientServiceIp);
    notice.setClientServicePort(clientServicePort);
    notice.setLoggers(loggers);
    return notice;
  }
}
