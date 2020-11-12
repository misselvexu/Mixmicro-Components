package xyz.vopen.mixmicro.components.boot.httpclient.degrade.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import okhttp3.Request;
import okhttp3.Response;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.AbstractDegradeInterceptor;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.ClientBlockException;

import java.io.IOException;

/**
 * {@link SentinelDegradeInterceptor}
 *
 * <p>Class SentinelDegradeInterceptor Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
public class SentinelDegradeInterceptor extends AbstractDegradeInterceptor {

  /**
   * 熔断拦截处理
   *
   * @param chain 请求执行链
   * @return 请求响应
   * @throws ClientBlockException 如果触发熔断，抛出ClientBlockException
   */
  @Override
  protected Response degradeIntercept(String resourceName, Chain chain)
      throws ClientBlockException, IOException {
    Request request = chain.request();
    Entry entry = null;
    try {
      entry = SphU.entry(resourceName, ResourceTypeConstants.COMMON_WEB, EntryType.OUT);
      return chain.proceed(request);
    } catch (BlockException e) {
      throw new ClientBlockException(e);
    } finally {
      if (entry != null) {
        entry.exit();
      }
    }
  }
}
