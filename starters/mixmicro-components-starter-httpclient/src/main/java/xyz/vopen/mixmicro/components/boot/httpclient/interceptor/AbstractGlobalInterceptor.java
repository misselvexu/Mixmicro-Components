package xyz.vopen.mixmicro.components.boot.httpclient.interceptor;

import okhttp3.Response;
import xyz.vopen.mixmicro.components.boot.httpclient.PrototypeInterceptor;

import java.io.IOException;

public abstract class AbstractGlobalInterceptor implements PrototypeInterceptor {

  @Override
  public final Response intercept(Chain chain) throws IOException {
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
}
