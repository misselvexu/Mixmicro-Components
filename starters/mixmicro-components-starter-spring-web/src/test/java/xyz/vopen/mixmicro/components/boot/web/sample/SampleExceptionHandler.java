package xyz.vopen.mixmicro.components.boot.web.sample;

import org.springframework.http.ResponseEntity;
import xyz.vopen.mixmicro.components.boot.web.ExceptionHandler;

/**
 * {@link SampleExceptionHandler}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
public class SampleExceptionHandler implements ExceptionHandler {

  @Override
  public ResponseEntity<?> handlerException(Exception exception) {

    if (exception instanceof SampleException) {

      return ResponseEntity.status(500)
          .body(xyz.vopen.mixmicro.components.common.ResponseEntity.fail(-2, "sample exception"));
    }

    return ResponseEntity.status(500)
        .body(xyz.vopen.mixmicro.components.common.ResponseEntity.fail(-2, "other exception"));
  }
}
