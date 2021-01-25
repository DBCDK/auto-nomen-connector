auto-nomen-connector
=============

A Java library providing a client to the auto-nomen service

### usage

Add the dependency to your Maven pom.xml

```xml
<dependency>
  <groupId>dk.dbc</groupId>
  <artifactId>auto-nomen-connector</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
In your Java code

```java
import dk.dbc.autonomen.AutoNomenConnector;
import javax.inject.Inject;
...

// Assumes environment variable AUTO_NOM_SERVICE_URL
// is set to the base URL of the work-presentation-service provider service.
@Inject
AutoNomenConnector connector;

// Todo: Add usage example
```

### License

Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3.
See license text in LICENSE.txt