package xyz.vopen.mixmicro.components.boot.swagger.test.p2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link D2Controller}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
@Api
@RestController
public class D2Controller {

  @ApiOperation("/h2")
  @GetMapping("/h2")
  String hi2() {
    return "h2";
  }
}
