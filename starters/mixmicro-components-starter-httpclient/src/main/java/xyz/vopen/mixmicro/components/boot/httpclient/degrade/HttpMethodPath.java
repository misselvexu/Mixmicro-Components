package xyz.vopen.mixmicro.components.boot.httpclient.degrade;

/**
 * {@link HttpMethodPath}
 *
 * <p>Class HttpMethodPath Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
public class HttpMethodPath {

  /** request method. such as GET, POST, PUT etc. */
  private final String method;

  /** request path */
  private final String path;

  public HttpMethodPath(String method, String path) {
    this.method = method;
    this.path = path;
  }

  public String getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }
}
