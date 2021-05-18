package xyz.vopen.framework.logging.admin.mongodb.service;

import org.springframework.data.mongodb.core.query.Query;
import xyz.vopen.framework.logging.admin.mongodb.response.PageResponse;

import java.util.List;

/**
 * {@link BaseMongoService} mongodb base crud service
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public interface BaseMongoService<E> {

  /**
   * @param entity mapping entity
   * @param query spring data mongo query
   * @return result
   */
  E get(E entity, Query query);

  /**
   * @param entity mapping entity
   * @param query spring data mongo query
   * @return result
   */
  List<E> find(E entity, Query query);

  /**
   * @param entity mapping entity
   * @param query spring data mongo query
   * @param pageNum page number
   * @param pageSize one page size
   * @return result
   */
  PageResponse<E> find(E entity, Query query, int pageNum, int pageSize);

  /**
   * @param entity mapping entity
   * @param query spring data mongo query
   * @return result
   */
  long count(E entity, Query query);

  /** @param entity mapping entity */
  void add(E entity);

  /** @param entity mapping entity */
  void update(E entity);

  /** @param entity mapping entity */
  void remove(E entity);
}
