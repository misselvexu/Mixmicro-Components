package xyz.vopen.mixmicro.components.boot.httpclient.interceptor;

import okhttp3.Response;

import java.io.IOException;

/**
 * okhttp全局拦截器 使用抽象类，方便后面进行功能升级
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class BaseGlobalInterceptor implements PrototypeInterceptor {

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
