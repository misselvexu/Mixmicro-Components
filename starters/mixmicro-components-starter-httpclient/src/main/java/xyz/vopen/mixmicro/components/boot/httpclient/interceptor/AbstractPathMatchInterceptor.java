package xyz.vopen.mixmicro.components.boot.httpclient.interceptor;

import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import xyz.vopen.mixmicro.components.boot.httpclient.PrototypeInterceptor;

import java.io.IOException;

public abstract class AbstractPathMatchInterceptor implements PrototypeInterceptor {

  private String[] include;

  private String[] exclude;

  private final PathMatcher pathMatcher = new AntPathMatcher();

  public void setInclude(String[] include) {
    this.include = include;
  }

  public void setExclude(String[] exclude) {
    this.exclude = exclude;
  }

  @Override
  public final Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    String path = request.url().encodedPath();

    if (isMatch(exclude, path)) {
      return chain.proceed(request);
    }

    if (!isMatch(include, path)) {
      return chain.proceed(request);
    }
    return doIntercept(chain);
  }

  /**
   * do intercept
   *
   * @param chain interceptor chain
   * @return http Response
   * @throws IOException IOException
   */
  protected abstract Response doIntercept(Chain chain) throws IOException;

  /**
   * Whether the current http URL path matches the specified patterns
   *
   * @param patterns the specified patterns
   * @param path http URL path
   * @return 匹配结果
   */
  private boolean isMatch(String[] patterns, String path) {
    if (patterns == null || patterns.length == 0) {
      return false;
    }
    for (String pattern : patterns) {
      boolean match = pathMatcher.match(pattern, path);
      if (match) {
        return true;
      }
    }
    return false;
  }
}
