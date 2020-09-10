package xyz.vopen.framework.logging.core.mongodb;

import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * spring data mongodb base crud implements
 *
 * @author tino
 * @version ${project.version}
 * @date 2020/9/8
 */
public abstract class BaseMongoRepositoryImpl<E> implements BaseMongoRepository<E> {

    /**
     * mongodb transaction switch:true-on,false-off
     */
    protected static Boolean transactionEnabled = false;
    protected MongoTemplate mongoTemplate;

    /**
     * set mongoTemplate
     *
     * @param template
     */
    public BaseMongoRepositoryImpl setMongoTemplate(MongoTemplate template) {
        mongoTemplate = template;
        if (transactionEnabled && null != mongoTemplate) {
            // turn on the mongondb transaction switch
            mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        }
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
        //  when the mongodb transaction is on, this will not supported on count sql
        if (transactionEnabled) {
            // the replace way is getting the result data size to count,bus this is unstable
            return this.find(entity, query).size();
        }
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
