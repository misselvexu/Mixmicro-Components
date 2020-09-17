package xyz.vopen.framework.logging.admin.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.admin.model.RequestLogModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.impl.BaseMongoRepositoryImpl;
import xyz.vopen.framework.logging.admin.repository.RequestLogRepository;

/**
 * Mixmicro Boot Log repository interface implements
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public class RequestLogRepositoryImpl extends BaseMongoRepositoryImpl<RequestLogModel> implements RequestLogRepository {
    /**
     * set spring data mongodb template
     *
     * @param mongoTemplate
     */
    public RequestLogRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
