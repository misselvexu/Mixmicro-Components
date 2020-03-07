package xyz.vopen.mixmicro.components.enhance.cache.annotation.config;

/**
 * Created on 2018/12/13.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */

import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.field.CreateCacheAnnotationBeanPostProcessor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CommonConfiguration.class, CreateCacheAnnotationBeanPostProcessor.class})
public @interface EnableCreateCacheAnnotation {}
