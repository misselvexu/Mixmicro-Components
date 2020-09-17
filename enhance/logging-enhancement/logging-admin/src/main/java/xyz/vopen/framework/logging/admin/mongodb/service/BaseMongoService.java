package xyz.vopen.framework.logging.admin.mongodb.service;

import org.springframework.data.mongodb.core.query.Query;
import xyz.vopen.framework.logging.admin.mongodb.response.PageResponse;

import java.util.List;

/**
 * mongodb base crud service
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/16
 */
public interface BaseMongoService<E> {

    /**
     * @param entity
     * @param query
     * @return
     */
    E get(E entity, Query query);

    /**
     * @param entity
     * @param query
     * @return
     */
    List<E> find(E entity, Query query);

    /**
     * @param entity
     * @param query
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageResponse<E> find(E entity, Query query, int pageNum, int pageSize, String orderBy);

    /**
     * @param entity
     * @param query
     * @return
     */
    long count(E entity, Query query);

    /**
     * @param entity
     */
    void add(E entity);

    /**
     * @param entity
     */
    void update(E entity);

    /**
     * @param entity
     */
    void remove(E entity);
}
