package xyz.vopen.mixmicro.components.enhance.schedule.quartz;

import com.mongodb.client.MongoClient;
import xyz.vopen.mixmicro.components.enhance.schedule.quartz.clojure.DynamicClassLoadHelper;
import org.quartz.spi.ClassLoadHelper;

public class DynamicMongoDBJobStore extends MongoDBJobStore {

  public DynamicMongoDBJobStore() {
    super();
  }

  public DynamicMongoDBJobStore(MongoClient mongo) {
    super(mongo);
  }

  public DynamicMongoDBJobStore(String mongoUri, String username, String password) {
    super(mongoUri, username, password);
  }

  @Override
  protected ClassLoadHelper getClassLoaderHelper(ClassLoadHelper original) {
    return new DynamicClassLoadHelper();
  }
}
