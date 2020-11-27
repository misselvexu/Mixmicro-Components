package xyz.vopen.mixmicro.kits.task;

@FunctionalInterface
public interface ResultExecutor<T> extends Executor {

  T execute(Context ctx) throws Exception;
}
