package xyz.vopen.mixmicro.components.boot.snowflake.test;

/**
 * com.acmedcare.framework.boot.snowflake.test
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 23/10/2018.
 */
import xyz.vopen.mixmicro.components.boot.snowflake.EnableSnowflake;
import xyz.vopen.mixmicro.components.boot.snowflake.Snowflake;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SnowflakeAutoConfigTestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@EnableSnowflake(workerId = "2")
public class SnowflakeAutoConfigTestApplication {

  @Resource private Snowflake snowflake;

  @Test
  public void nextId() {
    System.out.println(this.snowflake.nextId());
  }
}
