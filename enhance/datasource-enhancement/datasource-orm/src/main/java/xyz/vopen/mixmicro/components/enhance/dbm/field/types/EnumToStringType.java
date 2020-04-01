package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists an enum as its string value produced by call @{link {@link Enum#toString()}.
 * You can also use the {@link EnumIntegerType}. If you want to use the {@link Enum#name()} instead,
 * see the {@link EnumStringType}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class EnumToStringType extends EnumStringType {

  private static final EnumToStringType singleTon = new EnumToStringType();

  private EnumToStringType() {
    super(SqlType.STRING, new Class<?>[] {Enum.class});
  }

  /** Here for others to subclass. */
  protected EnumToStringType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static EnumToStringType getSingleton() {
    return singleTon;
  }

  @Override
  protected String getEnumName(Enum<?> enumVal) {
    return enumVal.toString();
  }
}
