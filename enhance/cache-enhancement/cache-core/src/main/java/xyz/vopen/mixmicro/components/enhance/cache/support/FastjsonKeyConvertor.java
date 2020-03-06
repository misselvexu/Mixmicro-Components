package xyz.vopen.mixmicro.components.enhance.cache.support;

import com.alibaba.fastjson.JSON;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class FastjsonKeyConvertor implements Function<Object, Object> {

    public static final FastjsonKeyConvertor INSTANCE = new FastjsonKeyConvertor();

    @Override
    public Object apply(Object originalKey) {
        if (originalKey == null) {
            return null;
        }
        if (originalKey instanceof String) {
            return originalKey;
        }
        return JSON.toJSONString(originalKey);
    }

}

