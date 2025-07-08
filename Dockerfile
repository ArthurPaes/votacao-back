FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/api.jar ./api.jar

EXPOSE 8080

CMD ["java", "-jar", "api.jar"]
