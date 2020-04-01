package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists a double primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DoubleType extends DoubleObjectType {

  private static final DoubleType singleTon = new DoubleType();

  private DoubleType() {
    super(SqlType.DOUBLE, new Class<?>[] {double.class});
  }

  /** Here for others to subclass. */
  protected DoubleType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static DoubleType getSingleton() {
    return singleTon;
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
