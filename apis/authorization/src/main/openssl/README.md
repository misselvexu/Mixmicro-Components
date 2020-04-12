## 证书创建命令

```bash

## 生成RSA私钥
openssl genrsa -out rsa_private_key.pem 2048

## 生成证书请求文件
openssl req -new -key rsa_private_key.pem -out rsa_cer_req.csr

## 生成证书crt，并设置有效时间为10年
openssl x509 -req -days 3650 -in rsa_cer_req.csr -signkey rsa_private_key.pem -out rsa_cert.crt

## iOS 公钥私钥 （misselvexu）
openssl x509 -outform der -in rsa_cert.crt -out public_key.der
openssl pkcs12 -export -out private_key.p12 -inkey rsa_private_key.pem -in rsa_cert.crt

## Java 公钥
openssl cipher -in rsa_private_key.pem -out rsa_public_key.pem -pubout

## Java 私钥
openssl pkcs8 -topk8 -in rsa_private_key.pem -out pkcs8_private_key.pem -nocrypt

```

## 证书转换命令

```bash

## PEM格式的证书与DER格式的证书的转换

openssl x509 -in cert.pem -inform PEM -out cert.der -outform DER
 
openssl x509 -in ca.cer -inform DER -out ca.pem -outform  PEM

```