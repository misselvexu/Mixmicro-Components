## Mixmicro+ Framework Kits

Mixmicro Framwork Kit (Utils) For Project(s);

> Kits List

* [x] Common Kits (`String ,Object ,Date ,Class ,Classloader ,Map ,Collection ,Thread , Executor ...Kits`)
* [x] Http Client Kits (`base on apache http client`)
* [x] Servlet Compress Filter
* [x] Jre Http Client Kit (base on `HttpURLConnection`)
* [x] Timer Kit (`HashedWheelTimer` support)
* [x] Spring Context Support
* [x] Executor Kit (`Retriable ,timeout ,AsyncRuntimExecutor` ... Kits)
* [x] Event Bus Kit
* [x] Java Serializable implements With `Hessian`
* [x] big queue base on `Memory Mapped File` .
* [x] File Compression Kit (`zip`,`gz`,`tar`,`7z` ... Kits)
* [x] Retry Kit
* [x] Timed-Globally unique identifiers Kit


> How To Usage

- Add Maven Dependency

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-kits-dependencies</artifactId>
            <version>Antarctica.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

```