package xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ConnectionPool;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;
import xyz.vopen.mixmicro.components.boot.httpclient.core.NoValidServiceInstanceChooser;
import xyz.vopen.mixmicro.components.boot.httpclient.core.PrototypeInterceptorBdfProcessor;
import xyz.vopen.mixmicro.components.boot.httpclient.core.ServiceInstanceChooser;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.BaseGlobalInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.NetworkInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.ServiceInstanceChooserInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.retry.BaseRetryInterceptor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(RetrofitProperties.class)
@AutoConfigureAfter({JacksonAutoConfiguration.class})
public class RetrofitAutoConfiguration implements ApplicationContextAware {

  @Autowired private RetrofitProperties retrofitProperties;

  private ApplicationContext applicationContext;

  @Configuration
  public static class RetrofitProcessorAutoConfiguration {

    @Bean
    public static PrototypeInterceptorBdfProcessor prototypeInterceptorBdfProcessor() {
      return new PrototypeInterceptorBdfProcessor();
    }
  }

  @Bean
  @ConditionalOnMissingBean
  public RetrofitConfigBean retrofitConfigBean()
      throws IllegalAccessException, InstantiationException {
    RetrofitConfigBean retrofitConfigBean = new RetrofitConfigBean(retrofitProperties);
    // Initialize the connection pool
    Map<String, ConnectionPool> poolRegistry = new ConcurrentHashMap<>(4);
    Map<String, PoolConfig> pool = retrofitProperties.getPool();
    if (pool != null) {
      pool.forEach(
          (poolName, poolConfig) -> {
            long keepAliveSecond = poolConfig.getKeepAliveSecond();
            int maxIdleConnections = poolConfig.getMaxIdleConnections();
            ConnectionPool connectionPool =
                new ConnectionPool(maxIdleConnections, keepAliveSecond, TimeUnit.SECONDS);
            poolRegistry.put(poolName, connectionPool);
          });
    }
    retrofitConfigBean.setPoolRegistry(poolRegistry);

    // callAdapterFactory
    Class<? extends CallAdapter.Factory>[] globalCallAdapterFactories =
        retrofitProperties.getGlobalCallAdapterFactories();
    retrofitConfigBean.setGlobalCallAdapterFactoryClasses(globalCallAdapterFactories);

    // converterFactory
    Class<? extends Converter.Factory>[] globalConverterFactories =
        retrofitProperties.getGlobalConverterFactories();
    retrofitConfigBean.setGlobalConverterFactoryClasses(globalConverterFactories);

    // globalInterceptors
    Collection<BaseGlobalInterceptor> globalInterceptors = getBeans(BaseGlobalInterceptor.class);
    retrofitConfigBean.setGlobalInterceptors(globalInterceptors);

    // retryInterceptor
    Class<? extends BaseRetryInterceptor> retryInterceptor =
        retrofitProperties.getRetryInterceptor();
    retrofitConfigBean.setRetryInterceptor(retryInterceptor.newInstance());

    // add networkInterceptor
    Collection<NetworkInterceptor> networkInterceptors = getBeans(NetworkInterceptor.class);
    retrofitConfigBean.setNetworkInterceptors(networkInterceptors);

    // add ServiceInstanceChooserInterceptor
    ServiceInstanceChooser serviceInstanceChooser;
    try {
      serviceInstanceChooser = applicationContext.getBean(ServiceInstanceChooser.class);
    } catch (BeansException e) {
      serviceInstanceChooser = new NoValidServiceInstanceChooser();
    }

    ServiceInstanceChooserInterceptor serviceInstanceChooserInterceptor =
        new ServiceInstanceChooserInterceptor(serviceInstanceChooser);
    retrofitConfigBean.setServiceInstanceChooserInterceptor(serviceInstanceChooserInterceptor);

    return retrofitConfigBean;
  }

  @Bean
  @ConditionalOnMissingBean
  public ObjectMapper jacksonObjectMapper() {
    return new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Bean
  @ConditionalOnMissingBean
  @Autowired
  public JacksonConverterFactory jacksonConverterFactory(ObjectMapper objectMapper) {
    return JacksonConverterFactory.create(objectMapper);
  }

  private <U> Collection<U> getBeans(Class<U> clz) {
    try {
      Map<String, U> beanMap = applicationContext.getBeansOfType(clz);
      return beanMap.values();
    } catch (BeansException e) {
      // do nothing
    }
    return null;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
