## How to use 

### Maven Dependency

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-dependencies</artifactId>
            <version>1.0.3</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>

```

- If you use `jedis` client , import dependency

```xml

<dependency>
    <groupId>com.yunlsp.framework.components</groupId>
    <artifactId>mixmicro-components-starter-cache-redis-default</artifactId>
</dependency>

```

- If you use `lettuce` client , import dependency

```xml

<dependency>
    <groupId>com.yunlsp.framework.components</groupId>
    <artifactId>mixmicro-components-starter-cache-redis-lettuce</artifactId>
</dependency>

```

- If you use `spring data` client , import dependency

```xml

<dependency>
    <groupId>com.yunlsp.framework.components</groupId>
    <artifactId>mixmicro-components-starter-cache-redis-springdata</artifactId>
</dependency>

```

### Configuration & Properties


- `Jedis` client

```properties

## 控制台状态答应时间间隔，单位：分钟
mixcache.statIntervalMinutes=5
mixcache.areaInCacheName=false
## 本地缓存配置
mixcache.local.default.type=linkedhashmap
mixcache.local.default.keyConvertor=fastjson
mixcache.remote.default.type=redis
mixcache.remote.default.keyConvertor=fastjson
mixcache.remote.default.valueEncoder=java
mixcache.remote.default.valueDecoder=java
mixcache.remote.default.poolConfig.minIdle=5
mixcache.remote.default.poolConfig.maxIdle=20
mixcache.remote.default.poolConfig.maxTotal=50

## 用户配置 Redis 资源的地址、密码、端口
mixcache.remote.default.host=${redis.host}
mixcache.remote.default.port=${redis.port}
mixcache.remote.default.password=${redis.password}

```

- `lettuce` client

```properties

## 控制台状态答应时间间隔，单位：分钟
mixcache.statIntervalMinutes=5
mixcache.areaInCacheName=false
## 本地缓存配置
mixcache.local.default.type=linkedhashmap
mixcache.local.default.keyConvertor=fastjson
mixcache.remote.default.type=redis.lettuce
mixcache.remote.default.keyConvertor=fastjson
mixcache.remote.default.valueEncoder=java
mixcache.remote.default.valueDecoder=java

## 用户配置 Redis 资源的地址、密码、端口
mixcache.remote.default.uri=redis://${redis.password}@${redis.host}:${redis.port}

```

### Java Code

App class:
```java
@SpringBootApplication
@EnableMethodCache(basePackages = "com.company.mypackage")
@EnableCreateCacheAnnotation
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
```

#### 方法缓存
Declare method cache using ```@Cached``` annotation.  
```expire = 3600``` indicates that the elements will expires in 3600 seconds after put.
MixCache auto generates cache key using all parameters.

```java
public interface DemoService {
    @Cached(expire = 3600, cacheType = CacheType.REMOTE)
    User getUserById(long userId);
}
```

Using ```key``` attribute to specify cache key using [SpEL](https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/expressions.html) script.
```java
public interface DemoService {
    @Cached(name="demoCache-", key="#userId", expire = 3600)
    User getUserById(long userId);

    @CacheUpdate(name="demoCache-", key="#user.userId", value="#user")
    void updateUser(User user);

    @CacheInvalidate(name="demoCache-", key="#userId")
    void deleteUser(long userId);
}
```
To enable use parameter name such as ```key="#userId"```, you javac compiler target must be 1.8 and the ```-parameters``` should be set, otherwise use index to access parameters like ```key="args[0]"```

自动刷新支持:
```java
public interface SummaryService{
    @Cached(expire = 3600, cacheType = CacheType.REMOTE)
    @CacheRefresh(refresh = 1800, stopRefreshAfterLastAccess = 3600, timeUnit = TimeUnit.SECONDS)
    @CachePenetrationProtect
    BigDecimal summaryOfToday(long catagoryId);
}
```
CachePenetrationProtect annotation indicates to synchronize load operation in multi-thread environment.

#### 缓存操作API

Create a ```Cache``` instance using ```@CreateCache``` annotation:

```java
@CreateCache(expire = 100, cacheType = CacheType.BOTH, localLimit = 50)
private Cache<Long, UserDO> demoCache;
```
The code above create a ```Cache``` instance. ```cacheType = CacheType.BOTH``` define a two level cache (a local in-memory-cache and a remote cache system) with local elements limited upper to 50(LRU based evict). You can use it like a map: 
```java
UserDO user = demoCache.get(12345L);
demoCache.put(12345L, loadUserFromDataBase(12345L));
demoCache.remove(12345L);

demoCache.computeIfAbsent(1234567L, (key) -> loadUserFromDataBase(1234567L));
```

Or you can create ```Cache``` instance manually (RedisCache in this example) :
```java
GenericObjectPoolConfig pc = new GenericObjectPoolConfig();
pc.setMinIdle(2);
pc.setMaxIdle(10);
pc.setMaxTotal(10);
JedisPool pool = new JedisPool(pc, "localhost", 6379);
Cache<Long, UserDO> demoCache = RedisCacheBuilder.createRedisCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(JavaValueEncoder.INSTANCE)
                .valueDecoder(JavaValueDecoder.INSTANCE)
                .jedisPool(pool)
                .keyPrefix("demoCache-")
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .buildCache();
```

#### Advanced API
Asynchronous API:
```java
CacheGetResult r = cache.GET(userId);
CompletionStage<ResultData> future = r.future();
future.thenRun(() -> {
    if(r.isSuccess()){
        System.out.println(r.getValue());
    }
});
```

分布式锁支持:
```java
cache.tryLockAndRun("key", 60, TimeUnit.SECONDS, () -> heavyDatabaseOperation());
```

