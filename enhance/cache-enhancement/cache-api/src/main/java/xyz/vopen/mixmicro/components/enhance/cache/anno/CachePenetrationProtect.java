package xyz.vopen.mixmicro.components.enhance.cache.anno;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 * @version ${project.version}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface CachePenetrationProtect {
    boolean value() default true;
    int timeout() default CacheConsts.UNDEFINED_INT;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
