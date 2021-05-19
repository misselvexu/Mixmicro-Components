package xyz.vopen.framework.logging.admin.service.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import xyz.vopen.framework.logging.admin.model.GlobalLogModel;
import xyz.vopen.framework.logging.admin.model.LogServiceDetailModel;
import xyz.vopen.framework.logging.admin.model.RequestLogModel;
import xyz.vopen.framework.logging.admin.repository.GlobalLogRepository;
import xyz.vopen.framework.logging.admin.repository.LogServiceDetailRepository;
import xyz.vopen.framework.logging.admin.repository.RequestLogRepository;
import xyz.vopen.framework.logging.admin.repository.impl.GlobalLogRepositoryImpl;
import xyz.vopen.framework.logging.admin.repository.impl.LogServiceDetailRepositoryImpl;
import xyz.vopen.framework.logging.admin.repository.impl.RequestLogRepositoryImpl;
import xyz.vopen.framework.logging.admin.service.LoggingDataService;
import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLog;

import java.util.UUID;

/**
 * {@link LoggingDataServiceImpl} Mixmicro logging data operation service implements
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public class LoggingDataServiceImpl implements LoggingDataService {
  public static final String BEAN_NAME = "LoggingDataRepository";
  /** Global log data repository interface */
  private final GlobalLogRepository globalLogRepository;
  /** Mixmicro Boot Log repository interface */
  private final RequestLogRepository requestLogRepository;
  /** logging service details repository interface */
  private final LogServiceDetailRepository logServiceDetailRepository;

  /**
   * init instance constructor
   *
   * @param mongoTemplate spring data MongoTemplate
   */
  public LoggingDataServiceImpl(MongoTemplate mongoTemplate) {
    this.globalLogRepository = new GlobalLogRepositoryImpl(mongoTemplate);
    this.requestLogRepository = new RequestLogRepositoryImpl(mongoTemplate);
    this.logServiceDetailRepository = new LogServiceDetailRepositoryImpl(mongoTemplate);
  }

  /**
   * Insert Global Log
   *
   * @param requestLogId request log id
   * @param globalLog {@link MixmicroGlobalLog}
   */
  @Override
  public void insertGlobalLog(String requestLogId, MixmicroGlobalLog globalLog) {
    GlobalLogModel globalLogModel = new GlobalLogModel();
    String id = UUID.randomUUID().toString();
    globalLogModel.setId(id);
    globalLogModel.setRequestLogId(requestLogId);
    globalLogModel.setLevel(globalLog.getLevel());
    globalLogModel.setContent(globalLog.getContent());
    globalLogModel.setCallerClass(globalLog.getCallerClass());
    globalLogModel.setCallerMethod(globalLog.getCallerMethod());
    globalLogModel.setCallerCodeLineNumber(globalLog.getCallerCodeLineNumber());
    globalLogModel.setExceptionStack(globalLog.getExceptionStack());
    globalLogModel.setServiceId(globalLog.getServiceId());
    globalLogModel.setServiceDetailId(globalLog.getServiceDetailId());
    if (null == globalLog.getCreateTime()) {
      globalLogModel.setCreateTime(System.currentTimeMillis());
    } else {
      globalLogModel.setCreateTime(globalLog.getCreateTime());
    }
    globalLogRepository.insert(globalLogModel);
  }

  /**
   * Insert request log
   *
   * @param serviceDetailId ServiceDetail ID
   * @param requestLog MinBoxLog
   * @return request log id
   */
  @Override
  public String insertRequestLog(String serviceDetailId, MixmicroLog requestLog) {
    RequestLogModel requestLogModel = new RequestLogModel();
    String id = UUID.randomUUID().toString();
    requestLogModel.setId(id);
    requestLogModel.setServiceDetailId(serviceDetailId);
    requestLogModel.setTraceId(requestLog.getTraceId());
    requestLogModel.setParentSpanId(requestLog.getParentSpanId());
    requestLogModel.setSpanId(requestLog.getSpanId());
    requestLogModel.setStartTime(requestLog.getStartTime());
    requestLogModel.setEndTime(requestLog.getEndTime());
    requestLogModel.setHttpStatus(requestLog.getHttpStatus());
    requestLogModel.setRequestBody(requestLog.getRequestBody());
    requestLogModel.setRequestHeaders(requestLog.getRequestHeaders());
    requestLogModel.setRequestIp(requestLog.getRequestIp());
    requestLogModel.setRequestMethod(requestLog.getRequestMethod());
    requestLogModel.setRequestUri(requestLog.getRequestUri());
    requestLogModel.setResponseBody(requestLog.getResponseBody());
    requestLogModel.setResponseHeaders(requestLog.getResponseHeaders());
    requestLogModel.setTimeConsuming(requestLog.getTimeConsuming());
    requestLogModel.setRequestParams(requestLog.getRequestParam());
    requestLogModel.setExceptionStack(requestLog.getExceptionStack());
    requestLogModel.setServiceId(requestLog.getServiceId());
    requestLogModel.setServiceDetailId(requestLog.getServiceDetailId());
    requestLogRepository.insert(requestLogModel);
    return id;
  }

  /**
   * @param serviceId ServiceId
   * @param serviceIp Service Ip Address
   * @param servicePort ServicePort
   * @return log service id
   */
  @Override
  public String insertServiceDetailIfAbsent(String serviceId, String serviceIp, int servicePort) {
    LogServiceDetailModel logServiceDetailModel = new LogServiceDetailModel();
    String id = formatServiceDetailID(serviceId, serviceIp, servicePort);
    logServiceDetailModel =
        logServiceDetailRepository.findOne(
            logServiceDetailModel, Query.query(Criteria.where("_id").is(id)));
    if (null == logServiceDetailModel) {
      logServiceDetailModel = new LogServiceDetailModel();
      logServiceDetailModel.setId(id);
      logServiceDetailModel.setServiceId(serviceId);
      logServiceDetailModel.setServiceIp(serviceIp);
      logServiceDetailModel.setServicePort(servicePort);
      logServiceDetailModel.setCreateTime(System.currentTimeMillis());
      logServiceDetailModel.setLastReportTime(System.currentTimeMillis());
      logServiceDetailRepository.insert(logServiceDetailModel);
    }
    return id;
  }

  @Override
  public void updateLogServiceDetailLastReportTime(String serviceDetailId) {
    Criteria criteria = Criteria.where("id").is(serviceDetailId);
    LogServiceDetailModel logServiceDetailModel =
        logServiceDetailRepository.findOne(new LogServiceDetailModel(), Query.query(criteria));
    if (null != logServiceDetailModel) {
      logServiceDetailModel.setLastReportTime(System.currentTimeMillis());
      logServiceDetailRepository.save(logServiceDetailModel);
    }
  }

  /**
   * format serviceDetail ID
   *
   * @param serviceId service id
   * @param serviceIp service ip address
   * @param servicePort service port
   * @return service detail id
   */
  private String formatServiceDetailID(String serviceId, String serviceIp, Integer servicePort) {
    Assert.notNull(serviceId, "Service Id Is Required.");
    Assert.notNull(serviceIp, "Service Ip Is Required.");
    Assert.notNull(servicePort, "Service Port Is Required.");
    return String.format("%s-%s:%d", serviceId, serviceIp, servicePort);
  }
}
