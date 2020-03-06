# An internal support project for spring-context in Acmedcare

=====================

Current project that extends `spring-context` is based on Spring Framework 3.2.x +


## Release version

````xml
<dependencies>

    ......

     <!-- Spring Framework -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.framework.version}</version>
    </dependency>

    <!-- Spring Context Extras -->
    <dependency>
        <groupId>com.yunlsp.framework.components</groupId>
        <artifactId>spring-context-support</artifactId>
        <version>1.0.0.BUILD-SNAPSHOT</version>
    </dependency>

    ......

</dependencies>
````

If your project failed to resolve the dependency, try to add the following repository:
```xml
    <repositories>
        <repository>
            <id>sonatype-nexus</id>
            <url>https://oss.sonatype.org/content/repositories/releases</url>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
    </repositories>
```


## Document

TODO: Working in Process