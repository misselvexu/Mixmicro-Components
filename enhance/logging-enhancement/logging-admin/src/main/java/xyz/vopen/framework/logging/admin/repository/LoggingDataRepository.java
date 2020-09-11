package xyz.vopen.framework.logging.admin.repository;

import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLog;
import xyz.vopen.framework.logging.admin.model.LogServiceDetailModel;
import xyz.vopen.framework.logging.core.response.LoggingResponse;
import xyz.vopen.framework.logging.core.response.ServiceResponse;

import java.sql.SQLException;
import java.util.List;

/**
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public interface LoggingDataRepository {

    /**
     * Insert Global Log
     *
     * @param requestLogId request log id
     * @param log          {@link MixmicroGlobalLog}
     * @return the global log id
     */
    String insertGlobalLog(String requestLogId, MixmicroGlobalLog log);

    /**
     * Insert ApiBootLogs To DataBase
     *
     * @param serviceDetailId ServiceDetail ID
     * @param log             MinBoxLog
     * @return request log id
     */
    String insertRequestLog(String serviceDetailId, MixmicroLog log) throws SQLException;

    /**
     * Insert ServiceDetail To DataBase
     *
     * @param serviceId   ServiceId
     * @param serviceIp   Service Ip Address
     * @param servicePort ServicePort
     * @return ServiceDetail Pk Value
     */
    String insertLogServiceDetail(String serviceId, String serviceIp, int servicePort)
            throws SQLException;

    /**
     * Select ServiceDetails Id
     *
     * @param serviceId   Service Id
     * @param serviceIp   Service Ip Address
     * @param servicePort Service Port
     * @return ServiceDetail Id
     */
    String selectLogServiceDetailId(String serviceId, String serviceIp, int servicePort);

    /**
     * select all service {@link ServiceResponse}
     *
     * @return LogServiceDetailModel
     */
    List<LogServiceDetailModel> findAllServiceDetails();

    /**
     * select top logging list {@link LoggingResponse}
     *
     * @return LoggingResponse
     */
    List<LoggingResponse> findTopList(int topCount);

    /**
     * Update ServiceDetail Last Report Time
     *
     * @param serviceDetailId ServiceDetail Pk Value
     */
    void updateLogServiceDetailLastReportTime(String serviceDetailId);
}
