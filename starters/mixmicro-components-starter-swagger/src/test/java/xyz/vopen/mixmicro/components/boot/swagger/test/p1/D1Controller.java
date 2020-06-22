package xyz.vopen.mixmicro.components.boot.swagger.test.p1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link D1Controller}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
@Api
@RestController
public class D1Controller {

  @ApiOperation("/h1")
  @GetMapping("/h1")
  String hi1() {
    return "h1";
  }
}
