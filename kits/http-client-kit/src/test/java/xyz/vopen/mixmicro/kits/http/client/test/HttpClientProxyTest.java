package xyz.vopen.mixmicro.kits.http.client.test;

import xyz.vopen.mixmicro.kits.http.client.AbstractHttpClient;
import xyz.vopen.mixmicro.kits.http.client.HttpClient;
import xyz.vopen.mixmicro.kits.http.client.HttpResponse;
import xyz.vopen.mixmicro.kits.http.client.HttpStatus;

/**
 * {@link HttpClientProxyTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-03-21.
 */
public class HttpClientProxyTest {

  public static void main(String[] args) {

    HttpClient httpClient = HttpClient.getInstance();


    HttpResponse httpResponse = new HttpResponse();
    httpClient.request(AbstractHttpClient.METHOD.GET,"http://www.baidu.com",null,httpResponse);

    if(httpResponse.getStatusCode() == HttpStatus.SC_OK) {
      System.out.println(httpResponse.getResult());
    }

  }
}
