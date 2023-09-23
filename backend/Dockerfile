FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT [ "java", "-XX:+UseSerialGC", "-XX:MaxRAM=128m", "-jar", "app.jar" ]
