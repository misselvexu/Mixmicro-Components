## Mix Token Starter

### Maven Dependency

```xml

<dependencyManagement>
   <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-dependencies</artifactId>
            <version>1.0.5.RC2</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>               
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>com.yunlsp.framework.components</groupId>
        <artifactId>mixmicro-components-starter-authorization</artifactId>
    </dependency>
</dependencies>

```

### Config Properties

> if you want to use custom rsa keys

```bash

## generate rsa private key , (1024 or 2048)
openssl genrsa -out rsa_private_key.pem 2048

## create Java Public Key 
openssl cipher -in rsa_private_key.pem -out rsa_public_key.pem -pubout

## create Java Private Key
openssl pkcs8 -topk8 -in rsa_private_key.pem -out pkcs8_private_key.pem -nocrypt

```

> copy your pem file(s) into `src/main/resources`

```properties

## private key pem file 
mixmicro.authorization.private-key-pem=pkcs8_private_key.pem

## public key pem file 
mixmicro.authorization.public-key-pem=rsa_public_key.pem

## debug enabled 
mixmicro.authorization.debug=true

## access token ttl (ms), default: 7 days. 
mixmicro.authorization.access-token-time-to-live=10000

```

### Usage 

> Default Payload Instance 

```java

Payload payload = new Payload("user id value", "mobile value");

```

> you can `extends` Payload , custom your required properties .

```java

@Getter
@Setter
public static class MyPayload extends Payload {

  private String customProperties;

  @Builder
  public MyPayload(String userId, String mobile, String customProperties) {
    super(userId, mobile);
    this.customProperties = customProperties;
  }
}

```

> Service `AutoWired`

```java

// Auto Inject Service Instance
@Autowired private AuthorizationService service;

```

> Apis

```java

/**
* Generate New Access Token With Defined {@link Payload}
*
* @param payload payload instance
* @return instance of {@link AccessToken}
* @throws AuthorizationException maybe thrown {@link AuthorizationException}
*/
<P extends Payload> Token generateToken(@NonNull P payload) throws AuthorizationException;

/**
* Check provide {@link AccessToken} is valid .
*
* @param accessToken instance of {@link AccessToken}
* @throws IllegalAccessTokenException thrown {@link IllegalAccessTokenException} when illegal
*     access token provide
* @throws ExpiredAccessTokenException thrown {@link ExpiredAccessTokenException} when access
*     token is expired
*/
void validateToken(@NonNull String accessToken) throws IllegalAccessTokenException, ExpiredAccessTokenException;

/**
* Decode Access Token's Payload instance
* @param accessToken Access Token Instance
* @param clazz Target custom class type of {@link Payload}
* @return instance result of {@link Payload}
* @throws AuthorizationException maybe thrown {@link AuthorizationException}
*/
<P extends Payload> P decodeTokenPayload(@NonNull String accessToken, Class<P> clazz) throws AuthorizationException;


```

> Output Values

- `Token` Json

```json

{
	"token_id": "1m1uj924He37JiFi9A3Ub$p4T4s",
	"access_token": "Rt4cgc0wbmhvIi0h+Qw004BWVIYnm/Ti/XnUO6HVsBdZyurCYb0oPPRxeRlGXpyZ6gWUX7nTBafMCHPIjMrB5CwPpvIxaPgUQtbOtPSN35L+N3uHtEB4JSOLJylERh0/RoqvPQiDrb5/8gbRkY6u/7unHHlnv7+3xy3mlnc7F7EHduPPWzgk5OsjYZQhc8UQ7Hm+9hMnZcXjTcS3UnWNd+K1mJXOsYw9kcNxoE8pfuWw4sBlQgleYR4iUBfQGmujhwcgofX3RLlKFS27JCO768SvySwQE9XWkt0qOweOW9gxFcSL96JOt0qVr7ZN5gHfONACI6qjO5jLrgMs7faDIxliNsEyT5hrzTL2h9jPVdQzXgoLpa4jI4fkoN5dDJubsWp/cXuRI3hXyhqlpZpQ/UjpDMSxV0At52h2KaSFQGytygLz2I5ApBclLHlhnXyNoN9VWMH7WVV8QNRObps6DN+tCKHSlqNW8rRY8d4qZaU9cNwzHmI6/aPAnCl6rca7Py3F1K05ktAPFKf5HsNwB26XwRwn/dRprTBp5bQ2q1pDV+nAgQVQQInEcRR4RvDTKptSEiyCNmEB3PjU28tSyrdy6gt4A2WeLe64ONfo1S535tUxyYxpliPzIOygZsVduuybE46agNp3MjaO/CywwghFm6FNSUvrHuMddWz/DZY\u003d",
	"expires_in": "10000",
	"refresh_token": "TyoJzgVd3SqiTpaCdeibR7lVPQZY/h1rDxhF9TR6vX8hAp0+p4iV+hH20/ks9EOj3KJuRh7DCOZ3DdoHlOJwFutktw6PCF5Uj7v7DseKmtIKVYI3WLYTuyOxPvseHthA0X9Q5Cm7q+VBgNDcNRfawyfZYA82QcUJ91uLeMPQFrOBd9x/+S3JSKoymnkduGkkjqNntV+pqCTll8mnMHG0xYxca0hgSkZhkyj+3DjO5fkidQTZv7epBkhgYxVyBdU2sphBVX1zg7ikSjXeEPaTsZDcxZZXmMeYSc3ZvglOwCZ73tiKhkjji9M1ybeck3R5hpNh6tG4aCd/1mOLivLoVQ\u003d\u003d"
}

```

### Example

```java

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

  // 自定义用户信息载体

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

```