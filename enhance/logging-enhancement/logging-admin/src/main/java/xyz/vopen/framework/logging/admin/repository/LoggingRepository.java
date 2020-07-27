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

package xyz.vopen.framework.logging.admin.repository;

import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLog;
import xyz.vopen.framework.logging.core.response.LoggingResponse;
import xyz.vopen.framework.logging.core.response.ServiceResponse;

import java.sql.SQLException;
import java.util.List;

/**
 * Mixmicro Boot Logging Storage
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface LoggingRepository {
  /**
   * Insert Global Log
   *
   * @param requestLogId request log id
   * @param log {@link MixmicroGlobalLog}
   * @return the global log id
   * @throws SQLException Sql Exception
   */
  String insertGlobalLog(String requestLogId, MixmicroGlobalLog log) throws SQLException;

  /**
   * Insert ApiBootLogs To DataBase
   *
   * @param serviceDetailId ServiceDetail ID
   * @param log MinBoxLog
   * @return request log id
   * @throws SQLException Sql Exception
   */
  String insertLog(String serviceDetailId, MixmicroLog log) throws SQLException;

  /**
   * Insert ServiceDetail To DataBase
   *
   * @param serviceId ServiceId
   * @param serviceIp Service Ip Address
   * @param servicePort ServicePort
   * @return ServiceDetail Pk Value
   * @throws SQLException Sql Exception
   */
  String insertServiceDetail(String serviceId, String serviceIp, int servicePort)
      throws SQLException;

  /**
   * Select ServiceDetails Id
   *
   * @param serviceId Service Id
   * @param serviceIp Service Ip Address
   * @param servicePort Service Port
   * @return ServiceDetail Id
   * @throws SQLException Sql Exception
   */
  String selectServiceDetailId(String serviceId, String serviceIp, int servicePort)
      throws SQLException;

  /**
   * select all service {@link ServiceResponse}
   *
   * @return ServiceResponse
   * @throws SQLException Sql Exception
   */
  List<ServiceResponse> findAllService() throws SQLException;

  /**
   * select top logging list {@link LoggingResponse}
   *
   * @return LoggingResponse
   * @throws SQLException Sql Exception
   */
  List<LoggingResponse> findTopList(int topCount) throws SQLException;

  /**
   * Update ServiceDetail Last Report Time
   *
   * @param serviceDetailId ServiceDetail Pk Value
   * @throws SQLException Sql Exception
   */
  void updateLastReportTime(String serviceDetailId) throws SQLException;
}
