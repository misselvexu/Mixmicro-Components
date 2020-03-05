package xyz.vopen.mixmicro.components.boot.openfeign.autoconfigure;

import feign.Client;
import feign.okhttp.OkHttpClient;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * {@link OkHttpOpenFeignAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
@Configuration
@ConditionalOnClass({OkHttpClient.class})
@ConditionalOnMissingClass({"com.netflix.loadbalancer.ILoadBalancer"})
@ConditionalOnMissingBean({okhttp3.OkHttpClient.class})
@ConditionalOnProperty({"feign.okhttp.enabled"})
public class OkHttpOpenFeignAutoConfiguration {

  private okhttp3.OkHttpClient okHttpClient;

  protected OkHttpOpenFeignAutoConfiguration() {}

  @Bean
  @ConditionalOnMissingBean({ConnectionPool.class})
  public ConnectionPool httpClientConnectionPool(
      FeignHttpClientProperties httpClientProperties,
      OkHttpClientConnectionPoolFactory connectionPoolFactory) {
    int maxTotalConnections = httpClientProperties.getMaxConnections();
    long timeToLive = httpClientProperties.getTimeToLive();
    TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
    return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
  }

  @Bean
  public okhttp3.OkHttpClient client(
      OkHttpClientFactory httpClientFactory,
      ConnectionPool connectionPool,
      FeignHttpClientProperties httpClientProperties) {
    boolean followRedirects = httpClientProperties.isFollowRedirects();
    int connectTimeout = httpClientProperties.getConnectionTimeout();
    boolean disableSslValidation = httpClientProperties.isDisableSslValidation();
    this.okHttpClient =
        httpClientFactory
            .createBuilder(disableSslValidation)
            .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
            .followRedirects(followRedirects)
            .connectionPool(connectionPool)
            .build();
    return this.okHttpClient;
  }

  @PreDestroy
  public void destroy() {
    if (this.okHttpClient != null) {
      this.okHttpClient.dispatcher().executorService().shutdown();
      this.okHttpClient.connectionPool().evictAll();
    }
  }

  @Bean
  @ConditionalOnMissingBean({Client.class})
  public Client feignClient(okhttp3.OkHttpClient client) {
    return new OkHttpClient(client);
  }
}
