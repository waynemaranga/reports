# Stage 1: Build
FROM hseeberger/scala-sbt:17.0.9_1.9.9_2.13.12 as build

WORKDIR /app
COPY ../. .
RUN sbt clean assembly

# Stage 2: Run
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/scala-2.13/*.jar app.jar
COPY ../.env .env
COPY ../application.conf application.conf

CMD ["java", "-jar", "app.jar"]
