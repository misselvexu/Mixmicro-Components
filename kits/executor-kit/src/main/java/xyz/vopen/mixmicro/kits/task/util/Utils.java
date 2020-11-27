package xyz.vopen.mixmicro.kits.task.util;

import xyz.vopen.mixmicro.kits.task.exception.TaskException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

public final class Utils {

  private static final String[] CHARS =
      new String[] {
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
        "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z"
      };

  private Utils() {}

  public static boolean isEmpty(String src) {
    return src == null || src.isEmpty();
  }

  public static boolean isNotEmpty(String src) {
    return !isEmpty(src);
  }

  public static String generateId() {
    StringBuilder result = new StringBuilder();
    String uuid = UUID.randomUUID().toString().replace("-", "");
    for (int i = 0; i < 8; i++) {
      String str = uuid.substring(i * 4, i * 4 + 4);
      int x = Integer.parseInt(str, 16);
      result.append(CHARS[x % 0x3E]);
    }
    return result.toString();
  }

  public static String getJavaVersion() {
    return System.getProperty("java.version");
  }

  public static String getOsInfo() {
    return System.getProperty("os.name") + "  " + System.getProperty("os.version");
  }

  public static byte[] aesEncode(String content, String password) {
    try {
      SecretKey key = getSecretKey(password);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new TaskException(e);
    }
  }

  public static byte[] aesDecode(byte[] content, String password) throws Exception {
    SecretKey key = getSecretKey(password);
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher.doFinal(content);
  }

  private static SecretKey getSecretKey(String password) throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance("AES");
    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    secureRandom.setSeed(password.getBytes());
    kg.init(128, secureRandom);
    SecretKey secretKey = kg.generateKey();
    byte[] keyBytes = secretKey.getEncoded();
    return new SecretKeySpec(keyBytes, "AES");
  }

  public static String readFromResource(String resource) throws IOException {
    InputStream in = null;
    InputStreamReader reader = null;
    try {
      in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
      if (in == null) {
        in = Utils.class.getResourceAsStream(resource);
      }

      if (in == null) {
        return null;
      }
      reader = new InputStreamReader(in, StandardCharsets.UTF_8);
      StringWriter sw = new StringWriter();
      char[] buffer = new char[1024 * 4];
      int len;
      while ((len = reader.read(buffer)) != -1) {
        sw.write(buffer, 0, len);
      }
      return sw.toString();
    } finally {
      if (reader != null) {
        reader.close();
      }
      if (in != null) {
        in.close();
      }
    }
  }
}
