package xyz.vopen.mixmicro.components.enhance.dbm.spring;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.BaseRepositoryImpl;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.table.DatabaseTableConfig;
import xyz.vopen.mixmicro.components.enhance.dbm.table.TableUtils;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Spring bean that auto-creates any tables that it finds Repositorys for if the property name in
 * TableCreator.AUTO_CREATE_TABLES property has been set to true. It will also auto-drop any tables
 * that were auto-created if the property name in TableCreator.AUTO_DROP_TABLES property has been
 * set to true.
 *
 * <p><b> NOTE: </b> If you are using the Spring type wiring in Java, {@link #initialize} should be
 * called after all of the set methods. In Spring XML, init-method="initialize" should be used.
 *
 * <p>Here is an example of spring wiring.
 *
 * <pre>
 * &lt;!-- our database type factory-bean --&gt;
 * &lt;bean id="tableCreator" class="TableCreator" init-method="initialize"&gt;
 * 	&lt;property name="connectionSource" ref="connectionSource" /&gt;
 * 	&lt;property name="configuredRepositories"&gt;
 * 		&lt;list&gt;
 * 			&lt;ref bean="accountRepository" /&gt;
 * 			&lt;ref bean="orderRepository" /&gt;
 * 		&lt;/list&gt;
 * 	&lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class TableCreator {

  public static final String AUTO_CREATE_TABLES = "dbm.orm.auto.create.tables";
  public static final String AUTO_DROP_TABLES = "dbm.orm.auto.drop.tables";

  private ConnectionSource connectionSource;
  private List<Repository<?, ?>> configuredRepositories;
  private Set<DatabaseTableConfig<?>> createdClasses = new HashSet<DatabaseTableConfig<?>>();

  public TableCreator() {
    // for spring
  }

  public TableCreator(ConnectionSource connectionSource, List<Repository<?, ?>> configuredRepositories) {
    this.connectionSource = connectionSource;
    this.configuredRepositories = configuredRepositories;
  }

  /**
   * Possibly create the tables is the {@link #AUTO_CREATE_TABLES} system property is set to "true".
   */
  public void maybeCreateTables() throws SQLException {
    initialize();
  }

  /**
   * If you are using the Spring type wiring, this should be called after all of the set methods.
   */
  public void initialize() throws SQLException {
    if (!Boolean.parseBoolean(System.getProperty(AUTO_CREATE_TABLES))) {
      return;
    }

    if (configuredRepositories == null) {
      throw new SQLException("configuredRepositories was not set in " + getClass().getSimpleName());
    }

    // find all of the repositorys and create the tables
    for (Repository<?, ?> repository : configuredRepositories) {
      Class<?> clazz = repository.getDataClass();
      try {
        DatabaseTableConfig<?> tableConfig = null;
        if (repository instanceof BaseRepositoryImpl) {
          tableConfig = ((BaseRepositoryImpl<?, ?>) repository).getTableConfig();
        }
        if (tableConfig == null) {
          tableConfig = DatabaseTableConfig.fromClass(connectionSource.getDatabaseType(), clazz);
        }
        TableUtils.createTable(connectionSource, tableConfig);
        createdClasses.add(tableConfig);
      } catch (Exception e) {
        // we don't stop because the table might already exist
        System.err.println("Was unable to auto-create table for " + clazz);
        e.printStackTrace();
      }
    }
  }

  /**
   * Possibly drop the tables that were previously created if the {@link #AUTO_DROP_TABLES} system
   * property is set to "true".
   */
  public void maybeDropTables() {
    destroy();
  }

  public void destroy() {
    if (!Boolean.parseBoolean(System.getProperty(AUTO_DROP_TABLES))) {
      return;
    }
    for (DatabaseTableConfig<?> tableConfig : createdClasses) {
      try {
        TableUtils.dropTable(connectionSource, tableConfig, false);
      } catch (Exception e) {
        // we don't stop because the table might already exist
        System.err.println("Was unable to auto-drop table for " + tableConfig.getDataClass());
        e.printStackTrace();
      }
    }
    createdClasses.clear();
  }

  // This is @Required
  public void setConnectionSource(ConnectionSource dataSource) {
    this.connectionSource = dataSource;
  }

  public void setConfiguredRepositories(List<Repository<?, ?>> configuredRepositories) {
    this.configuredRepositories = configuredRepositories;
  }
}
