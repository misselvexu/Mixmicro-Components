package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists a integer primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class IntType extends IntegerObjectType {

  private static final IntType singleTon = new IntType();

  private IntType() {
    super(SqlType.INTEGER, new Class<?>[] {int.class});
  }

  /** Here for others to subclass. */
  protected IntType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static IntType getSingleton() {
    return singleTon;
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
