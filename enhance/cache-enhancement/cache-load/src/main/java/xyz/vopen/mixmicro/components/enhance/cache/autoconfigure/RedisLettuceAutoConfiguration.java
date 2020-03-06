package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.masterslave.MasterSlave;
import io.lettuce.core.masterslave.StatefulRedisMasterSlaveConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.enhance.cache.CacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.CacheConfigException;
import xyz.vopen.mixmicro.components.enhance.cache.anno.CacheConsts;
import xyz.vopen.mixmicro.components.enhance.cache.external.ExternalCacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.redis.lettuce.LettuceConnectionManager;
import xyz.vopen.mixmicro.components.enhance.cache.redis.lettuce.MixCacheCodec;
import xyz.vopen.mixmicro.components.enhance.cache.redis.lettuce.RedisLettuceCacheBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@Configuration
@Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
public class RedisLettuceAutoConfiguration {
  public static final String AUTO_INIT_BEAN_NAME = "redisLettuceAutoInit";

  @Bean(name = {AUTO_INIT_BEAN_NAME})
  public RedisLettuceAutoInit redisLettuceAutoInit() {
    return new RedisLettuceAutoInit();
  }

  public static class RedisLettuceCondition extends MixCacheCondition {
    public RedisLettuceCondition() {
      super("redis.lettuce");
    }
  }

  public static class RedisLettuceAutoInit extends ExternalCacheAutoInit {

    public RedisLettuceAutoInit() {
      super("redis.lettuce");
    }

    @Override
    protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
      Map<String, Object> map = ct.subTree("uri" /*there is no dot*/).getProperties();
      String readFromStr = ct.getProperty("readFrom");
      String mode = ct.getProperty("mode");
      long asyncResultTimeoutInMillis =
          Long.parseLong(
              ct.getProperty(
                  "asyncResultTimeoutInMillis",
                  Long.toString(CacheConsts.ASYNC_RESULT_TIMEOUT.toMillis())));
      ReadFrom readFrom = null;
      if (readFromStr != null) {
        readFrom = ReadFrom.valueOf(readFromStr.trim());
      }

      AbstractRedisClient client;
      StatefulConnection connection = null;
      if (map == null || map.size() == 0) {
        throw new CacheConfigException("lettuce uri is required");
      } else {
        List<RedisURI> uriList =
            map.values().stream()
                .map((k) -> RedisURI.create(URI.create(k.toString())))
                .collect(Collectors.toList());
        if (uriList.size() == 1) {
          RedisURI uri = uriList.get(0);
          if (readFrom == null) {
            client = RedisClient.create(uri);
            ((RedisClient) client)
                .setOptions(
                    ClientOptions.builder()
                        .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                        .build());
          } else {
            client = RedisClient.create();
            ((RedisClient) client)
                .setOptions(
                    ClientOptions.builder()
                        .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                        .build());
            StatefulRedisMasterSlaveConnection c =
                MasterSlave.connect((RedisClient) client, new MixCacheCodec(), uri);
            c.setReadFrom(readFrom);
            connection = c;
          }
        } else {
          if (mode != null && mode.equalsIgnoreCase("MasterSlave")) {
            client = RedisClient.create();
            ((RedisClient) client)
                .setOptions(
                    ClientOptions.builder()
                        .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                        .build());
            StatefulRedisMasterSlaveConnection c =
                MasterSlave.connect((RedisClient) client, new MixCacheCodec(), uriList);
            if (readFrom != null) {
              c.setReadFrom(readFrom);
            }
            connection = c;
          } else {
            client = RedisClusterClient.create(uriList);
            ((RedisClusterClient) client)
                .setOptions(
                    ClusterClientOptions.builder()
                        .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                        .build());
            if (readFrom != null) {
              StatefulRedisClusterConnection c =
                  ((RedisClusterClient) client).connect(new MixCacheCodec());
              c.setReadFrom(readFrom);
              connection = c;
            }
          }
        }
      }

      ExternalCacheBuilder externalCacheBuilder =
          RedisLettuceCacheBuilder.createRedisLettuceCacheBuilder()
              .connection(connection)
              .redisClient(client)
              .asyncResultTimeoutInMillis(asyncResultTimeoutInMillis);
      parseGeneralConfig(externalCacheBuilder, ct);

      // eg: "remote.default.client"
      autoConfigureBeans.getCustomContainer().put(cacheAreaWithPrefix + ".client", client);
      LettuceConnectionManager m = LettuceConnectionManager.defaultManager();
      m.init(client, connection);
      autoConfigureBeans
          .getCustomContainer()
          .put(cacheAreaWithPrefix + ".connection", m.connection(client));
      autoConfigureBeans
          .getCustomContainer()
          .put(cacheAreaWithPrefix + ".commands", m.commands(client));
      autoConfigureBeans
          .getCustomContainer()
          .put(cacheAreaWithPrefix + ".asyncCommands", m.asyncCommands(client));
      autoConfigureBeans
          .getCustomContainer()
          .put(cacheAreaWithPrefix + ".reactiveCommands", m.reactiveCommands(client));
      return externalCacheBuilder;
    }
  }
}
