package xyz.vopen.mixmicro.kits.task;

@FunctionalInterface
public interface Callback {

  void call(Context ctx, Exception error);
}
