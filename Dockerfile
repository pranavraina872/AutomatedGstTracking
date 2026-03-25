#  Build Stage
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

#  Runtime Stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/AutomatedGstInvoiceChecker-0.0.1-SNAPSHOT app.jar
RUN mkdir -p /app/Downloads

ENV DOWNLOAD_DIR=/app/Downloads
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
