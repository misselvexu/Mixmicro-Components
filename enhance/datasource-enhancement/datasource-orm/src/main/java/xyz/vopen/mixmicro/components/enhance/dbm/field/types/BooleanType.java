package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists a boolean primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class BooleanType extends BooleanObjectType {

  private static final BooleanType singleTon = new BooleanType();

  private BooleanType() {
    super(SqlType.BOOLEAN, new Class<?>[] {boolean.class});
  }

  protected BooleanType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  protected BooleanType(SqlType sqlType) {
    super(sqlType);
  }

  public static BooleanType getSingleton() {
    return singleTon;
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
