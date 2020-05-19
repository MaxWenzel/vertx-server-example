# vertx-server-example
Vert.x server example with Postgres

## Prerequisites

Docker should be installed and running

## Startup

1. Create tables
```bash
gradle task devdb update
```

## Building

To launch your tests:
```bash
./gradlew clean test
```

To package your application:
```bash
./gradlew clean assemble
```

To run your application:
```bash
./gradlew clean run
```