package xyz.vopen.mixmicro.components.enhance.cache.anno;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface SerialPolicy {

  String JAVA = "JAVA";

  String KRYO = "KRYO";

  Function<Object, byte[]> encoder();

  Function<byte[], Object> decoder();
}
