package xyz.vopen.mixmicro.components.mongo.client.validation;

import xyz.vopen.mixmicro.components.mongo.client.utils.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class VerboseJSR303ConstraintViolationException extends ConstraintViolationException {
  /**
   * Creates a VerboseJSR303ConstraintViolationException
   *
   * @param vio the violations
   */
  public VerboseJSR303ConstraintViolationException(final Set<ConstraintViolation<?>> vio) {
    super(createVerboseMessage(vio), vio);
    Assert.parameterNotNull("vio", vio);
  }

  private static String createVerboseMessage(final Set<ConstraintViolation<?>> vio) {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("The following constraints have been violated:\n");
    for (final ConstraintViolation<?> c : vio) {
      sb.append(c.getRootBeanClass().getSimpleName());
      sb.append(".");
      sb.append(c.getPropertyPath());
      sb.append(": ");
      sb.append(c.getMessage());
      sb.append(" ('");
      sb.append(c.getInvalidValue());
      sb.append("')\n");
    }
    return sb.toString();
  }
}
