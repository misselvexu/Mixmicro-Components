package xyz.vopen.mixmicro.components.boot.autohttpclient;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;

/**
 * {@link DemoHttpApiFallback}
 *
 * <p>Class DemoHttpApiFallback Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
@Component
public class DemoHttpApiFallback implements DemoHttpApi {

  @Override
  public ResponseBody index() {
    return ResponseBody.create(MediaType.parse("application/json"), "{\"error\": true}");
  }
}
