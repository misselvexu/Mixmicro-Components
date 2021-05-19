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
   * @param globalLog {@link MixmicroGlobalLog}
   */
  void insertGlobalLog(String requestLogId, MixmicroGlobalLog globalLog);

  /**
   * Insert ApiBootLogs To DataBase
   *
   * @param requestLog MinBoxLog
   * @return request log id
   */
  String insertRequestLog(MixmicroLog requestLog) throws SQLException;

  /**
   * Insert ServiceDetail To DataBase
   *
   * @param serviceId ServiceId
   * @param serviceIp Service Ip Address
   * @param servicePort ServicePort
   * @return ServiceDetail Pk Value
   */
  String insertServiceDetailIfAbsent(String serviceId, String serviceIp, int servicePort);

  /**
   * Update ServiceDetail Last Report Time
   *
   * @param serviceDetailId ServiceDetail Pk Value
   */
  void updateLogServiceDetailLastReportTime(String serviceDetailId);
}
