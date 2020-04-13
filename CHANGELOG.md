## CHANGELOG

### 1.0.0.RC2

#### features

> Apis

- Defined common api interface
- Defined exception api interface
- Defined logger api interface

> Kits

- Common Kits (`String ,Object ,Date ,Class ,Classloader ,Map ,Collection ,Thread , Executor ...Kits`)
- Http Client Kits (`base on apache http client`)
- Servlet Compress Filter
- Jre Http Client Kit (base on `HttpURLConnection`)
- Timer Kit (`HashedWheelTimer` support)
- Spring Context Support
- Executor Kit (`Retriable ,timeout ,AsyncRuntimExecutor` ... Kits)
- Event Bus Kit
- Java Serializable implements With `Hessian`
- big queue base on `Memory Mapped File` .
- File Compression Kit (`zip`,`gz`,`tar`,`7z` ... Kits)
- Retry Kit
- Timed-Globally unique identifiers Kit

> Spring Boot Starters

- Mix microservice project `banner`
- `Redis Cache` support (Jedis & Lettuce & SpringData)
- Web server `cors` support
- Web server http message converter implements with `fastjson`.
- Spring boot starter logger api support.
- `Spring Cloud Openfeign` integrate
- `Jax-RS` REST Framework Starter With `RESTEasy`.
- Spring Cloud Project Proxy Support.
- Distributed sequence number generator implement with `snowflake`.

#### Useage

```xml
<dependencyManagement>
   <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-dependencies</artifactId>
            <version>2020.03.RC7</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>               
    </dependencies>
</dependencyManagement>

```