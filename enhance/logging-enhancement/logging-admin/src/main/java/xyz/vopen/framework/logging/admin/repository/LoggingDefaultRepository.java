package xyz.vopen.framework.logging.admin.repository;

import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLog;
import xyz.vopen.framework.logging.core.response.LoggingResponse;
import xyz.vopen.framework.logging.core.response.ServiceResponse;

import java.sql.SQLException;
import java.util.List;

/**
 * The {@link LoggingRepository} default support
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingDefaultRepository implements LoggingRepository {
  @Override
  public String insertGlobalLog(String requestLogId, MixmicroGlobalLog log) throws SQLException {
    return null;
  }

  @Override
  public String insertLog(String serviceDetailId, MixmicroLog log) throws SQLException {
    return null;
  }

  @Override
  public String insertServiceDetail(String serviceId, String serviceIp, int servicePort)
      throws SQLException {
    return null;
  }

  @Override
  public String selectServiceDetailId(String serviceId, String serviceIp, int servicePort)
      throws SQLException {
    return null;
  }

  @Override
  public List<ServiceResponse> findAllService() throws SQLException {
    return null;
  }

  @Override
  public List<LoggingResponse> findTopList(int topCount) throws SQLException {
    return null;
  }

  @Override
  public void updateLastReportTime(String serviceDetailId) throws SQLException {}
}
