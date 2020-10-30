package xyz.vopen.mixmicro.components.boot.autohttpclient;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientLogStrategy;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.MixHttpClient;

/**
 * {@link DemoHttpApi}
 *
 * <p>Class DemoHttpApi Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/10/30
 */
@MixHttpClient(baseUrl = "https://www.baidu.com", logStrategy = MixHttpClientLogStrategy.HEADERS)
public interface DemoHttpApi {

  @GET("/")
  ResponseBody index();

}
