# Multi-stage build for Spring Boot application
FROM maven:3.9-openjdk-21 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:21-jre-slim

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/tracker-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Railway uses PORT env var)
EXPOSE 8080

# Add health check (simplified)
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1

# Run the application with Railway's PORT
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]

# Add labels for metadata
LABEL maintainer="Personal Learning Tracker"
LABEL version="1.0"
LABEL description="Personal Learning Tracker Spring Boot Application"
