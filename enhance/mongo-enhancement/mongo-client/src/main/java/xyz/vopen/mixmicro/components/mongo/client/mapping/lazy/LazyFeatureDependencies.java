package xyz.vopen.mixmicro.components.mongo.client.mapping.lazy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public final class LazyFeatureDependencies {

  private static final Logger LOG = LoggerFactory.getLogger(LazyFeatureDependencies.class);
  private static Boolean fulFilled;

  private LazyFeatureDependencies() {}

  /**
   * Checks that the dependencies to support lazy proxies are present
   *
   * @return true the dependencies are found
   */
  public static boolean assertDependencyFullFilled() {
    final boolean fulfilled = testDependencyFullFilled();
    if (!fulfilled) {
      LOG.warn("Lazy loading impossible due to missing dependencies.");
    }
    return fulfilled;
  }

  /**
   * Checks that the dependencies to support lazy proxies are present
   *
   * @return true the dependencies are found
   */
  public static boolean testDependencyFullFilled() {
    if (fulFilled != null) {
      return fulFilled;
    }
    try {
      fulFilled =
          Class.forName("net.sf.cglib.proxy.Enhancer") != null
              && Class.forName("com.thoughtworks.proxy.toys.hotswap.HotSwapping") != null;
    } catch (ClassNotFoundException e) {
      fulFilled = false;
    }
    return fulFilled;
  }

  /**
   * Creates a LazyProxyFactory
   *
   * @return the LazyProxyFactory
   */
  public static LazyProxyFactory createDefaultProxyFactory() {
    if (testDependencyFullFilled()) {
      final String factoryClassName = "CGLibLazyProxyFactory";
      try {
        return (LazyProxyFactory) Class.forName(factoryClassName).newInstance();
      } catch (Exception e) {
        LOG.error("While instantiating " + factoryClassName, e);
      }
    }
    return null;
  }
}
