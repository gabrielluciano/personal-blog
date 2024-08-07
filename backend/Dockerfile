FROM maven:3.9.8-eclipse-temurin-21-alpine AS build

WORKDIR /build

COPY pom.xml .

RUN mvn verify --fail-never

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.4_7-jre-alpine

WORKDIR /app

ARG JAR_FILE=/build/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

CMD [ "java", "-jar", "app.jar" ]
