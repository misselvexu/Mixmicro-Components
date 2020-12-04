package xyz.vopen.mixmicro.components.boot.autohttpclient;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientLogStrategy;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.MixHttpClient;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.DegradeStrategy;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.MixHttpClientDegrade;

/**
 * {@link DemoHttpApi}
 *
 * <p>Class DemoHttpApi Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/10/30
 */
@MixHttpClient(
    baseUrl = "https://www.google.com",
    logStrategy = MixHttpClientLogStrategy.HEADERS,
    fallback = DemoHttpApiFallback.class,
    fallbackFactory = DemoHttpApiFallbackFactory.class)
public interface DemoHttpApi {

  @GET("/")
  @MixHttpClientDegrade(count = 1, degradeStrategy = DegradeStrategy.AVERAGE_RT)
  ResponseBody index();
}
