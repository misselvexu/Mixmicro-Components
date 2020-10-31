## CHANGELOG

--------
### 1.0.6.RC1

#### ISSUES

- Fix Circuit-Breaker issues .
- Fix Application Health Kit Configuration Properties Spell Issues .

#### IMPROVE

- Improve Spring Web & Openfeign Header Transport Mechanism .
- Improve Circuit-Breaker Event Notice Mechanism .

#### FEATURES

- Add WebSocket Development Enhancement .
- Add Metrics Spring Development Enhancement .
- Defined Application Health Check Api .
- Add Email Integrate Components .
- Add Database Middleware Application Integrate Component .
- Add Spring Boot Application Graceful Shutdown Support .

--------
### 1.0.0.RC8

#### features
- add web request logger aspect supported .
- add logger specification defined `mixmicro-components-boot-logger` supported. 
- add open feign invoked carry use custom headers support .
- upgrade message queue stream dependency version `1.0.0.RC3` .
- add request api security module starter supported .
- add access token session runtime starter supported .
- add elastic-search common starter . By @王文贵(Acker)

#### bugfix
- fix wrapped response body serialize issues (fastjson) By @刘海波(Kobe)
- fix open feign service api invoke exception process issues . By @高健(Jake)
- fix message queue client tags filter . By @邵光荣(Glory)
- fix framework startup & shutdown script issues .By @陈志明

#### TODO
- [ ] improve framework template creation .By @周树辉(Carey)
- [ ] Re-defined cloud event notify framework .


--------
### 1.0.0.RC3

#### bugfix

- Fix Security Encryptor defined issues;
- Removed java compile doc plugin temporary;
- Fix cache dependency optional (true) issues;
- Fix fastjson http converter conflict with prometheus plugin

#### Improve

- Improve openfeign service call performance , also set some default configuration(s) for feign client;

#### features

- Add mix cache usage document & samples;
- Add mix security enhance ;
- Add cloud event bus support;
- Add `CompatibleMixmicroException` with custom `ErrorCode` interface;


--------
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

#### Usage

```xml
<dependencyManagement>
   <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-dependencies</artifactId>
            <version>1.0.7.RC1</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>               
    </dependencies>
</dependencyManagement>

```