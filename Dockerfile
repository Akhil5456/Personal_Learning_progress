# Use a lightweight Java 17 runtime environment
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/tracker-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Add labels for metadata
LABEL maintainer="your-email@example.com"
LABEL version="1.0"
LABEL description="Personal Learning Tracker Spring Boot Application"
