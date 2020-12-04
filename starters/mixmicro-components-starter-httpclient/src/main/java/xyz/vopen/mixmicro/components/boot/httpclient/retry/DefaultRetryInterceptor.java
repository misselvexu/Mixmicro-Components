package xyz.vopen.mixmicro.components.boot.httpclient.retry;

import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class DefaultRetryInterceptor extends AbstractRetryInterceptor {

  @Override
  protected Response retryIntercept(
      int maxRetries, int intervalMs, RetryRule[] retryRules, Chain chain)
      throws IOException, InterruptedException {
    while (true) {
      try {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (containRetryRule(retryRules, RetryRule.RETRY_WHEN_NON200CODE)) {
          if (response.isSuccessful()) {
            return response;
          }
          // 执行重试
          maxRetries--;
          if (maxRetries < 0) {
            // 最后一次还没成功，返回最后一次response
            return response;
          }
          response.close();
          Thread.sleep(intervalMs);
        } else {
          return response;
        }
      } catch (Exception e) {
        boolean judgeRetry = judgeRetry(retryRules, e);
        if (!judgeRetry) {
          throw e;
        }
        try {
          maxRetries--;
          if (maxRetries < 0) {
            // 最后一次还没成功，抛出最后一次访问的异常
            throw e;
          }
          Thread.sleep(intervalMs);
        } catch (InterruptedException e1) {
          throw new RuntimeException(e1);
        }
      }
    }
  }

  private boolean judgeRetry(RetryRule[] retryRules, Exception e) {
    if (containRetryRule(retryRules, RetryRule.RETRY_WHEN_ANY_EXCEPTION)) {
      return true;
    }
    if (containRetryRule(retryRules, RetryRule.RETRY_WHEN_IO_EXCEPTION)) {
      return e instanceof IOException;
    }
    return false;
  }

  private boolean containRetryRule(RetryRule[] retryRules, RetryRule retryRule) {
    for (RetryRule rule : retryRules) {
      if (rule.equals(retryRule)) {
        return true;
      }
    }
    return false;
  }
}
