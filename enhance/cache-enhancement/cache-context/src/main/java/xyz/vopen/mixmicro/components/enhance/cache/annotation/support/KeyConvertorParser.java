package xyz.vopen.mixmicro.components.enhance.cache.annotation.support;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface KeyConvertorParser {
  Function<Object, Object> parseKeyConvertor(String convertor);
}
