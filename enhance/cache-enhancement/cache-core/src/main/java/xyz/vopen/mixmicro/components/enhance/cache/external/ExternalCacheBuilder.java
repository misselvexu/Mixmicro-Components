package xyz.vopen.mixmicro.components.enhance.cache.external;

import xyz.vopen.mixmicro.components.enhance.cache.AbstractCacheBuilder;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public abstract class ExternalCacheBuilder<T extends ExternalCacheBuilder<T>>
    extends AbstractCacheBuilder<T> {

  @Override
  public ExternalCacheConfig getConfig() {
    if (config == null) {
      config = new ExternalCacheConfig();
    }
    return (ExternalCacheConfig) config;
  }

  public T keyPrefix(String keyPrefix) {
    getConfig().setKeyPrefix(keyPrefix);
    return self();
  }

  public T valueEncoder(Function<Object, byte[]> valueEncoder) {
    getConfig().setValueEncoder(valueEncoder);
    return self();
  }

  public T valueDecoder(Function<byte[], Object> valueDecoder) {
    getConfig().setValueDecoder(valueDecoder);
    return self();
  }

  public void setKeyPrefix(String keyPrefix) {
    getConfig().setKeyPrefix(keyPrefix);
  }

  public void setValueEncoder(Function<Object, byte[]> valueEncoder) {
    getConfig().setValueEncoder(valueEncoder);
  }

  public void setValueDecoder(Function<byte[], Object> valueDecoder) {
    getConfig().setValueDecoder(valueDecoder);
  }
}
