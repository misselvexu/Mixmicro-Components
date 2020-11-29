package xyz.vopen.mixmicro.kits.task;

import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * {@link TaskTester}
 *
 * <p>Class TaskTester Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/28
 */
public class TaskTester {

  @Test
  public void testTaskEngine() throws Exception {

    TaskEngine engine =
        new TaskEngine.Builder()
            .corePoolSize(8)
            .maxPoolSize(100)
            .keepAliveSeconds(1000)
            .queueCapacity(64)
            .rejectedPolicy(new ThreadPoolExecutor.CallerRunsPolicy())
            .completedHandler(
                task -> {
                  // ....
                  System.out.println(" ==== Engine Task Completed Handler ==== ");
                  System.out.println(" Task Id: " + task.getId() + ", Status : " + task.getState());
                })
            .build();

    // simple task
    Task simpleTask =
        engine
            .newTaskBuilder(
                new SimpleExecutor() {
                  @Override
                  public void execute(Context ctx) {
                    // ...
                    System.out.println("simple demo task ...");
                  }
                })
            .afterExecuted(
                new Callback() {
                  @Override
                  public void call(Context ctx, Exception error) {
                    // ...
                    System.out.println("simple demo task is executed ~");
                  }
                })
            .type("simple-demo-type")
            .build();

    engine.execute(simpleTask);
    simpleTask.await();

    // ~~
    System.out.println("###");

    ResultTask<Object> simpleWithResultTask =
        engine
            .newResultTaskBuilder(
                new ResultableExecutor<Object>() {
                  @Override
                  public Object execute(Context ctx) throws Exception {

                    System.out.println("resultable demo task ~");

                    return "result";
                  }
                })
            .type("result-demo-type")
            .build();


    engine.execute(simpleWithResultTask);
    simpleWithResultTask.await();

    String result = simpleWithResultTask.get().toString();

    System.out.println("result demo task execute result: " + result);

    // ~~
    System.out.println("##");

    TaskGroup group = engine.prepareGroup("demo-group");



    // ~~
    System.out.println("task engine status ...");
    System.out.println("[Total Task Size] : " + engine.getTotalNumberOfTask());
    System.out.println("[Completed Task Size] : " + engine.getCompletedNumberOfTask());
    System.out.println("[Running Task Size] : " + engine.getRunningNumberOfTask());
    System.out.println("[Group Task Size] : " + engine.getRunningTaskGroups().size());

    System.out.println("###");

    System.out.println("ready to shutdown task engine .");
    engine.shutdown();
  }
}
