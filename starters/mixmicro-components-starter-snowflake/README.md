## Acmedcare Snowflake Support Starter

### How to use


- Maven Dependency

```xml

<dependency>
    <groupId>com.yunlsp.framework.components</groupId>
    <artifactId>mixmicro-components-starter-snowflake</artifactId>
    <version>1.0.4</version>
</dependency>

```

- Code Example `@EnableSnowflake`

```java

@SpringBootApplication
@EnableAutoConfiguration
@EnableSnowflake(workerId = "2")
public class SnowflakeAutoConfigTestApplication {

  @Resource private Snowflake snowflake;

  @Test
  public void nextId() {
    System.out.println(this.snowflake.nextId());
  }
}


```
