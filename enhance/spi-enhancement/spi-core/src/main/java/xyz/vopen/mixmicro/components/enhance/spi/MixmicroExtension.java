package xyz.vopen.mixmicro.components.enhance.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展点
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see MixmicroExtensible
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MixmicroExtension {
  /**
   * 扩展点名字
   *
   * @return 扩展点名字
   */
  String value();

  /**
   * 优先级排序，默认不需要，大的优先级高
   *
   * @return 排序
   */
  int order() default 0;
}
