package xyz.vopen.mixmicro.kits.task.util;

public final class Assert {

  private Assert() {}

  public static void notNull(Object obj) {
    if (obj == null) {
      throw new IllegalArgumentException("parameter not null.");
    }
  }
}
