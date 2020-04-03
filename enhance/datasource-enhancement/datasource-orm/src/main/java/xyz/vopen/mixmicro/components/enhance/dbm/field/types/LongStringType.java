package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Persists the {@link String} Java class but with more storage in the database.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LongStringType extends StringType {

  private static final LongStringType singleTon = new LongStringType();

  private LongStringType() {
    super(SqlType.LONG_STRING);
  }

  /** Here for others to subclass. */
  protected LongStringType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static LongStringType getSingleton() {
    return singleTon;
  }

  @Override
  public boolean isAppropriateId() {
    return false;
  }

  @Override
  public int getDefaultWidth() {
    return 0;
  }

  @Override
  public Class<?> getPrimaryClass() {
    return String.class;
  }
}
