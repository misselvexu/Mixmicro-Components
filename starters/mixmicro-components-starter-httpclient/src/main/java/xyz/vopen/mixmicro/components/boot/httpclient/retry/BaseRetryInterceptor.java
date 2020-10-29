package xyz.vopen.mixmicro.components.boot.httpclient.retry;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Invocation;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 请求重试拦截器 Request retry interceptor
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class BaseRetryInterceptor implements Interceptor {

  private static final int LIMIT_RETRIES = 10;

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Invocation invocation = request.tag(Invocation.class);
    assert invocation != null;
    Method method = invocation.method();
    Retry retry;
    if (method.isAnnotationPresent(Retry.class)) {
      retry = method.getAnnotation(Retry.class);
    } else {
      Class<?> declaringClass = method.getDeclaringClass();
      retry = declaringClass.getAnnotation(Retry.class);
    }
    if (retry == null) {
      // 不用重试
      return chain.proceed(request);
    }
    // 重试
    int maxRetries = retry.maxRetries();
    int intervalMs = retry.intervalMs();
    RetryRule[] retryRules = retry.retryRules();
    // 最多重试10次
    maxRetries = Math.min(maxRetries, LIMIT_RETRIES);
    try {
      return retryIntercept(maxRetries, intervalMs, retryRules, chain);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * process a retryable request The access level here is set to protected, which can facilitate
   * business personalized expansion
   *
   * @param maxRetries Maximum number of retries
   * @param intervalMs Retry interval
   * @param retryRules Retry rules
   * @param chain Execution chain
   * @return Response
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  protected abstract Response retryIntercept(
      int maxRetries, int intervalMs, RetryRule[] retryRules, Chain chain)
      throws IOException, InterruptedException;
}
