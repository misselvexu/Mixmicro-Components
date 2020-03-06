package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import xyz.vopen.mixmicro.components.enhance.cache.CacheConfigException;
import xyz.vopen.mixmicro.components.enhance.cache.anno.KeyConvertor;
import xyz.vopen.mixmicro.components.enhance.cache.support.FastjsonKeyConvertor;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class DefaultKeyConvertorParser implements KeyConvertorParser {
    @Override
    public Function<Object, Object> parseKeyConvertor(String convertor) {
        if (convertor == null) {
            return null;
        }
        if (KeyConvertor.FASTJSON.equalsIgnoreCase(convertor)) {
            return FastjsonKeyConvertor.INSTANCE;
        } else if (KeyConvertor.NONE.equalsIgnoreCase(convertor)) {
            return null;
        }
        throw new CacheConfigException("not supported:" + convertor);
    }
}
