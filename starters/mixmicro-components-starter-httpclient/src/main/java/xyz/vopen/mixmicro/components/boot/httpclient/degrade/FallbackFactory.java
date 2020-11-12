package xyz.vopen.mixmicro.components.boot.httpclient.degrade;

/**
 * {@link FallbackFactory}
 *
 * <p>Class FallbackFactory Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
public interface FallbackFactory<T> {

  /**
   * Build an instance of the fallback appropriate for the given cause
   *
   * @param cause fallback cause
   * @return an instance that implements the client interface.
   */
  T build(Throwable cause);
}
