## Mixmicro SPI Components 

### Usage

- Add Maven Dependency

```xml

<dependency>
    <groupId>com.yunlsp.framework.components</groupId>
    <artifactId>mixmicro-components-starter-spi</artifactId>
</dependency>

```

- Defined Extensible Interface Or Abstract Class With `@MixmicroExtensible` Annotation

```java

package com.demo;

@MixmicroExtensible
public interface DemoApi {}

```

- Defined Extension Implements Class With `@MixmicroExtension` Annotation

```java

package com.demo;

@MixmicroExtension("default-demo-api")
public class DefaultDemoApi implements DemoApi {}

```

- Create new SPI service file with name `com.demo.DemoApi` under dir  `src/main/resources/META-INF/mixmicro/services`

> Content

```properties

default-demo-api=com.demo.DefaultDemoApi

```

- Use with Api

```java

## Get Extension Instance with `MixmicroExtensionLoaderFactory`

DemoApi demoApi = MixmicroExtensionLoaderFactory.getExtensionLoader(DemoApi.class).getExtension("default-demo-api");

// ....

```

### Integrate With Spring Framework

> editing ...