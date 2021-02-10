## Integration Tests for MongoDB Connector

### Prerequisites

- Maven 3.6.x
- Java 1.8

### Tested Platforms

- Ubuntu 18.04
- WSO2 EI 6.6.0, WSO2 MI 1.1.0, WSO2 MI 1.2.0

### Steps

1. Download WSO2 EI 6.6.0 from the official website.

2. Copy the wso2ei-6.6.0.zip in to location `<CONNECTOR_HOME>/repository`.

3. Update the `mongodb.properties` file located at `<CONNECTOR_HOME>/src/test/resources/artifacts/ESB/connector/config`.

4. Navigate to `<CONNECTOR_HOME>/` and run the following command.
   ```
   mvn clean install -Dskip-tests=false
   ```
