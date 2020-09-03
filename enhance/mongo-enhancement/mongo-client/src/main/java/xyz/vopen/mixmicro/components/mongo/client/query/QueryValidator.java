package xyz.vopen.mixmicro.components.mongo.client.query;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.AllOperationValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.DefaultTypeValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.DoubleTypeValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.EntityAnnotatedValueValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.EntityTypeAndIdValueValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.ExistsOperationValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.GeoWithinOperationValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.InOperationValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.IntegerTypeValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.KeyValueTypeValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.ListValueValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.LongTypeValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.ModOperationValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.NotInOperationValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.PatternValueValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.SizeOperationValidator;
import xyz.vopen.mixmicro.components.mongo.client.query.validation.ValidationFailure;

import java.util.List;

final class QueryValidator {
  private QueryValidator() {}

  /*package*/
  static boolean isCompatibleForOperator(
      final MappedClass mappedClass,
      final MappedField mappedField,
      final Class<?> type,
      final FilterOperator op,
      final Object value,
      final List<ValidationFailure> validationFailures) {
    // TODO: it's really OK to have null values?  I think this is to prevent null pointers further
    // down,
    // but I want to move the null check into the operations that care whether they allow nulls or
    // not.
    if (value == null || type == null) {
      return true;
    }

    boolean validationApplied =
        ExistsOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
            || SizeOperationValidator.getInstance()
                .apply(mappedField, op, value, validationFailures)
            || InOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
            || NotInOperationValidator.getInstance()
                .apply(mappedField, op, value, validationFailures)
            || ModOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
            || GeoWithinOperationValidator.getInstance()
                .apply(mappedField, op, value, validationFailures)
            || AllOperationValidator.getInstance().apply(mappedField, op, value, validationFailures)
            || KeyValueTypeValidator.getInstance().apply(type, value, validationFailures)
            || IntegerTypeValidator.getInstance().apply(type, value, validationFailures)
            || LongTypeValidator.getInstance().apply(type, value, validationFailures)
            || DoubleTypeValidator.getInstance().apply(type, value, validationFailures)
            || PatternValueValidator.getInstance().apply(type, value, validationFailures)
            || EntityAnnotatedValueValidator.getInstance().apply(type, value, validationFailures)
            || ListValueValidator.getInstance().apply(type, value, validationFailures)
            || EntityTypeAndIdValueValidator.getInstance()
                .apply(mappedClass, mappedField, value, validationFailures)
            || DefaultTypeValidator.getInstance().apply(type, value, validationFailures);

    return validationApplied && validationFailures.isEmpty();
  }
}
