
# GlassFish / Payara embedded deployment tests

This project contains a simple EAR composed of an EJB and a WAR.

It reproduces the Hibernate deployment error on the following issues:
- GlassFish: [issue 24414](https://github.com/eclipse-ee4j/glassfish/issues/24414)
- Payara: [issue 6248](https://github.com/payara/Payara/issues/6248)

It also reproduces the multiple context initialization error:
- GlassFish: [issue 24415](https://github.com/eclipse-ee4j/glassfish/issues/24415)

## Requirements

- Java Development Kit 17
- Apache Maven 3.9 or later

## How to test Hibernate deployment

By default, the project uses GlassFish 7.0.5 with Hibernate 6.1.7.Final. It also runs on Payara 6.2023.5.

Simply use the command `mvn clean package` and the tests should run successfully.

You'll notice those lines in the output:

```
### [INIT] WAR initialized
### [INIT] WAR-EXT initialized
### [INIT] WAR-EXT initialized
### [INIT] WAR-EXT initialized
### [CREATE] Response: <html><head></head><body>10 leafs created</body></html>
### [QUERY][WAR] Response: <html><head></head><body>Leaf 4</body></html>
### [QUERY][WAR-EXT] Response: <html><head></head><body>Leaf 5</body></html>
### [CLOSE] WAR-EXT destroyed
### [CLOSE] WAR-EXT destroyed
### [CLOSE] WAR-EXT destroyed
### [CLOSE] WAR destroyed
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

## How to test multiple context initialization

One can see the multiple invokations of `WAR-EXT` init and close.

### Context initializaton on GlassFish 7.0.5

To reproduce, launch the following command: 

```sh
mvn clean package -Dtest-context=true  \
                  -Dappserver.version=7.0.5
```

### Context initializaton on Payara 6.2023.5

Actually, this works fine on Payara, since the context methods are only invoked once.
So the following command will work:

```sh
mvn clean package -Dtest-context=true  \
                  -Dappserver.groupId=fish.payara.extras \
                  -Dappserver.artifactId=payara-embedded-all \
                  -Dappserver.version=6.2023.5
```
