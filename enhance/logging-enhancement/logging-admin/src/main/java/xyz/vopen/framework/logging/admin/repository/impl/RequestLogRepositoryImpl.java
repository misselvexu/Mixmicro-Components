package xyz.vopen.framework.logging.admin.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.admin.model.RequestLogModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.impl.BaseMongoRepositoryImpl;
import xyz.vopen.framework.logging.admin.repository.RequestLogRepository;

/**
 * {@link RequestLogRepositoryImpl} Mixmicro Boot Log repository interface implements
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public class RequestLogRepositoryImpl extends BaseMongoRepositoryImpl<RequestLogModel>
    implements RequestLogRepository {
  /**
   * set spring data mongodb template
   *
   * @param mongoTemplate spring data MongoTemplate
   */
  public RequestLogRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }
}
