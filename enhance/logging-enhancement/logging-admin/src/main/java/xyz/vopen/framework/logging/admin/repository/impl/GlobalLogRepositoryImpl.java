package xyz.vopen.framework.logging.admin.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.admin.model.GlobalLogModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.impl.BaseMongoRepositoryImpl;
import xyz.vopen.framework.logging.admin.repository.GlobalLogRepository;

/**
 * Global log data repository interface implements
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public class GlobalLogRepositoryImpl extends BaseMongoRepositoryImpl<GlobalLogModel> implements GlobalLogRepository {
    /**
     * set spring data mongodb template
     *
     * @param mongoTemplate
     */
    public GlobalLogRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
