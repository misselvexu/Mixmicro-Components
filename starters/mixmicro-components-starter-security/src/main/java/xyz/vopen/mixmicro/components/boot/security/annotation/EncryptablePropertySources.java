package xyz.vopen.mixmicro.components.boot.security.annotation;

import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.boot.security.core.EncryptablePropertySourceConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation that aggregates several {@link EncryptablePropertySource} annotations.
 *
 * <p>Can be used natively, declaring several nested {@link EncryptablePropertySource} annotations.
 * Can also be used in conjunction with Java 8's support for <em>repeatable annotations</em>, where
 * {@link EncryptablePropertySource} can simply be declared several times on the same {@linkplain
 * ElementType#TYPE type}, implicitly generating this container annotation.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see EncryptablePropertySource
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EncryptablePropertySourceConfiguration.class)
public @interface EncryptablePropertySources {
  EncryptablePropertySource[] value();
}
