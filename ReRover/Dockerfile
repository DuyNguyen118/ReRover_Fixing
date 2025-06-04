# Stage 1: Build the application
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy only the files needed for Maven to download dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
