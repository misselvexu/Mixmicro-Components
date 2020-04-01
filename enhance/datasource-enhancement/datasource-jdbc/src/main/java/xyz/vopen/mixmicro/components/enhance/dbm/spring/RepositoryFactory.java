package xyz.vopen.mixmicro.components.enhance.dbm.spring;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.RepositoryManager;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.table.DatabaseTableConfig;

import java.sql.SQLException;

/**
 * Spring bean that can be used to create Repository's of certain classes without needing their own Repository
 * class.
 *
 * <p>Here is an example of spring wiring. See the Spring example in the documentation for more
 * info.
 *
 * <pre>
 * 	&lt;bean id="accountRepository" class="RepositoryFactory" factory-method="createRepository"&gt;
 * 		&lt;constructor-arg index="0" ref="connectionSource" /&gt;
 * 		&lt;constructor-arg index="1" value="com.acmedcare.framework.dbm.orm.examples.spring.Account" /&gt;
 * 	&lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RepositoryFactory {

  /** Create and return a Repository based on the arguments. */
  public static <T, ID> Repository<T, ID> createRepository(ConnectionSource connectionSource, Class<T> clazz)
      throws SQLException {
    return RepositoryManager.createRepository(connectionSource, clazz);
  }

  /** Create and return a Repository based on the arguments. */
  public static <T, ID> Repository<T, ID> createRepository(
      ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
    return RepositoryManager.createRepository(connectionSource, tableConfig);
  }
}
