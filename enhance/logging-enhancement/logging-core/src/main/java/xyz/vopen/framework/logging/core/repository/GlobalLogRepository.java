package xyz.vopen.framework.logging.core.repository;

import xyz.vopen.framework.logging.core.model.GlobalLogModel;
import xyz.vopen.framework.logging.core.mongodb.BaseMongoRepository;

/**
 * Global log data repository interface
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public interface GlobalLogRepository extends BaseMongoRepository<GlobalLogModel> {
}
