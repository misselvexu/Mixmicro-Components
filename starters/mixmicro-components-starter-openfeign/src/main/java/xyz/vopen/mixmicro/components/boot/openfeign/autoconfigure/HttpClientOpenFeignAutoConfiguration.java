package xyz.vopen.mixmicro.components.boot.openfeign.autoconfigure;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@link HttpClientOpenFeignAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
@Configuration
@ConditionalOnClass({ApacheHttpClient.class})
@ConditionalOnMissingClass({"com.netflix.loadbalancer.ILoadBalancer"})
@ConditionalOnMissingBean({CloseableHttpClient.class})
@ConditionalOnProperty(
    value = {"feign.httpclient.enabled"},
    matchIfMissing = true)
public class HttpClientOpenFeignAutoConfiguration {

  private final Timer timer = new Timer("FeignApacheHttpClientConfiguration.connectionManagerTimer", true);

  private final RegistryBuilder registryBuilder;

  private CloseableHttpClient httpClient;

  public HttpClientOpenFeignAutoConfiguration(RegistryBuilder registryBuilder) {
    this.registryBuilder = registryBuilder;
  }

  @Bean
  @ConditionalOnMissingBean({HttpClientConnectionManager.class})
  public HttpClientConnectionManager connectionManager(
      ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
      FeignHttpClientProperties httpClientProperties) {

    final HttpClientConnectionManager connectionManager =
        connectionManagerFactory.newConnectionManager(
            httpClientProperties.isDisableSslValidation(),
            httpClientProperties.getMaxConnections(),
            httpClientProperties.getMaxConnectionsPerRoute(),
            httpClientProperties.getTimeToLive(),
            httpClientProperties.getTimeToLiveUnit(),
            this.registryBuilder);

    this.timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            connectionManager.closeExpiredConnections();
          }
        },
        30000L,
        httpClientProperties.getConnectionTimerRepeat());

    // RETURN
    return connectionManager;
  }

  @Bean
  public CloseableHttpClient httpClient(
      ApacheHttpClientFactory httpClientFactory,
      HttpClientConnectionManager httpClientConnectionManager,
      FeignHttpClientProperties httpClientProperties) {

    RequestConfig defaultRequestConfig =
        RequestConfig.custom()
            .setConnectTimeout(httpClientProperties.getConnectionTimeout())
            .setRedirectsEnabled(httpClientProperties.isFollowRedirects())
            .build();
    this.httpClient =
        httpClientFactory
            .createBuilder()
            .setConnectionManager(httpClientConnectionManager)
            .setDefaultRequestConfig(defaultRequestConfig)
            .build();

    // RETURN
    return this.httpClient;
  }

  @Bean
  @ConditionalOnMissingBean({Client.class})
  public Client feignClient(HttpClient httpClient) {
    return new ApacheHttpClient(httpClient);
  }

  @PreDestroy
  public void destroy() throws Exception {
    this.timer.cancel();
    if (this.httpClient != null) {
      this.httpClient.close();
    }
  }
}
