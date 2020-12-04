package xyz.vopen.mixmicro.components.boot.quartz.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * {@link QuartzTest}
 *
 * <p>Class QuartzTest Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/10/11
 */
public class QuartzTest {

  public static void main(String[] args) throws SchedulerException, InterruptedException {

    // 1、创建调度器Scheduler
    SchedulerFactory schedulerFactory = new StdSchedulerFactory("quartz.properties");
    Scheduler scheduler = schedulerFactory.getScheduler();

    // 2、创建JobDetail实例，并与PrintWordsJob类绑定(Job执行内容)
    JobDetail jobDetail = JobBuilder.newJob(PrintWordsJob.class).withIdentity("job1", "group1").build();

    // 3、构建Trigger实例,每隔1s执行一次
    Trigger trigger =
        TriggerBuilder.newTrigger()
            .withIdentity("trigger1", "triggerGroup1")
            .startNow() // 立即生效
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5) // 每隔1s执行一次
                    .repeatForever())
            .build(); // 一直执行

    // 4、执行
    scheduler.scheduleJob(jobDetail, trigger);
    System.out.println("--------scheduler start ! ------------");
    scheduler.start();

    // 睡眠
    TimeUnit.SECONDS.sleep(30);
    scheduler.shutdown();
    System.out.println("--------scheduler shutdown ! ------------");
  }

  public static class PrintWordsJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
      String printTime = new SimpleDateFormat("yy-MM-dd HH-mm-ss").format(new Date());
      System.out.println(
          "PrintWordsJob start at:"
              + printTime
              + ", prints: Hello Job-"
              + new Random().nextInt(100));
    }
  }
}
