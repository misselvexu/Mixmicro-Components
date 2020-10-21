package xyz.vopen.framework.logging.admin.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import xyz.vopen.framework.logging.admin.model.LogServiceDetailModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.impl.BaseMongoRepositoryImpl;
import xyz.vopen.framework.logging.admin.repository.LogServiceDetailRepository;

/**
 * {@link LogServiceDetailRepositoryImpl} logging service details repository interface im
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public class LogServiceDetailRepositoryImpl extends BaseMongoRepositoryImpl<LogServiceDetailModel>
    implements LogServiceDetailRepository {
  /**
   * set spring data mongodb template
   *
   * @param mongoTemplate spring data MongoTemplate
   */
  public LogServiceDetailRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }
}
