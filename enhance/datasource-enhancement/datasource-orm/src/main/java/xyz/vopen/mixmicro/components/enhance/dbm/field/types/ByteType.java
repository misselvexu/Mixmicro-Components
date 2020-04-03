package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists a byte primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ByteType extends ByteObjectType {

  private static final ByteType singleTon = new ByteType();

  private ByteType() {
    super(SqlType.BYTE, new Class<?>[] {byte.class});
  }

  /** Here for others to subclass. */
  protected ByteType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static ByteType getSingleton() {
    return singleTon;
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
