package xyz.vopen.mixmicro.components.authorization;

import org.junit.Test;

import java.security.interfaces.RSAPrivateKey;

import static org.junit.Assert.*;

/**
 * {@link SerializationTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-11.
 */
public class SerializationTest {


  @Test
  public void testLoad() throws Exception {

    Serialization.AuthorizationKeyLoader loader = new Serialization.AuthorizationKeyLoader(SerializationTest.class.getClassLoader());

    RSAPrivateKey key = loader.loadPrivateKey("META-INF/mixmicro_default_authorization_pkcs8_private_key.pem");

    assertNotNull(key);

    loader.loadKey("META-INF/mixmicro_default_authorization_pkcs8_private_key.pem", RSAPrivateKey.class);

    loader.loadKey("/Users/misselvexu/Documents/yunlsp.gitlab.com/Mixmicro-Components/apis/authorization/src/main/resources/META-INF/mixmicro_default_authorization_pkcs8_private_key.pem", RSAPrivateKey.class);

  }

}