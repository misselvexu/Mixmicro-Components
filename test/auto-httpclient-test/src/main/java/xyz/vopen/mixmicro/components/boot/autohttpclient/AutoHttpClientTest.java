package xyz.vopen.mixmicro.components.boot.autohttpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientScan;

/**
 * {@link AutoHttpClientTest}
 *
 * <p>Class AutoHttpClientTest Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/10/30
 */
@SpringBootApplication
@MixHttpClientScan(basePackages = "xyz.vopen.mixmicro.components.boot.autohttpclient")
public class AutoHttpClientTest {

  public static void main(String[] args) {
    SpringApplication.run(AutoHttpClientTest.class, args);
  }
}
