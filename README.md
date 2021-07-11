# Web Form Consumer

Easy Backend Application for consuming http-posts and storing them as files.

## How to run...

Run java 16 application.

` java -jar web-form-consumer-<VERSION>.jar

It will be (very) good to use a `application.properties` with key-values as start parameters.  
For example:

    server.port=8444
    
    # SSL Config
    server.ssl.key-store=keystore.p12
    server.ssl.key-store-password=<YOUR_PASSWORD>
    # JKS or PKCS12
    server.ssl.store-type=pkcs12

    allowedOrigins=*
    dir=data

For creating a keystore, as above the file `keystore.p12` run the following line:

    keytool -genkeypair -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650

_The keystore has to be created on the computer where the app is running._  
_I did look it up here: https://mkyong.com/spring-boot/spring-boot-ssl-https-examples/_  
_Access page in chrome due to unsafe ssl
certificate: https://www.technipages.com/google-chrome-bypass-your-connection-is-not-private-message_

_Enjoy!_

## Versions

- 1.0 initial implementation
  

