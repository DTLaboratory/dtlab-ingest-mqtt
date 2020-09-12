# DtLab MQTT Ingest

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/25295eb70b8d463486773644f4b1c215)](https://app.codacy.com/gh/SoMind/dtlab-ingest-mqtt?utm_source=github.com&utm_medium=referral&utm_content=SoMind/dtlab-ingest-mqtt&utm_campaign=Badge_Grade_Dashboard)

## UNDER CONSTRUCTION

Listen for data on an MQTT topic and post each message to a remote HTTP service (probably a dtlab-ingest instance).

```console
sbt assembly && MQTT_URL=ssl://YOUR_HOST:8883 MQTT_TOPIC=test/c/ MQTT_CLIENT_ID=YOUR_CLIENT_ID MQTT_USER=YOUR_USER MQTT_PWD=YOUR_PWD java -jar target/scala-2.12/DtLabMqttIngest.jar
```