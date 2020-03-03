/*
 * Copyright 1999-2018 Mixmicro+ Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.vopen.mixmicro.kits.tink;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * RSA Utils
 *
 * @author Elve.Xu [iskp.me<at>gmail.com]
 * @version v1.2 - 2018/5/16.
 */
@SuppressWarnings({"Duplicates", "AlibabaClassNamingShouldBeCamel"})
@UtilityClass
public final class RSAKits {

  public static final String CHAR_ENCODING = "UTF-8";
  private static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

  /** 字节数据转字符串专用集合 */
  private static final char[] HEX_CHAR = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  private static final int MAX_DECRYPT_BLOCK = 128;
  private static final int MAX_ENCRYPT_BLOCK = 117;

  /** 这个值关系到块加密的大小,可以更改,但是不要太大,否则效率会降低 */
  private static final int DEFAULT_KEY_SIZE = 1024;

  private static int keySize = DEFAULT_KEY_SIZE;

  private static String RSAKeyStore = "/tmp/RSAKey.txt";
  private static BouncyCastleProvider provider = new BouncyCastleProvider();

  /** 生成密钥对 */
  public static KeyPair generateKeyPair() throws Exception {
    return generateKeyPair(RSAKeyStore);
  }

  /**
   * Gen-KeyPairs
   *
   * @param keySize key size (default: 1024)
   * @return key pair
   * @throws Exception exception
   */
  public static KeyPair generateKeyPair(int keySize) throws Exception {
    if (keySize > 0) {
      RSAKits.keySize = keySize;
    }
    return generateKeyPair(RSAKeyStore);
  }

  /**
   * 生成密钥对
   *
   * @param rsaKeyStore 密钥文件地址
   */
  private static KeyPair generateKeyPair(String rsaKeyStore) throws Exception {
    try {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());

      keyPairGen.initialize(keySize, new SecureRandom());

      KeyPair keyPair = keyPairGen.generateKeyPair();

      System.err.println(keyPair.getPrivate());
      System.err.println(keyPair.getPublic());

      saveKeyPair(keyPair, rsaKeyStore);

      return keyPair;

    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /** 得到密钥 */
  public static KeyPair getKeyPair() throws Exception {
    return getKeyPair(RSAKeyStore);
  }

  /** 得到密钥 */
  private static KeyPair getKeyPair(String rsaKeyStore) throws Exception {
    FileInputStream fis = new FileInputStream(rsaKeyStore);
    ObjectInputStream ois = new ObjectInputStream(fis);
    KeyPair keyPair = (KeyPair) ois.readObject();
    ois.close();
    fis.close();

    return keyPair;
  }

  private static void saveKeyPair(KeyPair keyPair, String rsaKeyStore) throws Exception {
    FileOutputStream fos = new FileOutputStream(rsaKeyStore);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    // 生成密钥
    oos.writeObject(keyPair);
    oos.close();
    fos.close();
  }

  /** 生成公钥 */
  private static RSAPublicKey generateRSAPublicKey(byte[] modules, byte[] publicExponent)
      throws Exception {
    KeyFactory keyFactory = null;
    try {
      keyFactory = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
    } catch (NoSuchAlgorithmException e) {
      throw new Exception(e.getMessage());
    }

    RSAPublicKeySpec pubKeySpec =
        new RSAPublicKeySpec(new BigInteger(modules), new BigInteger(publicExponent));

    try {
      return (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
    } catch (InvalidKeySpecException e) {
      throw new Exception(e.getMessage());
    }
  }

  /** 生成私钥 */
  private static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent)
      throws Exception {
    KeyFactory keyFac = null;
    try {
      keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
    } catch (NoSuchAlgorithmException ex) {
      throw new Exception(ex.getMessage());
    }

    RSAPrivateKeySpec priKeySpec =
        new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
    try {
      return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
    } catch (InvalidKeySpecException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
    Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING, new BouncyCastleProvider());
    // encrpt
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // spilt
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_ENCRYPT_BLOCK;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return encryptedData;
  }

  public static byte[] decrypt(PrivateKey privateKey, byte[] encryptedData) throws Exception {
    Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING, provider);
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    int inputLen = encryptedData.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // spilt
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
        cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_DECRYPT_BLOCK;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    return decryptedData;
  }

