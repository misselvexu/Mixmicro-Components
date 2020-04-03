package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

/**
 * Type that persists a char primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class CharType extends CharacterObjectType {

  private static final CharType singleTon = new CharType();

  private CharType() {
    super(SqlType.CHAR, new Class<?>[] {char.class});
  }

  /** Here for others to subclass. */
  protected CharType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static CharType getSingleton() {
    return singleTon;
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
    Character character = (Character) javaObject;
    // this is required because otherwise we try to store \0 in the database
    if (character == null || character == 0) {
      return null;
    } else {
      return character;
    }
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
