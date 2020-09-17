package xyz.vopen.framework.logging.admin.repository;

import xyz.vopen.framework.logging.admin.model.GlobalLogModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.BaseMongoRepository;

/**
 * Global log data repository interface
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public interface GlobalLogRepository extends BaseMongoRepository<GlobalLogModel> {
}
