FROM openjdk:13-alpine

RUN mkdir -p /app

COPY target/scala-2.13/*.jar /app/

WORKDIR /app

# override CMD from your run command, or k8s yaml, or marathon json, etc...
CMD java -Xms256m -Xmx512m -jar /app/DtlabIngestMqtt.jar

