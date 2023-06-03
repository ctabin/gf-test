
# GlassFish / Payara embedded deployment tests

This project contains a simple EAR composed of an EJB and a WAR.

It reproduces the Hibernate deployment error on the following issues:
- GlassFish: [issue 24414](https://github.com/eclipse-ee4j/glassfish/issues/24414)
- Payara: [issue 6248](https://github.com/payara/Payara/issues/6248)

## Requirements

- Java Development Kit 17
- Apache Maven 3.9 or later

## How to test

By default, the project uses GlassFish 7.0.5 with Hibernate 6.1.7.Final. It also runs on Payara 6.2023.5.

Simply use the command `mvn clean package` and the tests should run successfully.

You'll notice those lines in the output:

```
### [CREATE] Response: <html><head></head><body>10 leafs created</body></html>
### [QUERY] Response: <html><head></head><body>Leaf 4</body></html>
```

### Hibernate 6.2.x on GlassFish 7.0.5

To reproduce, launch the following command: 

```sh
mvn clean package -Dhibernate.version=6.2.4.Final  \
                  -Dappserver.version=7.0.5
```

### Hibernate 6.2.x on Payara 6.2023.5

To reproduce, launch the following command: 

```sh
mvn clean package -Dhibernate.version=6.2.4.Final \
                  -Dappserver.groupId=fish.payara.extras \
                  -Dappserver.artifactId=payara-embedded-all \
                  -Dappserver.version=6.2023.5
```
