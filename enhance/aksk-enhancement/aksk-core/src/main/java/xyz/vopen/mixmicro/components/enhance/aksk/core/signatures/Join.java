package xyz.vopen.mixmicro.components.enhance.aksk.core.signatures;

import java.util.Collection;

public enum Join {
  ;

  public static String join(final String delimiter, final Collection collection) {
    if (collection.size() == 0) return "";

    final StringBuilder sb = new StringBuilder();

    for (final Object obj : collection) {
      sb.append(obj).append(delimiter);
    }

    return sb.substring(0, sb.length() - delimiter.length());
  }

  public static String join(final String delimiter, final Object... collection) {
    if (collection.length == 0) return "";

    final StringBuilder sb = new StringBuilder();

    for (final Object obj : collection) {
      sb.append(obj).append(delimiter);
    }

    return sb.substring(0, sb.length() - delimiter.length());
  }
}
