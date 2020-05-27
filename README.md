# vertx-server-example
Vert.x server example with Postgres

## Prerequisites

Docker should be installed and running

## Startup

1. Pull Docker Postgres Image
    ```bash
    gradle task pullPostgresImage
    ```
1. Create Docker Postgres Container
    ```bash
    gradle task createPostgresContainer
    ```
1. Start Docker Postgres Container
    ```bash
    gradle task startPostgresContainer
    ```
1. Create tables
    ```bash
    ./gradlew task devdb update
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

Create deployable artifact
```bash
 ./gradlew task shadowJar
```

## Usage

1. Start the application
    ```bash
    java -jar vertx-server-example-1.0-SNAPSHOT-fat.jar
    ```
1. Call example URL http://localhost:8082/postalcodes/55481