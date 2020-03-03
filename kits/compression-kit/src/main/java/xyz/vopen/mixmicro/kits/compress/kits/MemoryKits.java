package xyz.vopen.mixmicro.kits.compress.kits;

/**
 * Memory Kits
 *
 * @author Elve.Xu
 */
public class MemoryKits {

  static long getMaxMemory() {
    return Runtime.getRuntime().maxMemory();
  }

  static long getTotalMemory() {
    return Runtime.getRuntime().totalMemory();
  }

  static long getFreeMemory() {
    return Runtime.getRuntime().freeMemory();
  }

  static long getUsedMemory() {
    return (getMaxMemory() - getFreeMemory());
  }
}
