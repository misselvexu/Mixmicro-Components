package xyz.vopen.mixmicro.components.enhance.cache.annotation.field;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import xyz.vopen.mixmicro.components.enhance.cache.*;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.CacheConsts;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.CachePenetrationProtect;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.CacheRefresh;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.CreateCache;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.method.CacheConfigUtil;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.support.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created on 2018/12/13.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
class LazyInitCache implements ProxyCache {

  private boolean inited;
  private Cache cache;

  private ConfigurableListableBeanFactory beanFactory;
  private CreateCache ann;
  private Field field;
  private RefreshPolicy refreshPolicy;
  private PenetrationProtectConfig protectConfig;

  public LazyInitCache(ConfigurableListableBeanFactory beanFactory, CreateCache ann, Field field) {
    this.beanFactory = beanFactory;
    this.ann = ann;
    this.field = field;
    CacheRefresh cr = field.getAnnotation(CacheRefresh.class);
    if (cr != null) {
      refreshPolicy = CacheConfigUtil.parseRefreshPolicy(cr);
    }
    CachePenetrationProtect penetrateProtect = field.getAnnotation(CachePenetrationProtect.class);
    if (penetrateProtect != null) {
      protectConfig = CacheConfigUtil.parsePenetrationProtectConfig(penetrateProtect);
    }
  }

  private void checkInit() {
    if (!inited) {
      synchronized (this) {
        if (!inited) {
          init();
          inited = true;
        }
      }
    }
  }

  @Override
  public Cache getTargetCache() {
    checkInit();
    return cache;
  }

  private void init() {
    if (inited) {
      throw new IllegalStateException();
    }
    GlobalCacheConfig globalCacheConfig = beanFactory.getBean(GlobalCacheConfig.class);
    ConfigProvider configProvider = beanFactory.getBean(ConfigProvider.class);

    CachedAnnoConfig cac = new CachedAnnoConfig();
    cac.setArea(ann.area());
    cac.setName(ann.name());
    cac.setTimeUnit(ann.timeUnit());
    cac.setExpire(ann.expire());
    cac.setLocalExpire(ann.localExpire());
    cac.setCacheType(ann.cacheType());
    cac.setLocalLimit(ann.localLimit());
    cac.setSerialPolicy(ann.serialPolicy());
    cac.setKeyConvertor(ann.keyConvertor());

    cac.setRefreshPolicy(refreshPolicy);
    cac.setPenetrationProtectConfig(protectConfig);

    String cacheName = cac.getName();
    if (CacheConsts.isUndefined(cacheName)) {
      String[] hiddenPackages = globalCacheConfig.getHiddenPackages();
      CacheNameGenerator g = configProvider.createCacheNameGenerator(hiddenPackages);
      cacheName = g.generateCacheName(field);
    }
    cache = configProvider.getCacheContext().__createOrGetCache(cac, ann.area(), cacheName);
  }

  @Override
  public CacheConfig config() {
    checkInit();
    return cache.config();
  }

  @Override
  public Object get(Object key) {
    checkInit();
    return cache.get(key);
  }

  @Override
  public Map getAll(Set keys) {
    checkInit();
    return cache.getAll(keys);
  }

  @Override
  public CacheGetResult GET(Object key) {
    checkInit();
    return cache.GET(key);
  }

  @Override
  public MultiGetResult GET_ALL(Set keys) {
    checkInit();
    return cache.GET_ALL(keys);
  }

  @Override
  public Object computeIfAbsent(Object key, Function loader) {
    checkInit();
    return cache.computeIfAbsent(key, loader);
  }

  @Override
  public Object computeIfAbsent(
      Object key, Function loader, boolean cacheNullWhenLoaderReturnNull) {
    checkInit();
    return cache.computeIfAbsent(key, loader, cacheNullWhenLoaderReturnNull);
  }

  @Override
  public Object computeIfAbsent(
      Object key,
      Function loader,
      boolean cacheNullWhenLoaderReturnNull,
      long expireAfterWrite,
      TimeUnit timeUnit) {
    checkInit();
    return cache.computeIfAbsent(
        key, loader, cacheNullWhenLoaderReturnNull, expireAfterWrite, timeUnit);
  }

  @Override
  public void put(Object key, Object value) {
    checkInit();
    cache.put(key, value);
  }

  @Override
  public void putAll(Map map) {
    checkInit();
    cache.putAll(map);
  }

  @Override
  public void put(Object key, Object value, long expireAfterWrite, TimeUnit timeUnit) {
    checkInit();
    cache.put(key, value, expireAfterWrite, timeUnit);
  }

  @Override
  public CacheResult PUT(Object key, Object value) {
    checkInit();
    return cache.PUT(key, value);
  }

  @Override
  public CacheResult PUT(Object key, Object value, long expireAfterWrite, TimeUnit timeUnit) {
    checkInit();
    return cache.PUT(key, value, expireAfterWrite, timeUnit);
  }

  @Override
  public void putAll(Map map, long expireAfterWrite, TimeUnit timeUnit) {
    checkInit();
    cache.putAll(map, expireAfterWrite, timeUnit);
  }

  @Override
  public CacheResult PUT_ALL(Map map) {
    checkInit();
    return cache.PUT_ALL(map);
  }

  @Override
  public CacheResult PUT_ALL(Map map, long expireAfterWrite, TimeUnit timeUnit) {
    checkInit();
    return cache.PUT_ALL(map, expireAfterWrite, timeUnit);
  }

  @Override
  public boolean remove(Object key) {
    checkInit();
    return cache.remove(key);
  }

  @Override
  public void removeAll(Set keys) {
    checkInit();
    cache.removeAll(keys);
  }

  @Override
  public Object unwrap(Class clazz) {
    checkInit();
    return cache.unwrap(clazz);
  }

  @Override
  public CacheResult REMOVE(Object key) {
    checkInit();
    return cache.REMOVE(key);
  }

  @Override
  public CacheResult REMOVE_ALL(Set keys) {
    checkInit();
    return cache.REMOVE_ALL(keys);
  }

  @Override
  public AutoReleaseLock tryLock(Object key, long expire, TimeUnit timeUnit) {
    checkInit();
    return cache.tryLock(key, expire, timeUnit);
  }

  @Override
  public boolean tryLockAndRun(Object key, long expire, TimeUnit timeUnit, Runnable action) {
    checkInit();
    return cache.tryLockAndRun(key, expire, timeUnit, action);
  }

  @Override
  public boolean putIfAbsent(Object key, Object value) {
    checkInit();
    return cache.putIfAbsent(key, value);
  }

  @Override
  public CacheResult PUT_IF_ABSENT(
      Object key, Object value, long expireAfterWrite, TimeUnit timeUnit) {
    checkInit();
    return cache.PUT_IF_ABSENT(key, value, expireAfterWrite, timeUnit);
  }
}
