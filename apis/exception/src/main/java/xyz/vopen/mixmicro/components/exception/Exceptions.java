package xyz.vopen.mixmicro.components.exception;

import xyz.vopen.mixmicro.kits.io.UnsafeStringWriter;

import java.io.PrintWriter;

/**
 * {@link Exceptions}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
public final class Exceptions {

  // DEFAULT CONSTRUCTOR

  private Exceptions() {}

  /**
   * @param e exception instance
   * @return string
   */
  public static String toString(Throwable e) {
    UnsafeStringWriter w = new UnsafeStringWriter();
    PrintWriter p = new PrintWriter(w);
    p.print(e.getClass().getName());
    if (e.getMessage() != null) {
      p.print(": " + e.getMessage());
    }
    p.println();
    try {
      e.printStackTrace(p);
      return w.toString();
    } finally {
      p.close();
    }
  }

  /**
   * @param msg exception simple message
   * @param e exception instance
   * @return string
   */
  public static String toString(String msg, Throwable e) {
    UnsafeStringWriter w = new UnsafeStringWriter();
    w.write(msg + "\n");
    try (PrintWriter p = new PrintWriter(w)) {
      e.printStackTrace(p);
      return w.toString();
    }
  }
}
