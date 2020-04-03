package xyz.vopen.mixmicro.components.enhance.dbm.table;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;

import java.lang.reflect.Constructor;
import java.sql.SQLException;

/**
 * Interface that allows you to inject a factory class that creates objects of this class. You set
 * it on the Repository using: {@link Repository#setObjectFactory(ObjectFactory)}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface ObjectFactory<T> {

  /**
   * Construct and return an object of a certain class.
   *
   * @throws SQLException if there was a problem creating the object.
   */
  public T createObject(Constructor<T> construcor, Class<T> dataClass) throws SQLException;
}
