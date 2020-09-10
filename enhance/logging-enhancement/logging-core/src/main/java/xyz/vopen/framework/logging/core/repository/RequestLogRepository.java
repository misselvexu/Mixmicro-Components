package xyz.vopen.framework.logging.core.repository;

import xyz.vopen.framework.logging.core.model.RequestLogModel;
import xyz.vopen.framework.logging.core.mongodb.BaseMongoRepository;

/**
 * Mixmicro Boot Log repository interface
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public interface RequestLogRepository extends BaseMongoRepository<RequestLogModel> {
}
