package xyz.vopen.mixmicro.components.enhance.cache.redis;

import xyz.vopen.mixmicro.components.enhance.cache.external.ExternalCacheConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class RedisCacheConfig<K, V> extends ExternalCacheConfig<K, V> {

    private Pool<Jedis> jedisPool;
    private Pool<Jedis>[] jedisSlavePools;
    private boolean readFromSlave;
    private int[] slaveReadWeights;

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Pool<Jedis>[] getJedisSlavePools() {
        return jedisSlavePools;
    }

    public void setJedisSlavePools(Pool<Jedis>... jedisSlavePools) {
        this.jedisSlavePools = jedisSlavePools;
    }

    public boolean isReadFromSlave() {
        return readFromSlave;
    }

    public void setReadFromSlave(boolean readFromSlave) {
        this.readFromSlave = readFromSlave;
    }

    public int[] getSlaveReadWeights() {
        return slaveReadWeights;
    }

    public void setSlaveReadWeights(int... slaveReadWeights) {
        this.slaveReadWeights = slaveReadWeights;
    }
}
