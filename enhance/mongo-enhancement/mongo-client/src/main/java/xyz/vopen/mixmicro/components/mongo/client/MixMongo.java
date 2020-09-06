package xyz.vopen.mixmicro.components.mongo.client;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Embedded;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Entity;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappingException;
import xyz.vopen.mixmicro.components.mongo.client.mapping.cache.EntityCache;
import xyz.vopen.mixmicro.components.mongo.client.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;

/**
 * {@link MixMongo}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings("ALL")
public class MixMongo {
  private static final Logger LOG = LoggerFactory.getLogger(MixMongo.class);
  private final Mapper mapper;

  /** Creates a MixMongo instance with a default Mapper and an empty class set. */
  public MixMongo() {
    this(new Mapper(), Collections.<Class>emptySet());
  }

  /**
   * Creates a MixMongo instance with the given Mapper and class set.
   *
   * @param mapper the Mapper to use
   * @param classesToMap the classes to map
   */
  public MixMongo(final Mapper mapper, final Set<Class> classesToMap) {
    this.mapper = mapper;
    for (final Class c : classesToMap) {
      map(c);
    }
  }

  /**
   * Creates a MixMongo instance with the given Mapper
   *
   * @param mapper the Mapper to use
   */
  public MixMongo(final Mapper mapper) {
    this(mapper, Collections.<Class>emptySet());
  }

  /**
   * Creates a MixMongo instance with the given classes
   *
   * @param classesToMap the classes to map
   */
  public MixMongo(final Set<Class> classesToMap) {
    this(new Mapper(), classesToMap);
  }

  /**
   * It is best to use a Mongo singleton instance here.
   *
   * @param mongoClient the representations of the connection to a MongoDB instance
   * @param dbName the name of the database
   * @return a MongoRepository that you can use to interact with MongoDB
   */
  @SuppressWarnings("deprecation")
  public MongoRepository createMongoRepository(final MongoClient mongoClient, final String dbName) {
    return new DefaultMongoRepository(this, mongoClient, dbName);
  }

  /**
   * Creates a new MongoRepository for interacting with MongoDB using POJOs
   *
   * @param mongoClient the representations of the connection to a MongoDB instance
   * @param mapper a pre-configured Mapper for your POJOs
   * @param dbName the name of the database
   * @return a MongoRepository that you can use to interact with MongoDB
   */
  @SuppressWarnings("deprecation")
  public MongoRepository createMongoRepository(
      final MongoClient mongoClient, final Mapper mapper, final String dbName) {
    return new DefaultMongoRepository(this, mapper, mongoClient, dbName);
  }

  /**
   * Creates an entity and populates its state based on the dbObject given. This method is primarily
   * an internal method. Reliance on this method may break your application in future releases.
   *
   * @param <T> type of the entity
   * @param mongoRepository the MongoRepository to use when fetching this reference
   * @param entityClass type to create
   * @param dbObject the object state to use
   * @return the newly created and populated entity
   */
  public <T> T fromDBObject(
      final MongoRepository mongoRepository, final Class<T> entityClass, final DBObject dbObject) {
    return fromDBObject(mongoRepository, entityClass, dbObject, mapper.createEntityCache());
  }

  /**
   * Creates an entity and populates its state based on the dbObject given. This method is primarily
   * an internal method. Reliance on this method may break your application in future releases.
   *
   * @param <T> type of the entity
   * @param mongoRepository the MongoRepository to use when fetching this reference
   * @param entityClass type to create
   * @param dbObject the object state to use
   * @param cache the EntityCache to use to prevent multiple loads of the same entities over and
   *     over
   * @return the newly created and populated entity
   */
  public <T> T fromDBObject(
      final MongoRepository mongoRepository,
      final Class<T> entityClass,
      final DBObject dbObject,
      final EntityCache cache) {
    if (!entityClass.isInterface() && !mapper.isMapped(entityClass)) {
      throw new MappingException("Trying to map to an unmapped class: " + entityClass.getName());
    }
    try {
      return mapper.fromDBObject(mongoRepository, entityClass, dbObject, cache);
    } catch (Exception e) {
      throw new MappingException("Could not map entity from DBObject", e);
    }
  }

  /** @return the mapper used by this instance of MixMongo */
  public Mapper getMapper() {
    return mapper;
  }

  /**
   * @return false. Setting this value has no value functionally or performance-wise.
   * @deprecated
   */
  @Deprecated
  public boolean getUseBulkWriteOperations() {
    return false;
  }

  /**
   * Check whether a specific class is mapped by this instance.
   *
   * @param entityClass the class we want to check
   * @return true if the class is mapped, else false
   */
  public boolean isMapped(final Class entityClass) {
    return mapper.isMapped(entityClass);
  }

  /**
   * @return false. Setting this value has no value functionally or performance-wise.
   * @deprecated
   */
  @Deprecated
  public boolean isUseBulkWriteOperations() {
    return false;
  }

  /**
   * Configures MixMongo to use bulk writes. Only useful with MongoDB 2.6+.
   *
   * @param useBulkWriteOperations true if MixMongo should use bulk writes
   * @deprecated Setting this value has no value functionally or performance-wise.
   */
  @Deprecated
  public void setUseBulkWriteOperations(final boolean useBulkWriteOperations) {}

  /**
   * Maps a set of classes
   *
   * @param entityClasses the classes to map
   * @return this
   */
  public synchronized MixMongo map(final Class... entityClasses) {
    if (entityClasses != null && entityClasses.length > 0) {
      for (final Class entityClass : entityClasses) {
        if (!mapper.isMapped(entityClass)) {
          mapper.addMappedClass(entityClass);
        }
      }
    }
    return this;
  }

  /**
   * Maps a set of classes
   *
   * @param entityClasses the classes to map
   * @return this
   */
  public synchronized MixMongo map(final Set<Class> entityClasses) {
    if (entityClasses != null && !entityClasses.isEmpty()) {
      for (final Class entityClass : entityClasses) {
        if (!mapper.isMapped(entityClass)) {
          mapper.addMappedClass(entityClass);
        }
      }
    }
    return this;
  }

  /**
   * Tries to map all classes in the package specified. Fails if one of the classes is not valid for
   * mapping.
   *
   * @param packageName the name of the package to process
   * @return the MixMongo instance
   */
  public synchronized MixMongo mapPackage(final String packageName) {
    return mapPackage(packageName, false);
  }

  /**
   * Tries to map all classes in the package specified.
   *
   * @param packageName the name of the package to process
   * @param ignoreInvalidClasses specifies whether to ignore classes in the package that cannot be
   *     mapped
   * @return the MixMongo instance
   */
  public synchronized MixMongo mapPackage(
      final String packageName, final boolean ignoreInvalidClasses) {
    try {
      for (final Class clazz :
          ReflectionUtils.getClasses(
              mapper.getOptions().getClassLoader(),
              packageName,
              mapper.getOptions().isMapSubPackages())) {
        try {
          final Embedded embeddedAnn = ReflectionUtils.getClassEmbeddedAnnotation(clazz);
          final Entity entityAnn = ReflectionUtils.getClassEntityAnnotation(clazz);
          final boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
          if ((entityAnn != null || embeddedAnn != null) && !isAbstract) {
            map(clazz);
          }
        } catch (final MappingException ex) {
          if (!ignoreInvalidClasses) {
            throw ex;
          }
        }
      }
      return this;
    } catch (Exception e) {
      throw new MappingException("Could not get map classes from package " + packageName, e);
    }
  }

  /**
   * Maps all the classes found in the package to which the given class belongs.
   *
   * @param clazz the class to use when trying to find others to map
   * @return this
   */
  public MixMongo mapPackageFromClass(final Class clazz) {
    return mapPackage(clazz.getPackage().getName(), false);
  }

  /**
   * Converts an entity to a DBObject. This method is primarily an internal method. Reliance on this
   * method may break your application in future releases.
   *
   * @param entity the entity to convert
   * @return the DBObject
   */
  public DBObject toDBObject(final Object entity) {
    try {
      return mapper.toDBObject(entity);
    } catch (Exception e) {
      throw new MappingException("Could not map entity to DBObject", e);
    }
  }
}
