package xyz.vopen.framework.logging.admin.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.admin.model.LogServiceDetailModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.impl.BaseMongoRepositoryImpl;
import xyz.vopen.framework.logging.admin.repository.LogServiceDetailRepository;

/**
 * logging service details repository interface im
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public class LogServiceDetailRepositoryImpl extends BaseMongoRepositoryImpl<LogServiceDetailModel> implements LogServiceDetailRepository {
    /**
     * set spring data mongodb template
     *
     * @param mongoTemplate
     */
    public LogServiceDetailRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
