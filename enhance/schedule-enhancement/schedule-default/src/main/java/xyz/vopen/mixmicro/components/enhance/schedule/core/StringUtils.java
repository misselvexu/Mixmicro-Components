package xyz.vopen.mixmicro.components.enhance.schedule.core;

public class StringUtils {

  public static String truncate(String s, int length) {
    if (s == null) {
      return null;
    }

    if (s.length() > length) {
      return s.substring(0, length);
    } else {
      return s;
    }
  }
}
