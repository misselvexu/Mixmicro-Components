package xyz.vopen.mixmicro.kits.executor;

import xyz.vopen.mixmicro.kits.executor.RetriableThreadExecutor.ExecutorCallback;
import xyz.vopen.mixmicro.kits.executor.RetriableThreadExecutor.RetriableAttribute;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * xyz.vopen.mixmicro.kits.executor
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 27/11/2018.
 */
public class RetriableThreadExecutorTest {

  public static final int SUCCESS_FLAG = 3;

  @Test
  public void testRetriableThreadExecutor() throws InterruptedException {

    final CountDownLatch count = new CountDownLatch(1);
    RetriableThreadExecutor<String> executor =
        new RetriableThreadExecutor<>(
            "测试任务",
            new Callable<String>() {
              @Override
              public String call() throws Exception {
                Random r = new Random();
                int num = r.nextInt(10);
//                if (num == SUCCESS_FLAG) {
                  return num + "";
//                } else {
//                  throw new RuntimeException("模拟异常");
//                }
              }
            },
            new RetriableAttribute(10, 3000, TimeUnit.MILLISECONDS),
            new ExecutorCallback<String>() {
              @Override
              public void onCompleted(String result) {
                System.out.println("执行完成:" + result);
                count.countDown();
              }

              @Override
              public void onFailed(String message) {
                System.out.println("执行失败:" + message);
                count.countDown();
              }
            });

    executor.execute();
    count.await();
  }
}
