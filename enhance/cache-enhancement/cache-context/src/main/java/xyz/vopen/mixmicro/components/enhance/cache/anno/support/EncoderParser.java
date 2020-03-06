package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface EncoderParser {
  Function<Object, byte[]> parseEncoder(String valueEncoder);

  Function<byte[], Object> parseDecoder(String valueDecoder);
}
