# Simple test Dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY ReRover/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
