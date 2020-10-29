package xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;
import xyz.vopen.mixmicro.components.boot.httpclient.core.BodyCallAdapterFactory;
import xyz.vopen.mixmicro.components.boot.httpclient.core.ResponseCallAdapterFactory;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.BaseLoggingInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.DefaultLoggingInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.retry.BaseRetryInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.retry.DefaultRetryInterceptor;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "mixmicro.httpclient")
public class RetrofitProperties {

  private static final String DEFAULT_POOL = "default";

  /** Connection pool configuration */
  private Map<String, PoolConfig> pool = new LinkedHashMap<>();

  /** Enable log printing */
  private boolean enableLog = true;

  /** Log print Interceptor */
  private Class<? extends BaseLoggingInterceptor> loggingInterceptor =
      DefaultLoggingInterceptor.class;

  /** retry interceptor */
  private Class<? extends BaseRetryInterceptor> retryInterceptor = DefaultRetryInterceptor.class;

  /** Disable Void return type */
  private boolean disableVoidReturnType = false;

  /**
   * global converter factories, The converter instance is first obtained from the Spring container.
   * If it is not obtained, it is created by reflection.
   */
  @SuppressWarnings("unchecked")
  private Class<? extends Converter.Factory>[] globalConverterFactories =
      (Class<? extends Converter.Factory>[]) new Class[] {JacksonConverterFactory.class};

  /**
   * global call adapter factories, The callAdapter instance is first obtained from the Spring
   * container. If it is not obtained, it is created by reflection.
   */
  @SuppressWarnings("unchecked")
  private Class<? extends CallAdapter.Factory>[] globalCallAdapterFactories =
      (Class<? extends CallAdapter.Factory>[])
          new Class[] {BodyCallAdapterFactory.class, ResponseCallAdapterFactory.class};

  public Class<? extends BaseLoggingInterceptor> getLoggingInterceptor() {
    return loggingInterceptor;
  }

  public void setLoggingInterceptor(Class<? extends BaseLoggingInterceptor> loggingInterceptor) {
    this.loggingInterceptor = loggingInterceptor;
  }

  public Map<String, PoolConfig> getPool() {
    if (!pool.isEmpty()) {
      return pool;
    }
    pool.put(DEFAULT_POOL, new PoolConfig(5, 300));
    return pool;
  }

  public void setPool(Map<String, PoolConfig> pool) {
    this.pool = pool;
  }

  public boolean isEnableLog() {
    return enableLog;
  }

  public void setEnableLog(boolean enableLog) {
    this.enableLog = enableLog;
  }

  public boolean isDisableVoidReturnType() {
    return disableVoidReturnType;
  }

  public void setDisableVoidReturnType(boolean disableVoidReturnType) {
    this.disableVoidReturnType = disableVoidReturnType;
  }

  public Class<? extends BaseRetryInterceptor> getRetryInterceptor() {
    return retryInterceptor;
  }

  public void setRetryInterceptor(Class<? extends BaseRetryInterceptor> retryInterceptor) {
    this.retryInterceptor = retryInterceptor;
  }

  public Class<? extends Converter.Factory>[] getGlobalConverterFactories() {
    return globalConverterFactories;
  }

  public void setGlobalConverterFactories(
      Class<? extends Converter.Factory>[] globalConverterFactories) {
    this.globalConverterFactories = globalConverterFactories;
  }

  public Class<? extends CallAdapter.Factory>[] getGlobalCallAdapterFactories() {
    return globalCallAdapterFactories;
  }

  public void setGlobalCallAdapterFactories(
      Class<? extends CallAdapter.Factory>[] globalCallAdapterFactories) {
    this.globalCallAdapterFactories = globalCallAdapterFactories;
  }
}
