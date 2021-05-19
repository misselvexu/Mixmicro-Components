## Mixmicro Spring CLoud OpenFeign Guide

## Features

---
### Service Call Service Over Openfeign With `Request Headers` （服务间调用值传递）

#### **First step** Upgrade `Components Framework Version`

> Minimum version `1.0.5.RC5`


Maven Management Dependency

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-dependencies</artifactId>
            <version>[1.1.0.RELEASE,)</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>

```

Maven Dependency

```xml

<dependency>
    <groupId>com.yunlsp.framework.components</groupId>
    <artifactId>mixmicro-components-starter-openfeign</artifactId>
</dependency>

```

#### **Second step** choose your `request header` type

Support Types: 

- Dirent Web Request Headers 【直接透传Web请求头信息】
- Custom Attributes 【自定义属性传递】【推荐】
- Application Properties Keys 【自定义配置文件固定值传递】 【不推荐，推荐使用第二种方式替代】


#### **Thrid step:** how to use

- Dirent Web Request Headers 【直接透传Web请求头信息】

```properties

## config transported header names .
mixmicro.feign.sensitive-headers=header-name-1,header-name-2

```

- Custom Attributes 【自定义属性传递】

```properties

## config custom attribute(s) as list
mixmicro.feign.attributes[0].name=header-a1

## attribute's type contain:  manual | request_header
mixmicro.feign.attributes[0].type=manual

## attribute value from request header demo
mixmicro.feign.attributes[1].name=header-a2
mixmicro.feign.attributes[1].type=request_header

```

> if attribute's type is `manual` , use api `FeignAttributes`

```java

// defined attributes map
Map<String, String> attributes = Maps.newHashMap();
// put key - values
attributes.put("header-a1", "ccv");
// add attribute's map
FeignAttributes.addAttributes(attributes);

```


- Application Properties Keys 【自定义配置文件固定值传递】

```properties

## config feign metadata env-leys map 
mixmicro.feign.metadata.env-keys.appname=demo.appname


### properties config value
demo.appname=demo-appname

```

---
### Service Call Service Exception Handler (服务间调用异常处理)

> Inter-service call exception handling consists of two cases （服务间调用异常处理包含两种情况）

#### Situation 1: Turning on the fusion downgrade （`Hystrix`开启的情况）

```properties
feign.hystrix.enabled=true
```

在`Hystrix`关闭的情况下，服务间调用的异常只有一种情况如下：

- com.netflix.hystrix.exception.HystrixBadRequestException

**注意**：如果是熔断的异常，如何获取到内部的服务间调用异常，实例代码如下：

```java

    // 框架抛出的熔断异常，业务可以捕获
    HystrixBadRequestException hbre = (HystrixBadRequestException) cause;
    // 获取堆栈
    Throwable throwable = hbre.getCause();
    
    // 判断解析调用异常， 此处可以用框架提供的工具类代替： xyz.vopen.mixmicro.components.boot.openfeign.decoder.FeignRuntimeExceptions
    if (throwable instanceof MixmicroInvokeServerException) {

      MixmicroInvokeServerException ise = (MixmicroInvokeServerException) throwable;
      Object response = ise.getResponse();
      
      if (response instanceof ResponseEntity<?>) {
        
        // 业务异常状态码和消息体 
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
        log.info("[FEIGN INVOKE EXCEPTION] http state code : {}, biz code : {}, biz exception : {}", ise.getHttpCode(), responseEntity.getCode(), responseEntity.getMessage());
        
        // 
      
      }
    }

```



#### Situation 2: Turning off the fusion downgrade （`Hystrix`关闭的情况）

```properties
feign.hystrix.enabled=false
```

在`Hystrix`关闭的情况下，服务间调用的异常只有两种情况如下：

- xyz.vopen.mixmicro.components.exception.defined.MixmicroInvokeClientException
- xyz.vopen.mixmicro.components.exception.defined.MixmicroInvokeServerException


#### Anyway: **Final Java Code** (兼容上述的两种情况)

```java

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 远程Feign调用对象
 */
@Autowired
private RemoteBizFeignClient client;

/**
 * 业务方法
 */
public void biz() {
  try{
    // 远程过程调用
    Object result = client.callRemoteMethod(param1,param2,...);
    // ...
    // ...
  } catch(Exception e) {
    // 业务异常处理逻辑
    Optional<ExceptionResponse<?>> optional = FeignRuntimeExceptions.resolving(e);
    
    if (optional.isPresent()) {
      // Get call exception code
      int bizCode = optional.get().getCode();
      // Exception description information
      String bizMessage = optional.get().getMessage();
      // Judgment exception handling
      switch (bizCode) {
        // 业务异常编码，业务侧提供
        case 1001:
          // .......
          break;
        case 1002:
          // .......
          break;
        default:
          // .......
        break;
      }
    }
  }
}

```