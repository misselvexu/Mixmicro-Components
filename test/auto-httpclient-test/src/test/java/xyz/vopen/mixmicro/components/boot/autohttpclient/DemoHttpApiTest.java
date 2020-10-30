package xyz.vopen.mixmicro.components.boot.autohttpclient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * {@link DemoHttpApiTest}
 *
 * <p>Class DemoHttpApiTest Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/10/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AutoHttpClientTest.class)
public class DemoHttpApiTest {

  @Autowired private DemoHttpApi demoHttpApi;

  @Test
  public void index() throws IOException {

    System.out.println(demoHttpApi.index().string());
  }
}