  public static byte[] hex2byte(String hex) throws IllegalArgumentException {
    if (hex.length() % 2 != 0) {
      throw new IllegalArgumentException();
    }
    char[] arr = hex.toCharArray();
    byte[] b = new byte[hex.length() / 2];
    for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
      String swap = "" + arr[i++] + arr[i];
      int byteint = Integer.parseInt(swap, 16) & 0xFF;
      b[j] = new Integer(byteint).byteValue();
    }
    return b;
  }

  public static String byte2hex(byte[] b) {
    StringBuilder hs = new StringBuilder();
    String stmp = "";
    for (byte aB : b) {
      stmp = Integer.toHexString(aB & 0xFF);
      if (stmp.length() == 1) {
        hs.append("0").append(stmp);
      } else {
        hs.append(stmp);
      }
    }
    return hs.toString().toUpperCase();
  }

  /** 字节数据转成十六进制字符串 */
  public static String byteArrayToString(byte[] data) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < data.length; i++) {
      // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
      builder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
      // 取出字节的低四位 作为索引得到相应的十六进制标识符
      builder.append(HEX_CHAR[(data[i] & 0x0f)]);
      if (i < data.length - 1) {
        builder.append(' ');
      }
    }
    return builder.toString();
  }

  /**
   * 得到公钥
   *
   * @param key 密钥字符串(经过Base64编码)
   */
  public static PublicKey getPublicKey(String key) throws Exception {
    byte[] encodedKey = Base64.decodeBuffer(key);

    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(keySpec);
  }

  /**
   * 得到私钥
   *
   * @param key 密钥字符串(经过Base64编码)
   */
  public static PrivateKey getPrivateKey(String key) throws Exception {
    byte[] encodedKey = Base64.decodeBuffer(key);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(keySpec);
  }

  /** 得到密钥字符串(经过base64编码) */
  public static String getKeyString(Key key) throws Exception {
    byte[] keyBytes = key.getEncoded();
    return Base64.encodeBufferWithoutEnd(keyBytes);
  }

  public static String sign(String content, PrivateKey privateKey) {
    try {
      Signature signature = Signature.getInstance("SHA1WithRSA");
      signature.initSign(privateKey);
      signature.update(content.getBytes(CHAR_ENCODING));
      byte[] signed = signature.sign();
      return new String(Base64.encode(signed));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static boolean checkSign(String content, String sign, PublicKey publicKey) {
    try {
      Signature signature = Signature.getInstance("SHA1WithRSA");
      signature.initVerify(publicKey);
      signature.update(content.getBytes(CHAR_ENCODING));
      return signature.verify(Base64.decode(sign.getBytes(CHAR_ENCODING)));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Load key from temp file
   *
   * @param store temp file path
   * @return key string
   * @throws Exception exception
   */
  private static String loadKey(String store) throws Exception {

    File file = new File(store);
    FileInputStream in = new FileInputStream(file);

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    try {
      String readLine = null;
      StringBuilder buffer = new StringBuilder();
      while ((readLine = reader.readLine()) != null) {
        if (readLine.charAt(0) == '-') {
          // nothing
        } else {
          buffer.append(readLine);
          buffer.append('\r');
        }
      }
      return buffer.toString();
    } finally {
      reader.close();
      in.close();
    }
  }

  /**
   * Load Private Key from PEM File
   *
   * @param privateKeyPemFile pem file instance
   * @return private key
   * @throws Exception exception
   */
  public static PrivateKey loadPrivateKey(File privateKeyPemFile) throws Exception {
    String privateKeyPEM = FileUtils.readFileToString(privateKeyPemFile, StandardCharsets.UTF_8);

    // strip of header, footer, newlines, whitespaces
    privateKeyPEM =
        privateKeyPEM
            .replaceAll("-----.+?PRIVATE.+?-----", "")
            .replaceAll("-----.+?PRIVATE.+?-----", "")
            .replaceAll("\\s", "");

    return getPrivateKey(privateKeyPEM);
  }

  public static PublicKey loadPublicKey(File publicKeyPemFile) throws Exception {
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
  public static PrivateKey loadPrivateKey(String privateKeyPEM) throws Exception {

    // strip of header, footer, newlines, whitespaces
    privateKeyPEM =
        privateKeyPEM
            .replaceAll("-----.+?PRIVATE.+?-----", "")
            .replaceAll("-----.+?PRIVATE.+?-----", "")
            .replaceAll("\\s", "");

    return getPrivateKey(privateKeyPEM);
  }

  public static PublicKey loadPublicKey(String publicKeyPEM) throws Exception {

    // strip of header, footer, newlines, whitespaces
    publicKeyPEM =
        publicKeyPEM
            .replaceAll("-----.+?PUBLIC.+?-----", "")
            .replaceAll("-----.+?PUBLIC.+?-----", "")
            .replaceAll("\\s", "");
    return getPublicKey(publicKeyPEM);
  }

  /**
   * Copy the contents of the given InputStream into a String. Leaves the stream open when done.
   *
   * @param in the InputStream to copy from (may be {@code null} or empty)
   * @return the String that has been copied to (possibly empty)
   * @throws IOException in case of I/O errors
   */
  public static String copyToString(InputStream in, Charset charset) throws IOException {
    if (in == null) {
      return "";
    }

    StringBuilder out = new StringBuilder();
    InputStreamReader reader = new InputStreamReader(in, charset);
    char[] buffer = new char[4096];
    int bytesRead = -1;
    while ((bytesRead = reader.read(buffer)) != -1) {
      out.append(buffer, 0, bytesRead);
    }
    return out.toString();
  }
}
