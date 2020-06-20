package xyz.vopen.mixmicro.components.boot.swagger.annotation;

import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.boot.swagger.autoconfigure.MixmicroSwaggerAutoConfiguration;

import java.lang.annotation.*;

/**
 * {@link EnableMixmicroSwagger}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MixmicroSwaggerAutoConfiguration.class})
public @interface EnableMixmicroSwagger {}
