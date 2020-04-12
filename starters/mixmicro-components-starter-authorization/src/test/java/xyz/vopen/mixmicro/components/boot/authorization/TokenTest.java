package xyz.vopen.mixmicro.components.boot.authorization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.vopen.mixmicro.components.authorization.Payload;
import xyz.vopen.mixmicro.components.authorization.Token;
import xyz.vopen.mixmicro.components.authorization.api.AuthorizationService;
import xyz.vopen.mixmicro.components.authorization.exception.AuthorizationException;
import xyz.vopen.mixmicro.components.authorization.exception.ExpiredAccessTokenException;
import xyz.vopen.mixmicro.components.authorization.exception.IllegalAccessTokenException;

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

    // 创建用户信息载体
    MyPayload myPayload = MyPayload.builder().mobile("13910187669").userId("U01").key("K1").build();

    // 创建 Token
    Token token = service.generateToken(myPayload);

    // 校验 Token 有效性
    try{
      service.validateToken(token.getAccessToken());
    } catch (IllegalAccessTokenException e) {
      // 无效 Token 异常
    } catch (ExpiredAccessTokenException e) {
      // Token 过期异常
    } catch (AuthorizationException e) {
      // 其他 Token 处理异常
    }

    // 解析 Token 用户信息
    MyPayload decodePayload = service.decodeTokenPayload(token.getAccessToken(), MyPayload.class);

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
