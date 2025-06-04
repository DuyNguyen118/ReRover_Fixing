# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY ReRover/pom.xml .
COPY ReRover/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Set timezone to Asia/Ho_Chi_Minh
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Install MySQL client for healthcheck
RUN apt-get update && apt-get install -y mysql-client && rm -rf /var/lib/apt/lists/*

# Copy the JAR file from the build stage
COPY --from=build /app/target/ReRover-0.0.1-SNAPSHOT.jar app.jar

# Environment variables with default values for local development
ENV SPRING_PROFILES_ACTIVE=production
ENV DB_HOST=localhost
ENV DB_PORT=3306
ENV DB_NAME=rerover
ENV DB_USERNAME=nvbduy
ENV DB_PASSWORD=nvbduy18
ENV PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD mysqladmin ping -h $DB_HOST -u $DB_USERNAME -p$DB_PASSWORD || exit 1

# Expose the port the app runs on
EXPOSE $PORT

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
