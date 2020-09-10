package xyz.vopen.framework.logging.core.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.core.model.LogServiceDetailModel;
import xyz.vopen.framework.logging.core.mongodb.BaseMongoRepositoryImpl;
import xyz.vopen.framework.logging.core.repository.LogServiceDetailRepository;

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
