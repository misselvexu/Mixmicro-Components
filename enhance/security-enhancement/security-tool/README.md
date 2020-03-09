## How to use security tool

### How to build 

```bash

git clone ssh://git@git.hgj.net:8022/mixmicro/Mixmicro-Components.git

cd Mixmicro-Components/enhance/security-enhancement/security-tool

mvn clean package -DskipTests=true

```

### Quick Start

```bash

usage: java -jar encrypt.jar [-e] -key <KEY> -text <TEXT>
java -jar encrypt.jar --help
Options:
 -e,--encrypt               encrypt input with assigned key
 -key,--encryptkey <KEY>    Master Password used for Encryption/Decryption.
 -text,--plaintext <TEXT>   Content to be encrypted.


© 2020 YunLSP+, Inc

```

### Samples

- Encrypt plaintext with password key

```bash

# Shell Command

java -jar encrypt.jar -e -key password -text demo


# Response

===================Encrypt Result===================
    Plain Text     : demo
    Cipher Text    : s9CL9ENiw4sfT7qwrNWHIuDmNLgXhFgrW7HtQjBwZBZj9onjgnixRbpVVnwW9MZX

Tips: Please do not reveal your key & Ciphertext !
```

