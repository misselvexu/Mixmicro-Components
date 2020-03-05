package xyz.vopen.mixmicro.components.boot.openfeign.decoder;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import xyz.vopen.mixmicro.components.common.ResponseEntity;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * {@link OpenFeignDecoder}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
public class OpenFeignDecoder implements Decoder {

  private OptionalDecoder decoder;

  public OpenFeignDecoder(OptionalDecoder decoder) {
    this.decoder = decoder;
  }

  @Override
  public Object decode(Response response, Type type)
      throws IOException, DecodeException, FeignException {

    // WARN：performance risks here
    byte[] bodyData = Util.toByteArray(response.body().asInputStream());
    try {
      ResponseEntity<?> temp = ResponseEntity.decode(bodyData, ResponseEntity.class);

      return decoder.decode(
          response.toBuilder().body(ResponseEntity.bytes(temp.getData())).build(), type);
    } catch (Exception ignored) {
    }
    //
    return decoder.decode(response, type);
  }
}
