package xyz.vopen.mixmicro.kits.queue.utils;

import java.util.regex.Pattern;
/**
 * {@link FolderNameValidator}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FolderNameValidator {

  private static final String ILLEGAL_CHARS =
      "/" + '\u0000' + '\u0001' + "-" + '\u001F' + '\u007F' + "-" + '\u009F' + '\uD800' + "-"
          + '\uF8FF' + '\uFFF0' + "-" + '\uFFFF';
  private static final Pattern p = Pattern.compile("(^\\.{1,2}$)|[" + ILLEGAL_CHARS + "]");

  public static void validate(String name) {
    if (name == null || name.length() == 0) {
      throw new IllegalArgumentException("folder name is emtpy");
    }
    if (name.length() > 255) {
      throw new IllegalArgumentException("folder name is too long");
    }
    if (p.matcher(name).find()) {
      throw new IllegalArgumentException("folder name [" + name + "] is illegal");
    }
  }
}
