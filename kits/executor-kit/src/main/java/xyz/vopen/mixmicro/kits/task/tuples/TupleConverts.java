package xyz.vopen.mixmicro.kits.task.tuples;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * {@link TupleConverts}
 *
 * <p>Class TupleConverts Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
public final class TupleConverts {

  public static Boolean asBoolean(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    try {
      return Boolean.valueOf(value.toString());
    } catch (Exception e) {
      throw new TupleException("cannot convert to Boolean", e);
    }
  }

  public static Character asCharacter(Object value) {
    if (value == null) {
      return null;
    }
    String str = value.toString();
    if (str.length() == 1) {
      return str.charAt(0);
    }
    throw new TupleException("cannot convert to Character");
  }

  public static Byte asByte(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Byte) {
      return (Byte) value;
    }
    try {
      Number number = asNumber(value, Byte.class);
      return number.byteValue();
    } catch (Exception e) {
      throw new TupleException("cannot convert to Byte.", e);
    }
  }

  public static Short asShort(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Short) {
      return (Short) value;
    }
    try {
      Number number = asNumber(value, Short.class);
      return number.shortValue();
    } catch (Exception e) {
      throw new TupleException("cannot convert to Short.", e);
    }
  }

  public static Integer asInteger(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Integer) {
      return (Integer) value;
    }
    try {
      return asNumber(value, Integer.class).intValue();
    } catch (Exception e) {
      throw new TupleException("cannot convert to Integer.", e);
    }
  }

  public static Long asLong(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Long) {
      return (Long) value;
    }
    try {
      return asNumber(value, Integer.class).longValue();
    } catch (Exception e) {
      throw new TupleException("cannot convert to Integer.", e);
    }
  }

  public static Float asFloat(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Float) {
      return (Float) value;
    }
    try {
      return asNumber(value, Integer.class).floatValue();
    } catch (Exception e) {
      throw new TupleException("cannot convert to Float.", e);
    }
  }

  public static Double asDouble(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Double) {
      return (Double) value;
    }
    try {
      return asNumber(value, Integer.class).doubleValue();
    } catch (Exception e) {
      throw new TupleException("cannot convert to Double.", e);
    }
  }

  public static BigDecimal asBigDecimal(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    }
    try {
      return new BigDecimal(asNumber(value, BigDecimal.class).doubleValue());
    } catch (Exception e) {
      throw new TupleException("cannot convert to BigDecimal.", e);
    }
  }

  public static String asString(Object value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }

  public static byte[] asBytes(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Number) {
      return new byte[] {((Number) value).byteValue()};
    }
    if (value instanceof byte[]) {
      return (byte[]) value;
    }
    if (value instanceof Byte[]) {
      Byte[] bytes = (Byte[]) value;
      byte[] result = new byte[bytes.length];
      for (int i = 0, len = bytes.length; i < len; i++) {
        result[i] = bytes[i];
      }
      return result;
    }
    if (value instanceof String) {
      return ((String) value).getBytes(StandardCharsets.UTF_8);
    }
    if (value instanceof InputStream) {
      byte[] result;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      InputStream in = (InputStream) value;
      copy(baos, in);
      result = baos.toByteArray();
      try {
        baos.close();
      } catch (IOException ignore) {
      }
      return result;
    }
    throw new TupleException("cannot convert to byte[].");
  }

  private static Number asNumber(final Object value, final Class<? extends Number> clazz)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    if (value instanceof Number) {
      return (Number) value;
    }
    final String str = value.toString();
    if (str.startsWith("0x") || str.startsWith("0X")) {
      return new BigInteger(str.substring("0x".length()), 16);
    }
    if (str.startsWith("0b") || str.startsWith("0B")) {
      return new BigInteger(str.substring("0b".length()), 2);
    }
    Constructor<?> constructor = clazz.getConstructor(String.class);
    return (Number) constructor.newInstance(str);
  }

  private static void copy(OutputStream out, InputStream in) {
    byte[] buffer = new byte[1024];
    int len;
    try {
      while ((len = in.read(buffer)) != -1) {
        out.write(buffer, 0, len);
      }
      out.flush();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException ignore) {
        }
      }
    }
  }
}
