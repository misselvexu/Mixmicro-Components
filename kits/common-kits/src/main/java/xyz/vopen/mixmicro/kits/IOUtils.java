package xyz.vopen.mixmicro.kits;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * IO工具类
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public final class IOUtils {

  /**
   * 静默关闭
   *
   * @param closeable 可关闭的
   */
  public static void closeQuietly(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception ignore) {
        // NOPMD
      }
    }
  }

  /**
   * 静默关闭 for jdk6
   *
   * @param closeable 可关闭的
   */
  public static void closeQuietly(ServerSocket closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception ignore) {
        // NOPMD
      }
    }
  }

  /**
   * 静默关闭 for jdk6
   *
   * @param closeable 可关闭的
   */
  public static void closeQuietly(Socket closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception ignore) {
        // NOPMD
      }
    }
  }

  private static final int BUFFER_SIZE = 1024 * 8;
  public static final int EOF = -1;

  private IOUtils() {}

  /**
   * write.
   *
   * @param is InputStream instance.
   * @param os OutputStream instance.
   * @return count.
   * @throws IOException If an I/O error occurs
   */
  public static long write(InputStream is, OutputStream os) throws IOException {
    return write(is, os, BUFFER_SIZE);
  }

  /**
   * write.
   *
   * @param is InputStream instance.
   * @param os OutputStream instance.
   * @param bufferSize buffer size.
   * @return count.
   * @throws IOException If an I/O error occurs
   */
  public static long write(InputStream is, OutputStream os, int bufferSize) throws IOException {
    byte[] buff = new byte[bufferSize];
    return write(is, os, buff);
  }

  /**
   * write.
   *
   * @param input InputStream instance.
   * @param output OutputStream instance.
   * @param buffer buffer byte array
   * @return count.
   * @throws IOException If an I/O error occurs
   */
  public static long write(final InputStream input, final OutputStream output, final byte[] buffer)
      throws IOException {
    long count = 0;
    int n;
    while (EOF != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }

  /**
   * read string.
   *
   * @param reader Reader instance.
   * @return String.
   * @throws IOException If an I/O error occurs
   */
  public static String read(Reader reader) throws IOException {
    try (StringWriter writer = new StringWriter()) {
      write(reader, writer);
      return writer.getBuffer().toString();
    }
  }

  /**
   * write string.
   *
   * @param writer Writer instance.
   * @param string String.
   * @throws IOException If an I/O error occurs
   */
  public static long write(Writer writer, String string) throws IOException {
    try (Reader reader = new StringReader(string)) {
      return write(reader, writer);
    }
  }

  /**
   * write.
   *
   * @param reader Reader.
   * @param writer Writer.
   * @return count.
   * @throws IOException If an I/O error occurs
   */
  public static long write(Reader reader, Writer writer) throws IOException {
    return write(reader, writer, BUFFER_SIZE);
  }

  /**
   * write.
   *
   * @param reader Reader.
   * @param writer Writer.
   * @param bufferSize buffer size.
   * @return count.
   * @throws IOException If an I/O error occurs
   */
  public static long write(Reader reader, Writer writer, int bufferSize) throws IOException {
    int read;
    long total = 0;
    char[] buf = new char[bufferSize];
    while ((read = reader.read(buf)) != -1) {
      writer.write(buf, 0, read);
      total += read;
    }
    return total;
  }

  /**
   * read lines.
   *
   * @param file file.
   * @return lines.
   * @throws IOException If an I/O error occurs
   */
  public static String[] readLines(File file) throws IOException {
    if (file == null || !file.exists() || !file.canRead()) {
      return new String[0];
    }

    return readLines(new FileInputStream(file));
  }

  /**
   * read lines.
   *
   * @param is input stream.
   * @return lines.
   * @throws IOException If an I/O error occurs
   */
  public static String[] readLines(InputStream is) throws IOException {
    List<String> lines = new ArrayList<String>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
      return lines.toArray(new String[0]);
    }
  }

  /**
   * write lines.
   *
   * @param os output stream.
   * @param lines lines.
   * @throws IOException If an I/O error occurs
   */
  public static void writeLines(OutputStream os, String[] lines) throws IOException {
    try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(os))) {
      for (String line : lines) {
        writer.println(line);
      }
      writer.flush();
    }
  }

  /**
   * write lines.
   *
   * @param file file.
   * @param lines lines.
   * @throws IOException If an I/O error occurs
   */
  public static void writeLines(File file, String[] lines) throws IOException {
    if (file == null) {
      throw new IOException("File is null.");
    }
    writeLines(new FileOutputStream(file), lines);
  }

  /**
   * append lines.
   *
   * @param file file.
   * @param lines lines.
   * @throws IOException If an I/O error occurs
   */
  public static void appendLines(File file, String[] lines) throws IOException {
    if (file == null) {
      throw new IOException("File is null.");
    }
    writeLines(new FileOutputStream(file, true), lines);
  }
}
