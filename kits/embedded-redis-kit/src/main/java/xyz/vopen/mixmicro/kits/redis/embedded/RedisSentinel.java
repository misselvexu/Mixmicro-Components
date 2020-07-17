package xyz.vopen.mixmicro.kits.redis.embedded;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RedisSentinel}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RedisSentinel extends AbstractRedisInstance {
  private static final String REDIS_READY_PATTERN = ".*Sentinel (runid|ID) is.*";

  public RedisSentinel(List<String> args, int port) {
    super(port);
    this.args = new ArrayList<String>(args);
  }

  public static RedisSentinelBuilder builder() {
    return new RedisSentinelBuilder();
  }

  @Override
  protected String redisReadyPattern() {
    return REDIS_READY_PATTERN;
  }
}
