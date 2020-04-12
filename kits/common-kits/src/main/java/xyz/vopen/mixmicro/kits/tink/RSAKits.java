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

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import xyz.vopen.mixmicro.kits.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA Utils
 *
 * @author Elve.Xu [iskp.me<at>gmail.com]
 * @version v1.2 - 2018/5/16.
 */
@SuppressWarnings({"Duplicates", "AlibabaClassNamingShouldBeCamel"})
@UtilityClass
public final class RSAKits {

  private static final String RSA_ALGORITHM = "RSA";
  private static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";

  public static RSAPublicKey getPublicKey(String publicKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
      X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decodeBuffer(publicKey));
      return (RSAPublicKey) keyFactory.generatePublic(spec);
    } catch (Exception e) {
      throw new RuntimeException("un-support algorithm key", e);
    }
  }

  public static RSAPrivateKey getPrivateKey(String privateKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decodeBuffer(privateKey));
      return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    } catch (Exception e) {
      throw new RuntimeException("un-support algorithm key", e);
    }
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
    String publicKeyStr = Base64.encodeBuffer(publicKey.getEncoded());
    Key privateKey = keyPair.getPrivate();
    String privateKeyStr = Base64.encodeBuffer(privateKey.getEncoded());
    Map<String, String> keyPairMap = new HashMap<>(2);
    keyPairMap.put("publicKey", publicKeyStr);
    keyPairMap.put("privateKey", privateKeyStr);

    return keyPairMap;
  }

  public static String encrypt(String data, RSAPublicKey publicKey) {
    try {
      Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      return Base64.encodeBuffer(
          rsaSplitCodec(
              cipher,
              Cipher.ENCRYPT_MODE,
              data.getBytes(StandardCharsets.UTF_8),
              publicKey.getModulus().bitLength()));
    } catch (Exception e) {
      throw new RuntimeException("RSA Encrypt [" + data + "] happened exception", e);
    }
  }

  public static String decrypt(String data, RSAPrivateKey privateKey) {
    try {
      Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return new String(
          rsaSplitCodec(
              cipher,
              Cipher.DECRYPT_MODE,
              Base64.decodeBuffer(data),
              privateKey.getModulus().bitLength()),
          StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("RSA Decrypt  [" + data + "] happened exception", e);
    }
  }

  private static String privateEncrypt(String data, RSAPrivateKey privateKey) {
    try {
      Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, privateKey);
      return Base64.encodeBuffer(
          rsaSplitCodec(
              cipher,
              Cipher.ENCRYPT_MODE,
              data.getBytes(StandardCharsets.UTF_8),
              privateKey.getModulus().bitLength()));
    } catch (Exception e) {
      throw new RuntimeException("RSA Encrypt [" + data + "] happened exception", e);
    }
  }

  private static String publicDecrypt(String data, RSAPublicKey publicKey) {
    try {
      Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, publicKey);
      return new String(
          rsaSplitCodec(
              cipher,
              Cipher.DECRYPT_MODE,
              Base64.decodeBuffer(data),
              publicKey.getModulus().bitLength()),
          StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("RSA Decrypt  [" + data + "] happened exception", e);
    }
  }

  public static String sign(String data, RSAPrivateKey privateKey) {
    try {
      // sign
      Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
      signature.initSign(privateKey);
      signature.update(data.getBytes(StandardCharsets.UTF_8));
      return Base64.encodeBuffer(signature.sign());
    } catch (Exception e) {
      throw new RuntimeException("RSA sign [" + data + "] happened exception", e);
    }
  }

  public static boolean verify(String data, String sign, RSAPublicKey publicKey) {
    try {
      Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
      signature.initVerify(publicKey);
      signature.update(data.getBytes(StandardCharsets.UTF_8));
      return signature.verify(Base64.decodeBuffer(sign));
    } catch (Exception e) {
      throw new RuntimeException("RSA verify sign [" + data + "] happened exception", e);
    }
  }

  private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
    int maxBlock = 0;
    if (opmode == Cipher.DECRYPT_MODE) {
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
      throw new RuntimeException("RSA split codec [" + maxBlock + "] happened exception", e);
    }
    byte[] resultDatas = out.toByteArray();
    IOUtils.closeQuietly(out);
    return resultDatas;
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
