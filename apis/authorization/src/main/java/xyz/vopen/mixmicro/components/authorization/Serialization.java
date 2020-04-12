package xyz.vopen.mixmicro.components.authorization;

import com.google.gson.*;
import com.google.gson.internal.Primitives;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.authorization.exception.AuthorizationLoaderException;
import xyz.vopen.mixmicro.kits.lang.Nullable;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import static xyz.vopen.mixmicro.components.authorization.Constants.AUTHORIZATION_LOG_NAME;
import static xyz.vopen.mixmicro.components.authorization.Constants.DATE_FORMAT_PATTEN;

/**
 * {@link Serialization}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
@SuppressWarnings({"unchecked", "unused"})
public final class Serialization {

  private static final Logger log = LoggerFactory.getLogger(AUTHORIZATION_LOG_NAME);

  // DEFAULT CONSTRUCTOR

  private Serialization() {}

  private static Gson gson;

  static {
    GsonBuilder builder = new GsonBuilder();

    builder
        .setDateFormat(DATE_FORMAT_PATTEN)
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .setExclusionStrategies();

    gson = builder.create();
  }

  /**
   * This method serializes the specified object into its equivalent Json representation. This
   * method should be used when the specified object is not a generic type. This method uses {@link
   * Class#getClass()} to get the type for the specified object, but the {@code getClass()} loses
   * the generic type information because of the Type Erasure feature of Java. Note that this method
   * works fine if the any of the object fields are of generic type, just the object itself should
   * not be of a generic type. If the object is of generic type, use {@link Gson#toJson(Object,
   * Type)} instead. If you want to write out the object to a {@link Writer}, use {@link
   * Gson#toJson(Object, Appendable)} instead.
   *
   * @param src the object for which Json representation is to be created setting for Gson
   * @return Json representation of {@code src}.
   */
  public static String toJsonString(Object src) {
    if (src == null) {
      return gson.toJson(JsonNull.INSTANCE);
    }
    return gson.toJson(src, src.getClass());
  }

  public static byte[] toJsonByte(Object src) {

    if (src == null) {
      return toByteArray(gson.toJson(JsonNull.INSTANCE));
    }
    return toByteArray(gson.toJson(src, src.getClass()));
  }

  /**
   * This method deserializes the specified Json into an object of the specified class. It is not
   * suitable to use if the specified class is a generic type since it will not have the generic
   * type information because of the Type Erasure feature of Java. Therefore, this method should not
   * be used if the desired type is a generic type. Note that this method works fine if the any of
   * the fields of the specified object are generics, just the object itself should not be a generic
   * type. For the cases when the object is of generic type, invoke {@link #fromJson(String, Type)}.
   * If you have the Json in a {@link Reader} instead of a String, use {@link Gson#fromJson(Reader,
   * Class)} instead.
   *
   * @param <T> the type of the desired object
   * @param json the string from which the object is to be de-serialized
   * @param classOfT the class of T
   * @return an object of type T from the string. Returns {@code null} if {@code json} is {@code
   *     null} or if {@code json} is empty.
   * @throws JsonSyntaxException if json is not a valid representation for an object of type
   *     classOfT
   */
  public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
    Object object = fromJson(json, (Type) classOfT);
    return Primitives.wrap(classOfT).cast(object);
  }

  public static <T> T fromJson(byte[] jsonBytes, Class<T> classOfT) throws JsonSyntaxException {
    Object object = fromJson(fromByteArray(jsonBytes), (Type) classOfT);
    return Primitives.wrap(classOfT).cast(object);
  }

  /**
   * This method deserializes the specified Json into an object of the specified type. This method
   * is useful if the specified object is a generic type. For non-generic objects, use {@link
   * #fromJson(String, Class)} instead. If you have the Json in a {@link Reader} instead of a
   * String, use {@link Gson#fromJson(Reader, Type)} instead.
   *
   * @param <T> the type of the desired object
   * @param json the string from which the object is to be deserialized
   * @param typeOfT The specific genericized type of src. You can obtain this type by using the
   *     {@link com.google.gson.reflect.TypeToken} class. For example, to get the type for {@code
   *     Collection<Foo>}, you should use:
   *     <pre>
   * Type typeOfT = new TypeToken&lt;Collection&lt;Foo&gt;&gt;(){}.getType();
   * </pre>
   *
   * @return an object of type T from the string. Returns {@code null} if {@code json} is {@code
   *     null} or if {@code json} is empty.
   * @throws JsonParseException if json is not a valid representation for an object of type typeOfT
   * @throws JsonSyntaxException if json is not a valid representation for an object of type
   */
  public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
    if (json == null) {
      return null;
    }
    StringReader reader = new StringReader(json);
    T target = (T) gson.fromJson(reader, typeOfT);
    return target;
  }

  public static <T> T fromJson(byte[] jsonBytes, Type typeOfT) throws JsonSyntaxException {
    if (jsonBytes == null || jsonBytes.length == 0) {
      return null;
    }
    StringReader reader = new StringReader(fromByteArray(jsonBytes));
    T target = (T) gson.fromJson(reader, typeOfT);
    return target;
  }

  // String & Byte Array Concert Methods

  public static byte[] toByteArray(String string) {
    byte[] bytes = new byte[string.length()];
    for (int i = 0; i != bytes.length; i++) {
      char ch = string.charAt(i);
      bytes[i] = (byte) ch;
    }
    return bytes;
  }

  public static String fromByteArray(byte[] bytes) {
    int i = 0;
    int length = 0;

    while (i < bytes.length) {
      length++;
      if ((bytes[i] & 0xf0) == 0xf0) {
        // surrogate pair
        length++;
        i += 4;
      } else if ((bytes[i] & 0xe0) == 0xe0) {
        i += 3;
      } else if ((bytes[i] & 0xc0) == 0xc0) {
        i += 2;
      } else {
        i += 1;
      }
    }

    char[] cs = new char[length];

    i = 0;
    length = 0;

    while (i < bytes.length) {
      char ch;

      if ((bytes[i] & 0xf0) == 0xf0) {
        int codePoint =
            ((bytes[i] & 0x03) << 18)
                | ((bytes[i + 1] & 0x3F) << 12)
                | ((bytes[i + 2] & 0x3F) << 6)
                | (bytes[i + 3] & 0x3F);
        int U = codePoint - 0x10000;
        char W1 = (char) (0xD800 | (U >> 10));
        char W2 = (char) (0xDC00 | (U & 0x3FF));
        cs[length++] = W1;
        ch = W2;
        i += 4;
      } else if ((bytes[i] & 0xe0) == 0xe0) {
        ch =
            (char)
                (((bytes[i] & 0x0f) << 12) | ((bytes[i + 1] & 0x3f) << 6) | (bytes[i + 2] & 0x3f));
        i += 3;
      } else if ((bytes[i] & 0xd0) == 0xd0) {
        ch = (char) (((bytes[i] & 0x1f) << 6) | (bytes[i + 1] & 0x3f));
        i += 2;
      } else if ((bytes[i] & 0xc0) == 0xc0) {
        ch = (char) (((bytes[i] & 0x1f) << 6) | (bytes[i + 1] & 0x3f));
        i += 2;
      } else {
        ch = (char) (bytes[i] & 0xff);
        i += 1;
      }

      cs[length++] = ch;
    }

    return new String(cs);
  }

  public static class AuthorizationKeyLoader {

    private final ClassLoader classLoader;

    public AuthorizationKeyLoader() {
      this(null);
    }

    public AuthorizationKeyLoader(@Nullable ClassLoader classLoader) {
      if (classLoader == null) {
        this.classLoader = AuthorizationKeyLoader.class.getClassLoader();
      } else {
        this.classLoader = classLoader;
      }
    }

    @Nullable
    public <K> K loadKey(String resource, Class<? extends RSAKey> keyClazz) {
      if (keyClazz == RSAPrivateKey.class) {
        return (K) loadPrivateKey(resource);
      }

      if (keyClazz == RSAPublicKey.class) {
        return (K) loadPublicKey(resource);
      }

      return null;
    }

    public RSAPrivateKey loadPrivateKey(String resource) {

      try {
        InputStream stream = classLoader.getResourceAsStream(resource);
        if (stream != null) {
          String content = new String(IOUtils.toByteArray(stream), StandardCharsets.UTF_8);
          return Cipher.loadPrivateKey(content);
        }
      } catch (Exception e) {
        throw new AuthorizationLoaderException("Target key file " + resource + " load failed.", e);
      }
      throw new AuthorizationLoaderException("Target key file [" + resource + "] is invalid.");
    }

    public RSAPublicKey loadPublicKey(String resource) {

      try {
        InputStream stream = classLoader.getResourceAsStream(resource);
        if (stream != null) {
          String content = new String(IOUtils.toByteArray(stream), StandardCharsets.UTF_8);
          return Cipher.loadPublicKey(content);
        }
      } catch (Exception e) {
        throw new AuthorizationLoaderException("Target key file " + resource + " load failed.", e);
      }
      throw new AuthorizationLoaderException("Target key file [" + resource + "] is invalid.");
    }
  }

  /**
   * {@link Cipher}
   *
   * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
   * @version ${project.version} - 2020-04-10.
   */
  public static class Cipher {

    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public Cipher(String publicKey, String privateKey) {
      try {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(org.apache.commons.codec.binary.Base64.decodeBase64(publicKey));
        this.publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(org.apache.commons.codec.binary.Base64.decodeBase64(privateKey));
        this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
      } catch (Exception e) {
        throw new RuntimeException("un-support algorithm key", e);
      }
    }

    public Cipher(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
      this.publicKey = publicKey;
      this.privateKey = privateKey;
    }

    public static Map<String, String> createKeys(int keySize) {
      KeyPairGenerator kpg;
      try {
        kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
      } catch (NoSuchAlgorithmException e) {
        throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
      }

      kpg.initialize(keySize);
      KeyPair keyPair = kpg.generateKeyPair();
      Key publicKey = keyPair.getPublic();
      String publicKeyStr = org.apache.commons.codec.binary.Base64.encodeBase64String(publicKey.getEncoded());
      Key privateKey = keyPair.getPrivate();
      String privateKeyStr = org.apache.commons.codec.binary.Base64.encodeBase64String(privateKey.getEncoded());
      Map<String, String> keyPairMap = new HashMap<>(2);
      keyPairMap.put("publicKey", publicKeyStr);
      keyPairMap.put("privateKey", privateKeyStr);

      return keyPairMap;
    }

    public String publicEncrypt(String data) {
      try {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, publicKey);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(
            rsaSplitCodec(
                cipher,
                javax.crypto.Cipher.ENCRYPT_MODE,
                data.getBytes(StandardCharsets.UTF_8),
                publicKey.getModulus().bitLength()));
      } catch (Exception e) {
        throw new RuntimeException("RSA Encrypt   [" + data + "] happened exception", e);
      }
    }

    public String privateDecrypt(String data) {
      try {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, privateKey);
        return new String(
            rsaSplitCodec(
                cipher,
                javax.crypto.Cipher.DECRYPT_MODE,
                org.apache.commons.codec.binary.Base64.decodeBase64(data),
                publicKey.getModulus().bitLength()),
            StandardCharsets.UTF_8);
      } catch (Exception e) {
        throw new RuntimeException("RSA Decrypt  [" + data + "] happened exception", e);
      }
    }

    public String privateEncrypt(String data) {
      try {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, privateKey);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(
            rsaSplitCodec(
                cipher,
                javax.crypto.Cipher.ENCRYPT_MODE,
                data.getBytes(StandardCharsets.UTF_8),
                publicKey.getModulus().bitLength()));
      } catch (Exception e) {
        throw new RuntimeException("RSA Encrypt   [" + data + "] happened exception", e);
      }
    }

    public String publicDecrypt(String data) {
      try {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, publicKey);
        return new String(
            rsaSplitCodec(
                cipher,
                javax.crypto.Cipher.DECRYPT_MODE,
                org.apache.commons.codec.binary.Base64.decodeBase64(data),
                publicKey.getModulus().bitLength()),
            StandardCharsets.UTF_8);
      } catch (Exception e) {
        throw new RuntimeException("RSA Decrypt  [" + data + "] happened exception", e);
      }
    }

    public String sign(String data) {
      try {
        // sign
        Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return org.apache.commons.codec.binary.Base64.encodeBase64String(signature.sign());
      } catch (Exception e) {
        throw new RuntimeException("RSA sign [" + data + "] happened exception", e);
      }
    }

    public boolean verify(String data, String sign) {
      try {
        Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(org.apache.commons.codec.binary.Base64.decodeBase64(sign));
      } catch (Exception e) {
        throw new RuntimeException("RSA verify sign [" + data + "] happened exception", e);
      }
    }

    private static byte[] rsaSplitCodec(
        javax.crypto.Cipher cipher, int opmode, byte[] datas, int keySize) {
      int maxBlock = 0;
      if (opmode == javax.crypto.Cipher.DECRYPT_MODE) {
        maxBlock = keySize / 8;
      } else {
        maxBlock = keySize / 8 - 11;
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int offSet = 0;
      byte[] buff;
      int i = 0;
      try {
        while (datas.length > offSet) {
          if (datas.length - offSet > maxBlock) {
            buff = cipher.doFinal(datas, offSet, maxBlock);
          } else {
            buff = cipher.doFinal(datas, offSet, datas.length - offSet);
          }
          out.write(buff, 0, buff.length);
          i++;
          offSet = i * maxBlock;
        }
      } catch (Exception e) {
        throw new RuntimeException("RSA split codec [" + maxBlock + "] happened exception ", e);
      }
      byte[] resultDatas = out.toByteArray();
      IOUtils.closeQuietly(out);
      return resultDatas;
    }

    public static RSAPublicKey getPublicKey(String publicKey) {
      try {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(org.apache.commons.codec.binary.Base64.decodeBase64(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(spec);
      } catch (Exception e) {
        throw new RuntimeException("un-support algorithm key", e);
      }
    }

    public static RSAPrivateKey getPrivateKey(String privateKey) {
      try {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(org.apache.commons.codec.binary.Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
      } catch (Exception e) {
        throw new RuntimeException("un-support algorithm key", e);
      }
    }

    /**
     * Load Private Key from PEM File
     *
     * @param privateKeyPemFile pem file instance
     * @return private key
     * @throws Exception exception
     */
    public static RSAPrivateKey loadPrivateKey(File privateKeyPemFile) throws Exception {
      String privateKeyPEM = FileUtils.readFileToString(privateKeyPemFile, StandardCharsets.UTF_8);

      // strip of header, footer, newlines, whitespaces
      privateKeyPEM =
          privateKeyPEM
              .replaceAll("-----.+?PRIVATE.+?-----", "")
              .replaceAll("-----.+?PRIVATE.+?-----", "")
              .replaceAll("\\s", "");

      return getPrivateKey(privateKeyPEM);
    }

    public static RSAPublicKey loadPublicKey(File publicKeyPemFile) throws Exception {
      String publicKeyPEM = FileUtils.readFileToString(publicKeyPemFile, StandardCharsets.UTF_8);

      // strip of header, footer, newlines, whitespaces
      publicKeyPEM =
          publicKeyPEM
              .replaceAll("-----.+?PUBLIC.+?-----", "")
              .replaceAll("-----.+?PUBLIC.+?-----", "")
              .replaceAll("\\s", "");
      return getPublicKey(publicKeyPEM);
    }

    /**
     * Load Private Key from PEM File
     *
     * @param privateKeyPEM pem file instance
     * @return private key
     * @throws Exception exception
     */
    public static RSAPrivateKey loadPrivateKey(String privateKeyPEM) throws Exception {

      // strip of header, footer, newlines, whitespaces
      privateKeyPEM =
          privateKeyPEM
              .replaceAll("-----.+?PRIVATE.+?-----", "")
              .replaceAll("-----.+?PRIVATE.+?-----", "")
              .replaceAll("\\s", "");

      return getPrivateKey(privateKeyPEM);
    }

    public static RSAPublicKey loadPublicKey(String publicKeyPEM) throws Exception {

      // strip of header, footer, newlines, whitespaces
      publicKeyPEM =
          publicKeyPEM
              .replaceAll("-----.+?PUBLIC.+?-----", "")
              .replaceAll("-----.+?PUBLIC.+?-----", "")
              .replaceAll("\\s", "");
      return getPublicKey(publicKeyPEM);
    }
  }
}
