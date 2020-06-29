## Message Queue Reference （参考文档）

### Message Queue Features (特性)



### Maven Dependency （依赖）

> 引入组件总依赖库，项目顶级`pom.xml`

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-dependencies</artifactId>
            <version>1.0.5.RC3</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>

```

> 引入事件库

```xml
<dependencies>
    
    <dependency>
        <groupId>com.yunlsp.framework.components</groupId>
        <artifactId>mixmicro-components-starter-message-queue</artifactId>
    </dependency>
    
    <!-- 可选 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>

```

### 发送方应用配置

#### 发送端配置

```properties

## 绑定消息队列地址
spring.cloud.stream.rocketmq.binder.name-server=10.10.10.52:9876

## 配置消息主题
### 配置消息发送通道 （示例主题名称：stream-topic）
spring.cloud.stream.bindings.stream-output-channel.destination=stream-topic

### 配置消息发送通道数据格式
spring.cloud.stream.bindings.stream-output-channel.content-type=application/json

### 配置消息发送通道组(可选)
#spring.cloud.stream.rocketmq.bindings.stream-output-channel.producer.group=binder-group
#spring.cloud.stream.rocketmq.bindings.stream-output-channel.producer.sync=true

## 定义日志级别
logging.level.org.springframework.cloud.bus=debug
logging.level.org.springframework.cloud.stream=debug

## 指定应用输出的端点
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always

```

#### 发送端示例代码

> 定义发送端通道绑定标识

```java

/**
 * {@link SampleProviderSource}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/17
 */
public interface SampleProviderSource {

  String OUTPUT = "stream-output-channel";


  /**
   * 定义输出的主题标识
   *
   * @return {@link org.springframework.messaging.MessageChannel}
   */
  @Output(OUTPUT)
  MessageChannel output();

}

```

> 绑定发送通道到组件库

通过注解`@EnableBinding`绑定即可

```java

@EnableBinding({
    SampleProviderSource.class
})

```

> 定义发送端数据载体

```java

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SamplePayload implements Serializable {

  // === Payloads Fields ===

  private int id;

  private String name;

  private long timestamp = System.currentTimeMillis();

  // === equals & hashcode ===

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SamplePayload that = (SamplePayload) o;
    return id == that.id &&
        timestamp == that.timestamp &&
        name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, timestamp);
  }
}

```

> 发送端发送消息示例

```java

// 注入发送端通道对象 
@Autowired private SampleProviderSource sampleProviderSource;

public void send(SamplePayload payload) {

    if (payload != null) {
    
      // 构建消息 Builder
      MessageBuilder<SamplePayload> builder = MessageBuilder.withPayload(payload);
    
      // 构建消息对象
      Message<SamplePayload> message = builder
          .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
          .build();
      
      // 发送消息过程
      boolean result = sampleProviderSource.output().send(message);
    
      log.info("Send result: {}, {} , destination: {}", result, payload, sampleProviderSource.output());
    
      return;
    }
    
    log.warn("Message payload is empty.");
}

```




### 消费方应用配置

#### 消费端配置

```properties

## 绑定消息队列地址
spring.cloud.stream.rocketmq.binder.name-server=10.10.10.52:9876

## 配置消息主题
### 配置消息消费通道 （示例主题名称：stream-topic ，对应发送方的主题名称）
spring.cloud.stream.bindings.stream-input-channel.destination=stream-topic
spring.cloud.stream.bindings.stream-input-channel.content-type=application/json

### 消息组配置， 注意：同一个组的消费者有且只有一个消费者消费到消息
spring.cloud.stream.bindings.stream-input-channel.group=G2

### 消息者消费特性配置 （可选）
#spring.cloud.stream.rocketmq.bindings.stream-input-channel.consumer.orderly=true


## 定义日志级别
logging.level.org.springframework.cloud.bus=debug
logging.level.org.springframework.cloud.stream=debug

## 指定应用输出的端点
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always

```

#### 消费端示例代码

> 定义消费端通道绑定标识

```java
/**
 * {@link SampleCustomerSink}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/17
 */
public interface SampleCustomerSink {

  String INPUT = "stream-input-channel";


  /**
   * 定义输入的主题标识
   *
   * @return {@link MessageChannel}
   */
  @Input(INPUT)
  SubscribableChannel output();

}

```

> 绑定消费通道到组件库

通过注解`@EnableBinding`绑定即可

```java

@EnableBinding({
    SampleCustomerSink.class
})

```

> 定义端数据载体

```java

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SamplePayload implements Serializable {

  // === Payloads Fields ===

  private int id;

  private String name;

  private long timestamp = System.currentTimeMillis();

  // === equals & hashcode ===

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SamplePayload that = (SamplePayload) o;
    return id == that.id &&
        timestamp == that.timestamp &&
        name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, timestamp);
  }
}

```

> 消费端发送消息示例

消费端可使用注解`@StreamListener` 进行绑定消费

```java

// 
@StreamListener(value = INPUT)
public void onStreamTopicMessage(SamplePayload payload) {

  log.info("[customer] receive message from :{} , payload: {}" , INPUT, payload);

}

```


#### 基于`RocketMQ`的消息分流与过滤使用示例

> editing
