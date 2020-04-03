package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists a float primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FloatType extends FloatObjectType {

  private static final FloatType singleTon = new FloatType();

  private FloatType() {
    super(SqlType.FLOAT, new Class<?>[] {float.class});
  }

  /** Here for others to subclass. */
  protected FloatType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static FloatType getSingleton() {
    return singleTon;
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
