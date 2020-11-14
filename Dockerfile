FROM java:8-alpine

RUN mkdir -p /app

COPY target/scala-2.12/*.jar /app/

WORKDIR /app

# override CMD from your run command, or k8s yaml, or marathon json, etc...
CMD java -Xms256m -Xmx512m -jar /app/DtlabIngestMqtt.jar

