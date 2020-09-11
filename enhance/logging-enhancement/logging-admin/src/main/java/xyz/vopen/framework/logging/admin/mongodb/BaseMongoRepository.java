package xyz.vopen.framework.logging.admin.mongodb;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * base mongodb mapper repository
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public interface BaseMongoRepository<T> {

    T findOne(T entity, Query query);

    List<T> find(T entity, Query query);

    long count(T entity, Query query);

    void insert(T entity);

    void insertMany(List<T> entities);

    void save(T entity);

    void updateOne(T entity, Query query, Update update);

    void updateMulti(T entity, Query query, Update update);

    void remove(T entity);

    void removeByQuery(Query query, T entity);
}
