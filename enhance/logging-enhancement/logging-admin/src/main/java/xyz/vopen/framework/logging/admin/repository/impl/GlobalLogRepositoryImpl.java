package xyz.vopen.framework.logging.admin.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.admin.model.GlobalLogModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.impl.BaseMongoRepositoryImpl;
import xyz.vopen.framework.logging.admin.repository.GlobalLogRepository;

/**
 * {@link GlobalLogRepositoryImpl} Global log data repository interface implements
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public class GlobalLogRepositoryImpl extends BaseMongoRepositoryImpl<GlobalLogModel>
    implements GlobalLogRepository {
  /**
   * set spring data mongodb template
   *
   * @param mongoTemplate spring data MongoTemplate
   */
  public GlobalLogRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }
}
