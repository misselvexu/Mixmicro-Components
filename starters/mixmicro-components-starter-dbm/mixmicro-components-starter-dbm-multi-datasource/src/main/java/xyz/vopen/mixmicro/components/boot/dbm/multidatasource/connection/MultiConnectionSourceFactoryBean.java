package xyz.vopen.mixmicro.components.boot.dbm.multidatasource.connection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.exception.MultiDataSourceRepositoryException;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;

import javax.inject.Provider;
import java.io.Serializable;

/**
 * {@link MultiConnectionSourceFactoryBean}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/3
 */
public class MultiConnectionSourceFactoryBean extends AbstractFactoryBean<Provider<MultiConnectionSourceFactory>> {

  /**
   * This abstract method declaration mirrors the method in the FactoryBean interface, for a
   * consistent offering of abstract template methods.
   *
   * @see FactoryBean#getObjectType()
   */
  @Override
  public Class<?> getObjectType() {
    return MultiConnectionSourceFactory.class;
  }

  /**
   * This method will return connection source instance of {@link ConnectionSource}
   *
   * @param schema connection source scheme name
   * @return return initialized instance of {@link ConnectionSource}
   */
  protected ConnectionSource getConnection(String schema) {

    try {
      Assert.notNull(schema,"ConnectionSource schema can't be null or blank.");

      Provider<MultiConnectionSourceFactory> provider = getObject();

      Assert.notNull(provider, "ConnectionSourceFactory provider must not be null. ");

      return provider.get().getConnectionSource(schema);

    } catch (Exception e) {

      throw new MultiDataSourceRepositoryException("not found connection source of schame :" + schema);
    }
  }

  /**
   * Template method that subclasses must override to construct the object returned by this factory.
   *
   * <p>Invoked on initialization of this FactoryBean in case of a singleton; else, on each {@link
   * #getObject()} call.
   *
   * @return the object returned by this factory
   * @throws Exception if an exception occurred during object creation
   * @see #getObject()
   */
  @Override
  @NonNull
  protected Provider<MultiConnectionSourceFactory> createInstance() throws Exception {
    BeanFactory beanFactory = getBeanFactory();
    Assert.state(beanFactory != null, "No BeanFactory available");
    return new ConnectionSourceFactoryProvider(beanFactory);
  }

  private static class ConnectionSourceFactoryProvider
      implements Provider<MultiConnectionSourceFactory>, Serializable {

    private final BeanFactory beanFactory;

    public ConnectionSourceFactoryProvider(BeanFactory beanFactory) {
      this.beanFactory = beanFactory;
    }

    @Override
    public MultiConnectionSourceFactory get() throws BeansException {
      return (MultiConnectionSourceFactory)
          this.beanFactory.getBean(DefaultMultiConnectionSourceFactory.CONNECTION_SOURCE_FACTORY_BEAN_NAME);
    }
  }
}
