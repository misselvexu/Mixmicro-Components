package xyz.vopen.mixmicro.kits.queue.utils;

/**
 * {@link Calculator}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class Calculator {

  /**
   * mod by shift
   *
   * @param val
   * @param bits
   * @return
   */
  public static long mod(long val, int bits) {
    return val - ((val >> bits) << bits);
  }

  /**
   * multiply by shift
   *
   * @param val
   * @param bits
   * @return
   */
  public static long mul(long val, int bits) {
    return val << bits;
  }

  /**
   * divide by shift
   *
   * @param val
   * @param bits
   * @return
   */
  public static long div(long val, int bits) {
    return val >> bits;
  }
}
