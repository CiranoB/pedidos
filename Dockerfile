FROM maven:3.8.4-openjdk-21-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /app/target/pedidos-1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]