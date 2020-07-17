package xyz.vopen.mixmicro.components.health;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This qualifier is used to define a liveness Health Check procedure
 *
 * @author Antoine Sabot-Durand
 * @since 2.0
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Liveness {

  /**
   * Support inline instantiation of the {@link Liveness} qualifier.
   *
   * @author Antoine Sabot-Durand
   * @since 2.2
   */
  public static final class Literal extends AnnotationLiteral<Liveness> implements Liveness {

    public static final Literal INSTANCE = new Literal();

    private static final long serialVersionUID = 1L;
  }
}
