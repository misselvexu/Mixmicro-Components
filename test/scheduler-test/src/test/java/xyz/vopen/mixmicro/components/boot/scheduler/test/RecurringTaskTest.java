package xyz.vopen.mixmicro.components.boot.scheduler.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.ExecutionContext;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.TaskInstance;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.VoidExecutionHandler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper.RecurringTask;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper.Tasks;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule.FixedDelay;

import javax.sql.DataSource;
import java.io.Serializable;

/**
 * {@link RecurringTaskTest}
 *
 * <p>Class RecurringTaskTest Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/10/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchedulerTester.class)
public class RecurringTaskTest {

  @Autowired private DataSource dataSource;

  @Test
  public void testRecurringTask01() throws Exception {

    RecurringTask<RecurringTaskParam> secTask =
        Tasks.recurring("my-recurring-sec-task", FixedDelay.ofSeconds(5), RecurringTaskParam.class)
            .initialData(new RecurringTaskParam("1", "Elve.Xu"))
            .execute(
                new VoidExecutionHandler<RecurringTaskParam>() {
                  @Override
                  public void execute(
                      TaskInstance<RecurringTaskParam> instance,
                      ExecutionContext context) {

                    RecurringTaskParam param = instance.getData();
                    System.out.printf("execute : %s - %s \r\n", param.id, param.name);
                  }
                });

    final Scheduler scheduler = Scheduler.create(dataSource).startTasks(secTask).threads(5).build();

    scheduler.start();

    Thread.currentThread().join();
  }

  private static class RecurringTaskParam implements Serializable {

    public String id;
    public String name;

    public RecurringTaskParam(String id, String name) {
      this.id = id;
      this.name = name;
    }
  }
}
