package xyz.vopen.mixmicro.components.enhance.cache.anno.config;

import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.enhance.cache.anno.field.CreateCacheAnnotationBeanPostProcessor;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CommonConfiguration.class, CreateCacheAnnotationBeanPostProcessor.class})
public @interface EnableCreateCacheAnnotation {}
