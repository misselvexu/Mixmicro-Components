package xyz.vopen.mixmicro.kits.task;

@FunctionalInterface
public interface Progress {

  void call(int progress);
}
