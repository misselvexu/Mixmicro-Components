package xyz.vopen.framework.logging.admin.mongodb.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import xyz.vopen.framework.logging.admin.mongodb.helper.MongoHelper;
import xyz.vopen.framework.logging.admin.mongodb.repository.BaseMongoRepository;

import java.util.List;

/**
 * {@link BaseMongoRepositoryImpl} spring data mongodb base crud implements
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2020/9/8
 */
public abstract class BaseMongoRepositoryImpl<E> implements BaseMongoRepository<E> {

  protected MongoTemplate mongoTemplate;

  /**
   * set mongoTemplate
   *
   * @param template spring data MongoTemplate
   */
  @SuppressWarnings("unused")
  public BaseMongoRepositoryImpl<E> setMongoTemplate(MongoTemplate template) {
    mongoTemplate = template;
    return this;
  }

  @Override
  public E findOne(E entity, Query query) {
    String collectionName = MongoHelper.getCollectionName(entity);
    if (query == null) {
      query = new Query();
    }

    return mongoTemplate.findOne(query, MongoHelper.getEntityClass(entity), collectionName);
  }

  @Override
  public List<E> find(E entity, Query query) {
    String collectionName = MongoHelper.getCollectionName(entity);
    if (query == null) {
      return mongoTemplate.findAll(MongoHelper.getEntityClass(entity), collectionName);
    }

    return mongoTemplate.find(query, MongoHelper.getEntityClass(entity), collectionName);
  }

  @Override
  public long count(E entity, Query query) {
    String collectionName = MongoHelper.getCollectionName(entity);
    if (query == null) {
      query = new Query();
    }
    return mongoTemplate.count(query, MongoHelper.getEntityClass(entity), collectionName);
  }

  @Override
  public void insert(E entity) {
    mongoTemplate.insert(entity, MongoHelper.getCollectionName(entity));
  }

  @Override
  public void insertMany(List<E> entities) {
    if (CollectionUtils.isEmpty(entities)) {
      return;
    }
    mongoTemplate.insert(entities, MongoHelper.getCollectionName(entities.get(0)));
  }

  @Override
  public void save(E entity) {
    mongoTemplate.save(entity, MongoHelper.getCollectionName(entity));
  }

  @Override
  public void updateOne(E entity, Query query, Update update) {
    mongoTemplate.updateFirst(query, update, MongoHelper.getCollectionName(entity));
  }

  @Override
  public void updateMulti(E entity, Query query, Update update) {
    mongoTemplate.updateMulti(query, update, MongoHelper.getCollectionName(entity));
  }

  @Override
  public void remove(E entity) {
    mongoTemplate.remove(entity, MongoHelper.getCollectionName(entity));
  }

  @Override
  public void removeByQuery(Query query, E entity) {
    mongoTemplate.remove(entity, MongoHelper.getCollectionName(entity));
  }
}
