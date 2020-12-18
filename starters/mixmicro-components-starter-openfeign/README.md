## Mixmicro Spring CLoud OpenFeign Guide

## Features

---
### Service Call Service Over Openfeign With `Request Headers`

#### **First step** Upgrade `Components Framework Version`

> Minimum version `1.0.5.RC5`


Maven Management Dependency

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.yunlsp.framework.components</groupId>
            <artifactId>mixmicro-components-dependencies</artifactId>
            <version>1.0.6.RC2</version>
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