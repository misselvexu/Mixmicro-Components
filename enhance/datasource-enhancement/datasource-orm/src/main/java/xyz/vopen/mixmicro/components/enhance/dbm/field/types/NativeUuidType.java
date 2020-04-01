package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

import java.util.UUID;

/**
 * Type that persists a {@link UUID} object but as a UUID type which is supported by a couple of
 * database-types.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class NativeUuidType extends UuidType {

  private static final NativeUuidType singleTon = new NativeUuidType();

  private NativeUuidType() {
    super(SqlType.UUID);
  }

  /** Here for others to subclass. */
  protected NativeUuidType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static NativeUuidType getSingleton() {
    return singleTon;
  }
}
