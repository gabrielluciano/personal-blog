FROM maven:3.8.6-eclipse-temurin-17-alpine AS buildstage

WORKDIR /app

COPY . .

RUN mvn package

FROM eclipse-temurin:17-jre-alpine AS runstage

COPY --from=buildstage /app/target/blog-2.0.0.jar .

ENTRYPOINT [ "java", "-jar", "blog-2.0.0.jar" ]
