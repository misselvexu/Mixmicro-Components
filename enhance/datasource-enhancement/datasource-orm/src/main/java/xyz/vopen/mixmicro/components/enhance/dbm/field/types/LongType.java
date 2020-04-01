package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists a long primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LongType extends LongObjectType {

  private static final LongType singleTon = new LongType();

  private LongType() {
    super(SqlType.LONG, new Class<?>[] {long.class});
  }

  /** Here for others to subclass. */
  protected LongType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static LongType getSingleton() {
    return singleTon;
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
