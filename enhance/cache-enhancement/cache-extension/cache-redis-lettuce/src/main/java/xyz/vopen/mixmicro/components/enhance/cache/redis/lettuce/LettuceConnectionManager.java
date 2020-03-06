package xyz.vopen.mixmicro.components.enhance.cache.redis.lettuce;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import xyz.vopen.mixmicro.components.enhance.cache.CacheConfigException;
import xyz.vopen.mixmicro.components.enhance.cache.CacheException;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class LettuceConnectionManager {

  private static final LettuceConnectionManager defaultManager = new LettuceConnectionManager();
  private Map<AbstractRedisClient, LettuceObjects> map =
      Collections.synchronizedMap(new WeakHashMap());

  private LettuceConnectionManager() {}

  public static LettuceConnectionManager defaultManager() {
    return defaultManager;
  }

  private LettuceObjects getLettuceObjectsFromMap(AbstractRedisClient redisClient) {
    LettuceObjects lo = map.get(redisClient);
    if (lo == null) {
      throw new CacheException("LettuceObjects is not initialized");
    }
    return lo;
  }

  public void init(AbstractRedisClient redisClient, StatefulConnection connection) {
    map.computeIfAbsent(
        redisClient,
        key -> {
          LettuceObjects lo = new LettuceObjects();
          lo.connection = connection;
          return lo;
        });
  }

  public StatefulConnection connection(AbstractRedisClient redisClient) {
    LettuceObjects lo = getLettuceObjectsFromMap(redisClient);
    if (lo.connection == null) {
      if (redisClient instanceof RedisClient) {
        lo.connection = ((RedisClient) redisClient).connect(new MixCacheCodec());
      } else if (redisClient instanceof RedisClusterClient) {
        lo.connection = ((RedisClusterClient) redisClient).connect(new MixCacheCodec());
      } else {
        throw new CacheConfigException("type " + redisClient.getClass() + " is not supported");
      }
    }
    return lo.connection;
  }

  public Object commands(AbstractRedisClient redisClient) {
    connection(redisClient);
    LettuceObjects lo = getLettuceObjectsFromMap(redisClient);
    if (lo.commands == null) {
      if (lo.connection instanceof StatefulRedisConnection) {
        lo.commands = ((StatefulRedisConnection) lo.connection).sync();
      } else if (lo.connection instanceof StatefulRedisClusterConnection) {
        lo.commands = ((StatefulRedisClusterConnection) lo.connection).sync();
      } else if (lo.connection instanceof StatefulRedisSentinelConnection) {
        lo.commands = ((StatefulRedisSentinelConnection) lo.connection).sync();
      } else {
        throw new CacheConfigException("type " + lo.connection.getClass() + " is not supported");
      }
    }
    return lo.commands;
  }

  public Object asyncCommands(AbstractRedisClient redisClient) {
    connection(redisClient);
    LettuceObjects lo = getLettuceObjectsFromMap(redisClient);
    if (lo.asyncCommands == null) {
      if (lo.connection instanceof StatefulRedisConnection) {
        lo.asyncCommands = ((StatefulRedisConnection) lo.connection).async();
      } else if (lo.connection instanceof StatefulRedisClusterConnection) {
        lo.asyncCommands = ((StatefulRedisClusterConnection) lo.connection).async();
      } else if (lo.connection instanceof StatefulRedisSentinelConnection) {
        lo.asyncCommands = ((StatefulRedisSentinelConnection) lo.connection).async();
      } else {
        throw new CacheConfigException("type " + lo.connection.getClass() + " is not supported");
      }
    }
    return lo.asyncCommands;
  }

  public Object reactiveCommands(AbstractRedisClient redisClient) {
    connection(redisClient);
    LettuceObjects lo = getLettuceObjectsFromMap(redisClient);
    if (lo.reactiveCommands == null) {
      if (lo.connection instanceof StatefulRedisConnection) {
        lo.reactiveCommands = ((StatefulRedisConnection) lo.connection).reactive();
      } else if (lo.connection instanceof StatefulRedisClusterConnection) {
        lo.reactiveCommands = ((StatefulRedisClusterConnection) lo.connection).reactive();
      } else if (lo.connection instanceof StatefulRedisSentinelConnection) {
        lo.reactiveCommands = ((StatefulRedisSentinelConnection) lo.connection).reactive();
      } else {
        throw new CacheConfigException("type " + lo.connection.getClass() + " is not supported");
      }
    }
    return lo.reactiveCommands;
  }

  public void removeAndClose(AbstractRedisClient redisClient) {
    LettuceObjects lo = (LettuceObjects) map.remove(redisClient);
    if (lo == null) {
      return;
    }
    /*
    if (lo.commands != null && lo.commands instanceof RedisClusterCommands) {
        ((RedisClusterCommands) lo.commands).close();
    }
    if (lo.asyncCommands != null && lo.asyncCommands instanceof RedisClusterAsyncCommands) {
        ((RedisClusterAsyncCommands) lo.asyncCommands).close();
    }
    if (lo.reactiveCommands != null && lo.reactiveCommands instanceof RedisClusterReactiveCommands) {
        ((RedisClusterReactiveCommands) lo.reactiveCommands).close();
    }
    */
    if (lo.connection != null) {
      lo.connection.close();
    }
    redisClient.shutdown();
  }

  private static class LettuceObjects {
    private StatefulConnection connection;
    private Object commands;
    private Object asyncCommands;
    private Object reactiveCommands;
  }
}
