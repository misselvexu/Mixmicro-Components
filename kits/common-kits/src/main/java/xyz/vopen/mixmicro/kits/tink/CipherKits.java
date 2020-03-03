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

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import lombok.experimental.UtilityClass;

/**
 * 加密、解密相关工具.
 *
 * <p>CipherUtils类包括的方法可以对指定的字符串进行标准的"MD5"加密.
 *
 * <p>CipherUtils类中的方法, 除非有特殊说明, 否则传入<code>null</code>将返回<code>null</code>.
 *
 * @author Elve.Xu
 */
@UtilityClass
public final class CipherKits {

  private static final String PBE_WITH_MD5_AND_DES = "PBEWithMD5AndDES";
  private static final String PASSPHRASE = "BQA<vom_PMfcixasdas1231254>>Sh123XEa#NLIw@Osj^U";
  private static byte[] salt = {
    (byte) 0xA9,
    (byte) 0x9B,
    (byte) 0xC8,
    (byte) 0x32,
    (byte) 0x56,
    (byte) 0x35,
    (byte) 0xE3,
    (byte) 0x03
  };

  /**
   * 对指定参数做标准的"MD5"加密, 返回加密后的结果.
   *
   * <blockquote>
   *
   * <pre>
   * String cipher = CipherUtils.md5("abc");
   * </pre>
   *
   * </blockquote>
   *
   * @param plain 加密的字符串.
   * @return 加密后的字符串.
   * @deprecated
   */
  public static String md5(String plain) {
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.reset();
      messageDigest.update(plain.getBytes("UTF-8"));
    } catch (Exception e) {
      return null;
    }

    byte[] byteArray = messageDigest.digest();
    StringBuilder buff = new StringBuilder();
    for (byte aByteArray : byteArray) {
      if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
        buff.append("0").append(Integer.toHexString(0xFF & aByteArray));
      } else {
        buff.append(Integer.toHexString(0xFF & aByteArray));
      }
    }

    return buff.toString();
  }

  /**
   * 对指定参数做MD5加密.
   *
   * <blockquote>
   *
   * <pre>
   * String cipher = CipherUtils.MD5("abc");
   * </pre>
   *
   * </blockquote>
   *
   * @param plain 加密的字符串.
   * @return 加密后的字符串.
   */
  public static String MD5(final String plain) {
    return SHA(plain, "MD5");
  }

  /**
   * 对指定参数做SHA-256加密.
   *
   * <blockquote>
   *
   * <pre>
   * String cipher = CipherUtils.SHA256("abc");
   * </pre>
   *
   * </blockquote>
   *
   * @param plain 加密的字符串.
   * @return 加密后的字符串.
   */
  public static String SHA256(final String plain) {
    return SHA(plain, "SHA-256");
  }

  /**
   * 对指定参数做SHA-512加密.
   *
   * <blockquote>
   *
   * <pre>
   * String cipher = CipherUtils.SHA512("abc");
   * </pre>
   *
   * </blockquote>
   *
   * @param plain 加密的字符串.
   * @return 加密后的字符串.
   */
  public static String SHA512(final String plain) {
    return SHA(plain, "SHA-512");
  }

  private static String SHA(final String plain, final String type) {
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance(type);
      messageDigest.reset();
      messageDigest.update(plain.getBytes("UTF-8"));
    } catch (Exception e) {
      return null;
    }

    byte[] byteArray = messageDigest.digest();
    StringBuilder buff = new StringBuilder();
    for (byte aByteArray : byteArray) {
      if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
        buff.append("0").append(Integer.toHexString(0xFF & aByteArray));
      } else {
        buff.append(Integer.toHexString(0xFF & aByteArray));
      }
    }

    return buff.toString();
  }

  /**
   * 对指定参数做基于<code>"PBEWithMD5AndDES"</code>的加密, 返回加密后的结果.
   *
   * <blockquote>
   *
   * <pre>
   * String cipher = CipherUtils.encrypt("abc");
   * </pre>
   *
   * </blockquote>
   *
   * @param plain 加密的字符串
   * @return 加密后的字符串.
   * @see KeySpec
   * @see SecretKey
   * @since jdk1.5
   */
  public static String encrypt(String plain) {
    return encrypt(plain, PASSPHRASE);
  }

  /**
   * 对指定参数做基于<code>"PBEWithMD5AndDES"</code>的加密, 返回加密后的结果.
   *
   * <blockquote>
   *
   * <pre>
   * String cipher = CipherUtils.encrypt("abc");
   * </pre>
   *
   * </blockquote>
   *
   * @param plain 加密的字符串
   * @param passphrase 自定义钥匙串
   * @return 加密后的字符串.
   * @see KeySpec
   * @see SecretKey
   * @since jdk1.5
   */
  public static String encrypt(String plain, String passphrase) {
    try {
      KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 19);
      SecretKey key = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES).generateSecret(keySpec);
      Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
      AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, 19);
      ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
      byte[] utf8 = plain.getBytes("UTF-8");
      byte[] enc = ecipher.doFinal(utf8);
      return new String(Base64.encode(enc));
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 对指定参数做基于<code>"PBEWithMD5AndDES"</code>的解密, 返回解密后的结果.
   *
   * <blockquote>
   *
   * <pre>
   * String plain = CipherUtils.decrypt("/bvYmxEHqyM=");
   * </pre>
   *
   * </blockquote>
   *
   * @param secret 解密的字符串.
   * @return 解密后的字符串
   * @see #encrypt(String)
   * @since jdk1.5
   */
  public static String decrypt(String secret) {
    return decrypt(secret, PASSPHRASE);
  }

  /**
   * 对指定参数做基于<code>"PBEWithMD5AndDES"</code>的解密, 返回解密后的结果.
   *
   * <blockquote>
   *
   * <pre>
   * String plain = CipherUtils.decrypt("/bvYmxEHqyM=");
   * </pre>
   *
   * </blockquote>
   *
   * @param secret 解密的字符串.
   * @param passphrase 自定义钥匙串
   * @return 解密后的字符串
   * @see #encrypt(String)
   * @since jdk1.5
   */
  public static String decrypt(String secret, String passphrase) {
    try {
      KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 19);
      SecretKey key = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES).generateSecret(keySpec);
      byte[] dec = Base64.decode(secret.getBytes());
      Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
      AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, 19);
      dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
      byte[] utf8 = dcipher.doFinal(dec);
      return new String(utf8, "UTF-8");

    } catch (Exception e) {
      return null;
    }
  }
}
