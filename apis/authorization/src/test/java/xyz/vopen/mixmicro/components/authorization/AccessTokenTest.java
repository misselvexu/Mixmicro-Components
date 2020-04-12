package xyz.vopen.mixmicro.components.authorization;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * {@link AccessTokenTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-11.
 */
public class AccessTokenTest {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testRsa01() throws Exception {

    Serialization.AuthorizationKeyLoader loader =
        new Serialization.AuthorizationKeyLoader(SerializationTest.class.getClassLoader());

    String content = "misselvexu";
    String privateKey =
        "/Users/misselvexu/Documents/yunlsp.gitlab.com/Mixmicro-Components/apis/authorization/src/main/openssl/2048/pkcs8_private_key.pem";
    String publicKey =
        "/Users/misselvexu/Documents/yunlsp.gitlab.com/Mixmicro-Components/apis/authorization/src/main/openssl/2048/rsa_public_key.pem";

    RSAPrivateKey pkey = Serialization.Cipher.loadPrivateKey(new File(privateKey));
    RSAPublicKey key = Serialization.Cipher.loadPublicKey(new File(publicKey));

    loader.loadPublicKey(AuthorizationConfig.DEFAULT.getPublicKeyPem());

    //    Serialization.Cipher cipher = new
    // Serialization.Cipher(Base64.encodeBuffer(key.getEncoded()),Base64.encodeBuffer(pkey.getEncoded()));
    Serialization.Cipher cipher =
        new Serialization.Cipher(
            loader.loadPublicKey(AuthorizationConfig.DEFAULT.getPublicKeyPem()),
            loader.loadPrivateKey(AuthorizationConfig.DEFAULT.getPrivateKeyPem()));

    String publicEncryptResult = cipher.publicEncrypt(content);

    System.out.println("publicEncryptResult = " + publicEncryptResult);

    String privateDecryptResult = cipher.privateDecrypt(publicEncryptResult);

    System.out.println("privateDecryptResult = " + privateDecryptResult);

    String sign = cipher.sign(content);

    System.out.println("sign = " + sign);

    boolean valid = cipher.verify(content, sign);

    System.out.println("valid = " + valid);
  }
}
