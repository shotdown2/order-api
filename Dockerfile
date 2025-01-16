FROM maven:3.8.4-openjdk-17 AS build

COPY pom.xml .
COPY src src

RUN mvn clean package

FROM openjdk:17-jdk-slim

COPY --from=build /target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]