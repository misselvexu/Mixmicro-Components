package xyz.vopen.framework.logging.admin.repository;

import xyz.vopen.framework.logging.admin.model.RequestLogModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.BaseMongoRepository;

/**
 * Mixmicro Boot Log repository interface
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public interface RequestLogRepository extends BaseMongoRepository<RequestLogModel> {
}
