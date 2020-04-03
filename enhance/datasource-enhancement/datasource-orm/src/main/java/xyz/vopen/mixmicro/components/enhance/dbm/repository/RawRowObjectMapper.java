package xyz.vopen.mixmicro.components.enhance.dbm.repository;

import xyz.vopen.mixmicro.components.enhance.dbm.field.DataType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.QueryBuilder;

import java.sql.SQLException;

/**
 * Parameterized row mapper that takes output from the {@link GenericRawResults} and returns a T. Is
 * used in the {@link Repository#queryRaw(String, DataType[], RawRowObjectMapper, String...)} method.
 *
 * <p><b> NOTE: </b> If you need to map Strings instead then consider using the {@link RawRowMapper}
 * with the {@link Repository#queryRaw(String, RawRowMapper, String...)} method which allows you to iterate
 * over the raw results as String[].
 *
 * @param <T> Type that the mapRow returns.
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface RawRowObjectMapper<T> {

  /**
   * Used to convert a raw results row to an object.
   *
   * <p><b>NOTE:</b> If you are using the {@link QueryBuilder#prepareStatementString()} to build
   * your query, it may have added the id column to the selected column list if the Repository object has
   * an id you did not include it in the columns you selected. So the results might have one more
   * column than you are expecting.
   *
   * @return The created object with all of the fields set from the results. Return null if there is
   *     no object generated from these results.
   * @param columnNames Array of names of columns.
   * @param dataTypes Array of the DataTypes of each of the columns as passed into the {@link
   *     Repository#queryRaw(String, DataType[], RawRowObjectMapper, String...)}
   * @param resultColumns Array of result columns.
   * @throws SQLException If there is any critical error with the data and you want to stop the
   *     paging.
   */
  public T mapRow(String[] columnNames, DataType[] dataTypes, Object[] resultColumns)
      throws SQLException;
}
