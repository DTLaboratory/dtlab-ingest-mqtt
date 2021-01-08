# DtLab MQTT Ingest

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/25295eb70b8d463486773644f4b1c215)](https://app.codacy.com/gh/SoMind/dtlab-ingest-mqtt?utm_source=github.com&utm_medium=referral&utm_content=SoMind/dtlab-ingest-mqtt&utm_campaign=Badge_Grade_Dashboard)

![alt text](docs/logo_cropped.png)

Listen for data on an MQTT topic and post each message to a remote HTTP service (probably a dtlab-ingest instance).

```console
DTLAB_INGEST_URL=http://localhost/dtlab/ingest:8080 MQTT_URL=ssl://some.iot-server.io:12345 KEYSTORE_PASSWORD=your-password KEYSTORE_PATH=/Users/your/secrets/client.jks sbt run
```

### Build and run

```console
sbt assembly
java -jar target/scala-2.12/DtlabIngestMqtt.jar
```

### Test and Code Coverage

```console
sbt clean coverage test
sbt coverageReport
open target/scala-2.12/scoverage-report/index.html
```

### Code Stats

```console
sbt stats
```
### Check dependencies

```console
sbt dependencyUpdates
```

