package xyz.vopen.mixmicro.components.boot.snowflake.test;

import xyz.vopen.mixmicro.components.boot.snowflake.Snowflake;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;

/**
 * SnowflakeTest
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-29.
 */
public class SnowflakeTest {

  @Test
  public void main() throws InterruptedException {
    Set<Long> keys = Sets.newConcurrentHashSet();

    CountDownLatch countDownLatch = new CountDownLatch(2);
    new Thread(
            new Runnable() {
              @Override
              public void run() {
                Snowflake snowflake = new Snowflake(1);
                try {
                  for (int i = 0; i < 1000000; i++) {
                    if (!keys.add(snowflake.nextId())) {
                      throw new RuntimeException();
                    }
                  }
                } finally {
                  countDownLatch.countDown();
                }
              }
            })
        .start();

    new Thread(
            new Runnable() {
              @Override
              public void run() {
                Snowflake snowflake = new Snowflake(2);
                try {
                  for (int i = 0; i < 1000000; i++) {
                    if (!keys.add(snowflake.nextId())) {
                      throw new RuntimeException();
                    }
                  }
                } finally {
                  countDownLatch.countDown();
                }
              }
            })
        .start();

    countDownLatch.await();
    System.out.println(keys.size());
  }
}
