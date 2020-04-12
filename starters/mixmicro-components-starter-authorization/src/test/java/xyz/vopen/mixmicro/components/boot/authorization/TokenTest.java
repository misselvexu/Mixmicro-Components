package xyz.vopen.mixmicro.components.boot.authorization;

import lombok.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.vopen.mixmicro.components.authorization.Payload;
import xyz.vopen.mixmicro.components.authorization.Serialization;
import xyz.vopen.mixmicro.components.authorization.Token;
import xyz.vopen.mixmicro.components.authorization.api.AuthorizationService;

/**
 * {@link TokenTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthorizationTest.class)
public class TokenTest {

  @Autowired private AuthorizationService service;

  @Test
  public void token() throws Exception {

    Payload payload = new Payload("user id value", "mobile value");

    MyPayload myPayload = MyPayload.builder().mobile("13910187669").userId("U01").key("K1").build();

    Token token = service.generateToken(myPayload);

    System.out.println(Serialization.toJsonString(token));

    Thread.sleep(10000);

    service.validateToken(token.getAccessToken());

    MyPayload decodePayload = service.decodeTokenPayload(token.getAccessToken(), MyPayload.class);

    System.out.println(decodePayload);
  }

  @Getter
  @Setter
  public static class MyPayload extends Payload {

    private String key;

    @Builder
    public MyPayload(String userId, String mobile, String key) {
      super(userId, mobile);
      this.key = key;
    }
  }
}
