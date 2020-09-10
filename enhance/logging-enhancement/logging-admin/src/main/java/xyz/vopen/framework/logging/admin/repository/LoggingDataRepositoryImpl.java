package xyz.vopen.framework.logging.admin.repository;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import xyz.vopen.framework.logging.core.MixmicroGlobalLog;
import xyz.vopen.framework.logging.core.MixmicroLog;
import xyz.vopen.framework.logging.core.model.GlobalLogModel;
import xyz.vopen.framework.logging.core.model.LogServiceDetailModel;
import xyz.vopen.framework.logging.core.model.RequestLogModel;
import xyz.vopen.framework.logging.core.mongodb.MongoPageHelper;
import xyz.vopen.framework.logging.core.repository.GlobalLogRepository;
import xyz.vopen.framework.logging.core.repository.LogServiceDetailRepository;
import xyz.vopen.framework.logging.core.repository.RequestLogRepository;
import xyz.vopen.framework.logging.core.repository.impl.GlobalLogRepositoryImpl;
import xyz.vopen.framework.logging.core.repository.impl.LogServiceDetailRepositoryImpl;
import xyz.vopen.framework.logging.core.repository.impl.RequestLogRepositoryImpl;
import xyz.vopen.framework.logging.core.response.LoggingResponse;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public class LoggingDataRepositoryImpl implements LoggingDataRepository {
    public static final String BEAN_NAME = "LoggingDataRepository";
    /**
     * Global log data repository interface
     */
    private GlobalLogRepository globalLogRepository;
    /**
     * Mixmicro Boot Log repository interface
     */
    private RequestLogRepository requestLogRepository;
    /**
     * logging service details repository interface
     */
    private LogServiceDetailRepository logServiceDetailRepository;

    /**
     * init instance constructor
     *
     * @param mongoTemplate
     */
    public LoggingDataRepositoryImpl(MongoTemplate mongoTemplate) {
        this.globalLogRepository = new GlobalLogRepositoryImpl(mongoTemplate);
        this.requestLogRepository = new RequestLogRepositoryImpl(mongoTemplate);
        this.logServiceDetailRepository = new LogServiceDetailRepositoryImpl(mongoTemplate);
    }

    /**
     * Insert Global Log
     *
     * @param requestLogId request log id
     * @param log          {@link MixmicroGlobalLog}
     * @return the global log id
     */
    @Override
    public String insertGlobalLog(String requestLogId, MixmicroGlobalLog log) {
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
        globalLogModel.setCreateTime(log.getCreateTime());
        globalLogRepository.insert(globalLogModel);
        return id;
    }

    /**
     * Insert request log
     *
     * @param serviceDetailId ServiceDetail ID
     * @param log             MinBoxLog
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
        requestLogModel.setRequestBody(log.getResponseBody());
        requestLogModel.setResponseHeaders(log.getResponseHeaders());
        requestLogModel.setTimeConsuming(log.getTimeConsuming());
        requestLogModel.setRequestParams(log.getRequestParam());
        requestLogModel.setExceptionStack(log.getExceptionStack());
        requestLogRepository.insert(requestLogModel);
        return id;
    }

    /**
     * @param serviceId   ServiceId
     * @param serviceIp   Service Ip Address
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
        logServiceDetailRepository.insert(logServiceDetailModel);
        return id;
    }

    /**
     * get service id by service detail
     *
     * @param serviceId   Service Id
     * @param serviceIp   Service Ip Address
     * @param servicePort Service Port
     * @return
     */
    @Override
    public String selectLogServiceDetailId(String serviceId, String serviceIp, int servicePort) {
        Criteria criteria =
                Criteria.where("serviceId").is(serviceId)
                        .and("serviceIp").is(serviceIp)
                        .and("servicePort").is(servicePort);
        LogServiceDetailModel logServiceDetailModel = logServiceDetailRepository.findOne(new LogServiceDetailModel(), Query.query(criteria));
        if (null != logServiceDetailModel) {
            return logServiceDetailModel.getId();
        }
        return null;
    }

    @Override
    public List<LogServiceDetailModel> findAllServiceDetails() {
        return logServiceDetailRepository.find(new LogServiceDetailModel(), null);
    }

    @Override
    public List<LoggingResponse> findTopList(int topCount) {
        PageRequest pageRequest = MongoPageHelper.startPage(1, topCount, null);
        List<RequestLogModel> requestLogModels = requestLogRepository.find(new RequestLogModel(), new Query().with(pageRequest));
        if (CollectionUtils.isEmpty(requestLogModels)) {
            return new ArrayList<>();
        }
        List<String> logServiceDetailIds = requestLogModels.stream().map(RequestLogModel::getServiceDetailId).collect(Collectors.toList());
        Map<String, LogServiceDetailModel> logServiceDetailModelMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(logServiceDetailIds)) {
            List<LogServiceDetailModel> logServiceDetailModels
                    = logServiceDetailRepository.find(new LogServiceDetailModel(), Query.query(Criteria.where("id").in(logServiceDetailIds)));
            if (!CollectionUtils.isEmpty(logServiceDetailModels)) {
                logServiceDetailModelMap = logServiceDetailModels.stream().collect(Collectors.toMap(LogServiceDetailModel::getId, Function.identity()));
            }
        }
        Map<String, LogServiceDetailModel> finalLogServiceDetailModelMap = logServiceDetailModelMap;
        List<LoggingResponse> loggingResponseList = requestLogModels.stream().map(requestLogModel -> {
            LoggingResponse loggingResponse = new LoggingResponse();
            BeanUtils.copyProperties(requestLogModel, loggingResponse);
            if (!CollectionUtils.isEmpty(finalLogServiceDetailModelMap)) {
                LogServiceDetailModel logServiceDetailModel = finalLogServiceDetailModelMap.get(requestLogModel.getServiceDetailId());
                if (null != logServiceDetailModel) {
                    loggingResponse.setCreateTime(new Timestamp(logServiceDetailModel.getCreateTime()));
                }
            }
            return loggingResponse;
        }).collect(Collectors.toList());
        return loggingResponseList;
    }

    @Override
    public void updateLogServiceDetailLastReportTime(String serviceDetailId) {
        Criteria criteria =
                Criteria.where("id").is(serviceDetailId);
        LogServiceDetailModel logServiceDetailModel = logServiceDetailRepository.findOne(new LogServiceDetailModel(), Query.query(criteria));
        if (null != logServiceDetailModel) {
            logServiceDetailModel.setLastReportTime(System.currentTimeMillis());
            logServiceDetailRepository.save(logServiceDetailModel);
        }
    }
}
