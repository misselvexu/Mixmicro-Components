package xyz.vopen.mixmicro.components.boot.httpclient.degrade;

import org.springframework.core.env.Environment;
import retrofit2.http.*;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.MixHttpClient;
import xyz.vopen.mixmicro.components.boot.httpclient.kits.MixHttpClientKit;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link AbstractResourceNameParser}
 *
 * <p>Class AbstractResourceNameParser Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
public abstract class AbstractResourceNameParser {

  private static final Map<Method, String> RESOURCE_NAME_CACHE = new ConcurrentHashMap<>(128);

  public String parseResourceName(Method method, Environment environment) {
    String resourceName = RESOURCE_NAME_CACHE.get(method);
    if (resourceName != null) {
      return resourceName;
    }
    Class<?> declaringClass = method.getDeclaringClass();
    MixHttpClient retrofitClient = declaringClass.getAnnotation(MixHttpClient.class);
    String baseUrl = retrofitClient.baseUrl();
    baseUrl = MixHttpClientKit.convertBaseUrl(retrofitClient, baseUrl, environment);
    HttpMethodPath httpMethodPath = parseHttpMethodPath(method);
    resourceName = defineResourceName(baseUrl, httpMethodPath);
    RESOURCE_NAME_CACHE.put(method, resourceName);
    return resourceName;
  }

  /**
   * define resource name.
   *
   * @param baseUrl baseUrl
   * @param httpMethodPath httpMethodPath
   * @return resource name.
   */
  protected abstract String defineResourceName(String baseUrl, HttpMethodPath httpMethodPath);

  protected HttpMethodPath parseHttpMethodPath(Method method) {

    if (method.isAnnotationPresent(HTTP.class)) {
      HTTP http = method.getAnnotation(HTTP.class);
      return new HttpMethodPath(http.method(), http.path());
    }

    if (method.isAnnotationPresent(GET.class)) {
      GET get = method.getAnnotation(GET.class);
      return new HttpMethodPath("GET", get.value());
    }

    if (method.isAnnotationPresent(POST.class)) {
      POST post = method.getAnnotation(POST.class);
      return new HttpMethodPath("POST", post.value());
    }

    if (method.isAnnotationPresent(PUT.class)) {
      PUT put = method.getAnnotation(PUT.class);
      return new HttpMethodPath("PUT", put.value());
    }

    if (method.isAnnotationPresent(DELETE.class)) {
      DELETE delete = method.getAnnotation(DELETE.class);
      return new HttpMethodPath("DELETE", delete.value());
    }

    if (method.isAnnotationPresent(HEAD.class)) {
      HEAD head = method.getAnnotation(HEAD.class);
      return new HttpMethodPath("HEAD", head.value());
    }

    return null;
  }
}
