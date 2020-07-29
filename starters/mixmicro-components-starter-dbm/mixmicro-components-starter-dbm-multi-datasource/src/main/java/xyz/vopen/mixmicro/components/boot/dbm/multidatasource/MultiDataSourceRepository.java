package xyz.vopen.mixmicro.components.boot.dbm.multidatasource;

/**
 * {@link MultiDataSourceRepository}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/1
 */
public interface MultiDataSourceRepository {

  /**
   * Defined Repository Database Schema Name
   *
   * @return schema name
   */
  String schema();

  /**
   * Defined Repository Model Class Type
   *
   * @return class type.
   */
  <T> Class<T> modelClass();
}
