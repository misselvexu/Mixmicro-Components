package xyz.vopen.mixmicro.components.mongo.client.query.validation;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.query.FilterOperator;

import java.util.List;

import static java.lang.String.format;
import static xyz.vopen.mixmicro.components.mongo.client.query.validation.CollectionTypeValidator.typeIsIterableOrArrayOrMap;

/** Checks if the value can have the {@code FilterOperator.NOT_IN} operator applied to it. */
public final class NotInOperationValidator extends OperationValidator {
  private static final NotInOperationValidator INSTANCE = new NotInOperationValidator();

  private NotInOperationValidator() {}

  /**
   * Get the instance
   *
   * @return the Singleton instance of this validator
   */
  public static NotInOperationValidator getInstance() {
    return INSTANCE;
  }

  @Override
  protected FilterOperator getOperator() {
    return FilterOperator.NOT_IN;
  }

  @Override
  protected void validate(
      final MappedField mappedField,
      final Object value,
      final List<ValidationFailure> validationFailures) {
    if (value == null) {
      validationFailures.add(
          new ValidationFailure(format("For a $nin operation, value cannot be null.")));
    } else if (!typeIsIterableOrArrayOrMap(value.getClass())) {
      validationFailures.add(
          new ValidationFailure(
              format(
                  "For a $nin operation, value '%s' should be a List or array. "
                      + "Instead it was a: %s",
                  value, value.getClass())));
    }
  }
}
