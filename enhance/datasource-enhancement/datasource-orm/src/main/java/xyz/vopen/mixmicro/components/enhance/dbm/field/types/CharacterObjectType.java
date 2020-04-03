package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a Character object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class CharacterObjectType extends BaseDataType {

  private static final CharacterObjectType singleTon = new CharacterObjectType();

  private CharacterObjectType() {
    super(SqlType.CHAR, new Class<?>[] {Character.class});
  }

  protected CharacterObjectType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static CharacterObjectType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
    if (defaultStr.length() != 1) {
      throw new SQLException(
          "Problems with field "
              + fieldType
              + ", default string to long for Character: '"
              + defaultStr
              + "'");
    }
    return (Character) defaultStr.charAt(0);
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (Character) results.getChar(columnPos);
  }
}
