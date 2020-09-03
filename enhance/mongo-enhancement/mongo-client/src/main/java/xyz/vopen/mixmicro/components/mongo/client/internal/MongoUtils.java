package xyz.vopen.mixmicro.components.mongo.client.internal;

import java.util.List;

/**
 * This class provides a set of utilities for use in Morphia. All these methods should be considered
 * internal only and should not be used in application code.
 */
public final class MongoUtils {
  private MongoUtils() {}

  /**
   * Joins strings with the given delimiter
   *
   * @param strings the strings to join
   * @param delimiter the delimiter
   * @return the joined string
   */
  public static String join(final List<String> strings, final char delimiter) {
    StringBuilder builder = new StringBuilder();
    for (String element : strings) {
      if (builder.length() != 0) {
        builder.append(delimiter);
      }
      builder.append(element);
    }
    return builder.toString();
  }
}
