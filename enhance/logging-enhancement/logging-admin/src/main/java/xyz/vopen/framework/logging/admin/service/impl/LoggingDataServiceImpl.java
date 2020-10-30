package xyz.vopen.framework.logging.admin.service.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
   * @param log {@link MixmicroGlobalLog}
   */
  @Override
  public void insertGlobalLog(String requestLogId, MixmicroGlobalLog log) {
    GlobalLogModel globalLogModel = new GlobalLogModel();
    String id = UUID.randomUUID().toString();
    globalLogModel.setId(id);
    globalLogModel.setRequestLogId(requestLogId);
    globalLogModel.setLevel(log.getLevel());
    globalLogModel.setContent(log.getContent());
    globalLogModel.setCallerClass(log.getCallerClass());
    globalLogModel.setCallerMethod(log.getCallerMethod());
    globalLogModel.setCallerCodeLineNumber(log.getCallerCodeLineNumber());
    globalLogModel.setExceptionStack(log.getExceptionStack());
    if (null == log.getCreateTime()) {
      globalLogModel.setCreateTime(System.currentTimeMillis());
    } else {
      globalLogModel.setCreateTime(log.getCreateTime());
    }
    globalLogRepository.insert(globalLogModel);
  }

  /**
   * Insert request log
   *
   * @param serviceDetailId ServiceDetail ID
   * @param log MinBoxLog
   * @return request log id
   */
  @Override
  public String insertRequestLog(String serviceDetailId, MixmicroLog log) {
    RequestLogModel requestLogModel = new RequestLogModel();
    String id = UUID.randomUUID().toString();
    requestLogModel.setId(id);
    requestLogModel.setServiceDetailId(serviceDetailId);
    requestLogModel.setTraceId(log.getTraceId());
    requestLogModel.setParentSpanId(log.getParentSpanId());
    requestLogModel.setSpanId(log.getSpanId());
    requestLogModel.setStartTime(log.getStartTime());
    requestLogModel.setEndTime(log.getEndTime());
    requestLogModel.setHttpStatus(log.getHttpStatus());
    requestLogModel.setRequestBody(log.getRequestBody());
    requestLogModel.setRequestHeaders(log.getRequestHeaders());
    requestLogModel.setRequestIp(log.getRequestIp());
    requestLogModel.setRequestMethod(log.getRequestMethod());
    requestLogModel.setRequestUri(log.getRequestUri());
    requestLogModel.setResponseBody(log.getResponseBody());
    requestLogModel.setResponseHeaders(log.getResponseHeaders());
    requestLogModel.setTimeConsuming(log.getTimeConsuming());
    requestLogModel.setRequestParams(log.getRequestParam());
    requestLogModel.setExceptionStack(log.getExceptionStack());
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
  public String insertLogServiceDetail(String serviceId, String serviceIp, int servicePort) {
    LogServiceDetailModel logServiceDetailModel = new LogServiceDetailModel();
    String id = UUID.randomUUID().toString();
    logServiceDetailModel.setId(id);
    logServiceDetailModel.setServiceId(serviceId);
    logServiceDetailModel.setServiceIp(serviceIp);
    logServiceDetailModel.setServicePort(servicePort);
    logServiceDetailModel.setCreateTime(System.currentTimeMillis());
    logServiceDetailModel.setLastReportTime(System.currentTimeMillis());
    logServiceDetailRepository.insert(logServiceDetailModel);
    return id;
  }

  /**
   * get service id by service detail
   *
   * @param serviceId Service Id
   * @param serviceIp Service Ip Address
   * @param servicePort Service Port
   * @return log service detail id
   */
  @Override
  public String selectLogServiceDetailId(String serviceId, String serviceIp, int servicePort) {
    Criteria criteria =
        Criteria.where("serviceId")
            .is(serviceId)
            .and("serviceIp")
            .is(serviceIp)
            .and("servicePort")
            .is(servicePort);
    LogServiceDetailModel logServiceDetailModel =
        logServiceDetailRepository.findOne(new LogServiceDetailModel(), Query.query(criteria));
    if (null != logServiceDetailModel) {
      return logServiceDetailModel.getId();
    }
    return null;
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
}
