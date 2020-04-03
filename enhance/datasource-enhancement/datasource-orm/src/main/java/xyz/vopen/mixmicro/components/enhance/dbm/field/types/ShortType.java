package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists a short primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ShortType extends ShortObjectType {

  private static final ShortType singleTon = new ShortType();

  private ShortType() {
    super(SqlType.SHORT, new Class<?>[] {short.class});
  }

  /** Here for others to subclass. */
  protected ShortType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static ShortType getSingleton() {
    return singleTon;
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
