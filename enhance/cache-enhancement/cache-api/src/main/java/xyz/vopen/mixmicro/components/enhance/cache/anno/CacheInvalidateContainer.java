package xyz.vopen.mixmicro.components.enhance.cache.anno;

import java.lang.annotation.*;

/**
 * @author <a href="scolia@qq.com">scolia</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheInvalidateContainer {

    CacheInvalidate[] value();
}
