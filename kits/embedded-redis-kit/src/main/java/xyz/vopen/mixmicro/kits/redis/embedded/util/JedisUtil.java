package xyz.vopen.mixmicro.kits.redis.embedded.util;

import xyz.vopen.mixmicro.kits.redis.embedded.Redis;
import xyz.vopen.mixmicro.kits.redis.embedded.RedisCluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link JedisUtil}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class JedisUtil {
  public static Set<String> jedisHosts(Redis redis) {
    final List<Integer> ports = redis.ports();
    return portsToJedisHosts(ports);
  }

  public static Set<String> sentinelHosts(RedisCluster cluster) {
    final List<Integer> ports = cluster.sentinelPorts();
    return portsToJedisHosts(ports);
  }

  public static Set<String> portsToJedisHosts(List<Integer> ports) {
    Set<String> hosts = new HashSet<String>();
    for (Integer p : ports) {
      hosts.add("localhost:" + p);
    }
    return hosts;
  }
}
