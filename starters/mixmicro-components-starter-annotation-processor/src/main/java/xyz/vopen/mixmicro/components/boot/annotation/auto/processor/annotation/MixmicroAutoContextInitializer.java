package xyz.vopen.mixmicro.components.boot.annotation.auto.processor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * ApplicationContextInitializer 处理
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Documented
@Retention(SOURCE)
@Target(TYPE)
public @interface MixmicroAutoContextInitializer {}
