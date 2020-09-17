package xyz.vopen.framework.logging.admin.mongodb.service.impl;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import xyz.vopen.framework.logging.admin.mongodb.helper.MongoPageHelper;
import xyz.vopen.framework.logging.admin.mongodb.repository.BaseMongoRepository;
import xyz.vopen.framework.logging.admin.mongodb.response.PageResponse;
import xyz.vopen.framework.logging.admin.mongodb.service.BaseMongoService;

import java.util.List;

/**
 * mongodb base crud service impl
 *
 * @author tino
 * @date 2019-07-12
 */
public abstract class BaseMongoServiceImpl<R extends BaseMongoRepository<E>, E> implements BaseMongoService<E> {

    protected R repository;

    /**
     * set mongodb repository
     *
     * @param repository
     * @return
     */
    public BaseMongoServiceImpl setRepository(R repository) {
        if (null == this.repository) {
            this.repository = repository;
        }
        return this;
    }

    @Override
    public E get(E entity, Query query) {
        return repository.findOne(entity, query);
    }

    @Override
    public List<E> find(E entity, Query query) {
        return repository.find(entity, query);
    }

    @Override
    public PageResponse<E> find(E entity, Query query, int pageNum, int pageSize, String orderBy) {
        PageRequest pageRequest = MongoPageHelper.startPage(pageNum, pageSize, orderBy);
        if (query == null) {
            query = new Query();
        }
        long total = repository.count(entity, query);
        int pages = (int) (total / pageSize + (total % pageSize > 0 ? 1 : 0));
        List<E> list = repository.find(entity, query.with(pageRequest));
        return new PageResponse<>(list, pageRequest.getPageNumber(), pageRequest.getPageSize(), total, pages);
    }

    @Override
    public long count(E entity, Query query) {
        return repository.count(entity, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(E entity) {
        repository.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(E entity) {
        try {
            repository.save(entity);
        } catch (OptimisticLockingFailureException e) {
            throw new RuntimeException("mongodb update error", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(E entity) {
        repository.remove(entity);
    }

}
