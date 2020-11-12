package xyz.vopen.mixmicro.components.boot.httpclient.degrade;

/**
 * {@link DefaultResourceNameParser}
 *
 * <p>Class DefaultResourceNameParser Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
public class DefaultResourceNameParser extends AbstractResourceNameParser {

  private static volatile String PREFIX = "MIX_HTTP_OUT";

  /**
   * define resource name.
   *
   * @param baseUrl baseUrl
   * @param httpMethodPath httpMethodPath
   * @return resource name.
   */
  @Override
  protected String defineResourceName(String baseUrl, HttpMethodPath httpMethodPath) {
    return String.format("%s:%s:%s", PREFIX, httpMethodPath.getMethod(), baseUrl + httpMethodPath.getPath());
  }
}
