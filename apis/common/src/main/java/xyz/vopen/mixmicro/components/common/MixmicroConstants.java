package xyz.vopen.mixmicro.components.common;

/**
 * {@link MixmicroConstants}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/4
 */
public class MixmicroConstants {

  /**
   * Default full date formatter pattern
   *
   * <p>
   */
  public static final String MIXMICRO_FULL_DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

  /**
   * Micro Service Invoke Flag
   *
   * <p>
   *
   * @deprecated use {@link #MIXMICRO_SERVICE_INVOKE_HEADER} instead of.
   */
  public static final String MIXMICRO_SERVICE_INVOK_HEADER = "mixmicro.service.invoke";

  /**
   * Micro Service Invoke Flag
   *
   * <p>
   */
  public static final String MIXMICRO_SERVICE_INVOKE_HEADER = "mixmicro.service.invoke";

  /**
   * Micro Ingress Invoke Flag
   *
   * <p>
   */
  public static final String MIXMICRO_INGRESS_INVOKE_HEADER = "mixmicro.ingress.invoke";
}
