package xyz.vopen.framework.logging.admin.repository;

import xyz.vopen.framework.logging.admin.model.RequestLogModel;
import xyz.vopen.framework.logging.admin.mongodb.repository.BaseMongoRepository;

/**
 * {@link RequestLogRepository} Mixmicro Boot Log repository interface
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public interface RequestLogRepository extends BaseMongoRepository<RequestLogModel> {}
