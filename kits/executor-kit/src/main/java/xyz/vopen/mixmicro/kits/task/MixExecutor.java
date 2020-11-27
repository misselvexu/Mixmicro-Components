package xyz.vopen.mixmicro.kits.task;

@FunctionalInterface
public interface MixExecutor extends Executor {

  void execute(Context ctx);
}
