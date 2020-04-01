package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.lang.reflect.Field;

/**
 * Marker class used to see if we have a customer persister defined.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class VoidType extends BaseDataType {

  VoidType() {
    super(null, new Class<?>[] {});
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return null;
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) {
    return null;
  }

  @Override
  public boolean isValidForField(Field field) {
    return false;
  }
}
