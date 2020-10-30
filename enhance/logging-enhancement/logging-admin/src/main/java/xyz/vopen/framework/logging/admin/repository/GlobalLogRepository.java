package xyz.vopen.framework.logging.admin.repository;

import xyz.vopen.framework.logging.admin.model.GlobalLogModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.BaseMongoRepository;

/**
 * {@link GlobalLogRepository} Global log data repository interface
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public interface GlobalLogRepository extends BaseMongoRepository<GlobalLogModel> {}
