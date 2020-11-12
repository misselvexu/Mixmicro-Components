package xyz.vopen.mixmicro.components.boot.httpclient.degrade;

/**
 * {@link DegradeStrategy}
 *
 * <p>Class DegradeStrategy Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
public enum DegradeStrategy {

  /** average RT */
  AVERAGE_RT,

  /** exception ratio */
  EXCEPTION_RATIO,
}
