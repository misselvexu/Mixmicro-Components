package xyz.vopen.mixmicro.components.enhance.mongo.client.test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import org.bson.types.ObjectId;
import xyz.vopen.mixmicro.components.enhance.mongo.client.test.model.Employee;
import xyz.vopen.mixmicro.components.mongo.client.Datastore;
import xyz.vopen.mixmicro.components.mongo.client.Key;
import xyz.vopen.mixmicro.components.mongo.client.MixmicroMongo;

/**
 * {@link SimpleMongoClientTest}
 *
 * <p>Class SimpleMongoClientTest Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/9/3
 */
public class SimpleMongoClientTest {

  public static void main(String[] args) {

    MixmicroMongo mongo = new MixmicroMongo();
    mongo.mapPackage("xyz.vopen.mixmicro.components.enhance.mongo.client.test.model");

    MongoClientOptions.Builder builder =
        new MongoClientOptions.Builder()
            // 设置连接超时时间为10s
            .connectTimeout(1000 * 10)
            // 设置最长等待时间为10s
            .maxWaitTime(1000 * 10)
            .addCommandListener(
                new CommandListener() {
                  @Override
                  public void commandStarted(CommandStartedEvent event) {
                    System.out.println(event);
                  }

                  @Override
                  public void commandSucceeded(CommandSucceededEvent event) {
                    System.out.println(event);
                  }

                  @Override
                  public void commandFailed(CommandFailedEvent event) {
                    System.out.println(event);
                  }
                });

    MongoClientURI uri = new MongoClientURI("mongodb://pipeline:pipeline#08039863@dev-middle.hgj.net:27017/pipeline", builder);

    MongoClient client = new MongoClient(uri);

    Datastore datastore = mongo.createDatastore(client, "pipeline");

    datastore.ensureIndexes();

    Key key = datastore.save(Employee.builder().id(ObjectId.get()).name("misselvexu").salary(10000d).build());

    System.out.println(key);
  }
}
