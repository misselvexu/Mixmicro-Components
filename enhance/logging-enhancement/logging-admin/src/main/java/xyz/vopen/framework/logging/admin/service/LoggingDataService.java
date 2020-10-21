package xyz.vopen.framework.logging.admin.service;

import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLog;

import java.sql.SQLException;

/**
 * {@link LoggingDataService} Mixmicro logging data operation service
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public interface LoggingDataService {

  /**
   * Insert Global Log
   *
   * @param requestLogId request log id
   * @param log {@link MixmicroGlobalLog}
   */
  void insertGlobalLog(String requestLogId, MixmicroGlobalLog log);

  /**
   * Insert ApiBootLogs To DataBase
   *
   * @param serviceDetailId ServiceDetail ID
   * @param log MinBoxLog
   * @return request log id
   */
  String insertRequestLog(String serviceDetailId, MixmicroLog log) throws SQLException;

  /**
   * Insert ServiceDetail To DataBase
   *
   * @param serviceId ServiceId
   * @param serviceIp Service Ip Address
   * @param servicePort ServicePort
   * @return ServiceDetail Pk Value
   */
  String insertLogServiceDetail(String serviceId, String serviceIp, int servicePort);

  /**
   * Select ServiceDetails Id
   *
   * @param serviceId Service Id
   * @param serviceIp Service Ip Address
   * @param servicePort Service Port
   * @return ServiceDetail Id
   */
  String selectLogServiceDetailId(String serviceId, String serviceIp, int servicePort);

  /**
   * Update ServiceDetail Last Report Time
   *
   * @param serviceDetailId ServiceDetail Pk Value
   */
  void updateLogServiceDetailLastReportTime(String serviceDetailId);
}
