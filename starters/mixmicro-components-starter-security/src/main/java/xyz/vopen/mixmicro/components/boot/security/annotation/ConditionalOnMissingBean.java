package xyz.vopen.mixmicro.components.boot.security.annotation;

import org.springframework.context.annotation.Conditional;
import xyz.vopen.mixmicro.components.boot.security.condition.OnMissingBeanCondition;

import java.lang.annotation.*;

/**
 * Similar to Spring's condition but supports placeholders. It only works with explicit {@link
 * org.springframework.context.annotation.Bean} name attribute specified.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMissingBeanCondition.class)
public @interface ConditionalOnMissingBean {}
