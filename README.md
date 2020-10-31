## Mix Micro-service Components

> A micro-services sample project.

### Overview

>

### Requirements

YunLSP+ Maven Repository Configuration

> [Configuration Reference](https://github.com/misselvexu/Acmedcare-Maven-Nexus/blob/master/README.md)

### Quick Start

You can download binaries from [Release Repo](http://git.hgj.net/elve.xu/Mixmicro-Components) or [repo.hgj.net](http://nexus.hgj.net/).

*First* : unzip release package

```bash
$ tar -zxvf *.tar.gz
```

*Second* : startup & shutdown

```bash
$ sh ./bin/startup.sh -p production  
```

*Third* : check the application log

```bash
$ tail -f logs/start.log
```

> param: -p (optional): `production` | `dev`


### Building from Source

You don’t need to build from source to use `Mix Micro Service Components` (binaries in [repo.hgj.net](http://nexus.hgj.net)), 
but if you want to try out the latest and greatest, 
`Mix Micro Service Components` can be easily built with the maven wrapper. You also need JDK 1.8.

*First* : git clone source from gitlab
 
```bash
$ git clone http://git.hgj.net/elve.xu/Mixmicro-Components.git
```

*Second* : build

```bash
$ mvn clean install
```

If you want to build with the regular `mvn` command, you will need [Maven v3.5.0 or above](https://maven.apache.org/run-maven/index.html).


### Document

#### How to Use

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


### License
 
```
                   GNU LESSER GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.

```
