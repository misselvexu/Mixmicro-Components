## Spring Cloud Event Reference （参考文档）

### Cloud Event Features (特性)

- Support consumer broadcasting model （支持广播消费模式，暂不支持集群消费）


### Maven Dependency （依赖）

> 引入组件总依赖库，项目顶级`pom.xml`

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-dependencies</artifactId>
            <version>1.0.2.RC1</version>
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
        <artifactId>mixmicro-components-starter-cloud-event</artifactId>
    </dependency>
    
    <!-- 可选 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>

```

### 应用配置

#### 发送（接受）端配置

```properties

## 绑定消息队列地址
spring.cloud.stream.rocketmq.binder.name-server=10.10.10.52:9876

## 定义当前事件应用的标识
spring.cloud.bus.id=${spring.application.name}:${server.port}

## 定义日志级别
logging.level.org.springframework.cloud.bus=debug
logging.level.org.springframework.cloud.stream=debug

## 指定应用输出的端点
management.endpoints.web.exposure.include=*

```

#### 发送端示例代码

> 发送端事件定义方式

- 方式一

使用组件库制定的事件类 `org.springframework.cloud.bus.event.DefaultRemoteApplicationEvent`


- 方式二

自定义事件类并继承自 `org.springframework.cloud.bus.event.RemoteApplicationEvent`

```java
public class SampleEvent extends RemoteApplicationEvent { 
    // …………
}
```


并且需要添加事件包扫描注解

```java

@RemoteApplicationEventScan(value = {
    "org.springframework.samples.bus"
})

```

> 发送方代码示例


```java

// 注入事件发送对象
@Autowired private ApplicationEventPublisher publisher;

// 定义事件信息数据传递载体
SampleEventPayload payload = SampleEventPayload.builder()
        .id(id)
        .name(name)
        .timestamp(new Date())
        .build();

// RC2版本会自动获取该数据，无序应用端提供
String originService = "bus-provider:9091";

// 事件接受方标识
String destinationService = "bus-consumer"

// 定义事件信息
RemoteApplicationEvent event = new DefaultRemoteApplicationEvent<>(payload, this, originService, destinationService);


```


> 接受方代码示例

如果有使用上述自定义事件（[自定义事件方式二](#发送端示例代码)），那么接收方同样需要配置事件包扫描注解

```java

@RemoteApplicationEventScan(value = {
    "org.springframework.samples.bus"
})

```

- 方式一

通过使用`Spring Framework`默认监听器`org.springframework.context.ApplicationListener`

```java

@Component
public class EventProcessor implements ApplicationListener<SampleEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(SampleEvent event) {
      System.out.printf("<<< Event: %s \n", event);
      System.out.printf("[SampleEvent CONSUMER] received event payload: %s \n", event.getPayload());
    }
}

```

- 方式二

通过使用组件扩展的监听器 `xyz.vopen.mixmicro.components.enhance.event.listener.MixmicroEventListener`

```java

@Component
public class CustomMixmicroEventListener extends MixmicroEventListener {

    @Override
    public void onEvent(DefaultRemoteApplicationEvent<?> event) {
      System.out.printf("<<< [RemoteApplicationEvent CONSUMER] received event payload: %s \n", event);

      System.out.println(SerializableBean.encode(event.getPayload()));
    }
}

```





