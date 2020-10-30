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
import org.springframework.lang.NonNull;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;
import xyz.vopen.mixmicro.components.boot.httpclient.ServiceInstanceChooser;
import xyz.vopen.mixmicro.components.boot.httpclient.core.cloud.InvalidServiceInstanceChooser;
import xyz.vopen.mixmicro.components.boot.httpclient.core.PrototypeInterceptorDefinitionPostProcessor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.AbstractGlobalInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.ServiceInstanceChooserInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.retry.AbstractRetryInterceptor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(MixHttpClientProperties.class)
@AutoConfigureAfter({JacksonAutoConfiguration.class})
public class MixHttpClientAutoConfiguration implements ApplicationContextAware {

  private final MixHttpClientProperties httpClientProperties;

  private ApplicationContext applicationContext;

  public MixHttpClientAutoConfiguration(MixHttpClientProperties httpClientProperties) {
    this.httpClientProperties = httpClientProperties;
  }

  @Configuration
  public static class RetrofitProcessorAutoConfiguration {

    @Bean
    public static PrototypeInterceptorDefinitionPostProcessor prototypeInterceptorBdfProcessor() {
      return new PrototypeInterceptorDefinitionPostProcessor();
    }
  }

  @Bean
  @ConditionalOnMissingBean
  public MixHttpClientConfigBean retrofitConfigBean()
      throws IllegalAccessException, InstantiationException {
    MixHttpClientConfigBean httpClientConfigBean = new MixHttpClientConfigBean(httpClientProperties);
    // Initialize the connection pool
    Map<String, ConnectionPool> poolRegistry = new ConcurrentHashMap<>(4);
    Map<String, MixHttpClientProperties.PoolConfig> pool = httpClientProperties.getPool();
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
    httpClientConfigBean.setPoolRegistry(poolRegistry);

    // callAdapterFactory
    Class<? extends CallAdapter.Factory>[] globalCallAdapterFactories =
        httpClientProperties.getGlobalCallAdapterFactories();
    httpClientConfigBean.setGlobalCallAdapterFactoryClasses(globalCallAdapterFactories);

    // converterFactory
    Class<? extends Converter.Factory>[] globalConverterFactories =
        httpClientProperties.getGlobalConverterFactories();
    httpClientConfigBean.setGlobalConverterFactoryClasses(globalConverterFactories);

    // globalInterceptors
    Collection<AbstractGlobalInterceptor> globalInterceptors = getBeans(AbstractGlobalInterceptor.class);
    httpClientConfigBean.setGlobalInterceptors(globalInterceptors);

    // retryInterceptor
    Class<? extends AbstractRetryInterceptor> retryInterceptor =
        httpClientProperties.getRetryInterceptor();
    httpClientConfigBean.setRetryInterceptor(retryInterceptor.newInstance());

    // add networkInterceptor
    Collection<MixHttpClientInterceptor> networkInterceptors = getBeans(MixHttpClientInterceptor.class);
    httpClientConfigBean.setNetworkInterceptors(networkInterceptors);

    // add ServiceInstanceChooserInterceptor
    ServiceInstanceChooser serviceInstanceChooser;
    try {
      serviceInstanceChooser = applicationContext.getBean(ServiceInstanceChooser.class);
    } catch (BeansException e) {
      serviceInstanceChooser = new InvalidServiceInstanceChooser();
    }

    ServiceInstanceChooserInterceptor serviceInstanceChooserInterceptor =
        new ServiceInstanceChooserInterceptor(serviceInstanceChooser);
    httpClientConfigBean.setServiceInstanceChooserInterceptor(serviceInstanceChooserInterceptor);

    return httpClientConfigBean;
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
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
