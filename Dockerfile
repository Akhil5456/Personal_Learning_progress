# Multi-stage build for Spring Boot application
FROM maven:3.9-openjdk-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/tracker-0.0.1-SNAPSHOT.jar app.jar

# Create data directory for H2 database
RUN mkdir -p /app/data

# Expose port (Render uses PORT env var, default 10000)
EXPOSE 10000

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT:-10000}/actuator/health || exit 1

# Run the application with Render's PORT
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-10000}"]

# Add labels for metadata
LABEL maintainer="Personal Learning Tracker"
LABEL version="1.0"
LABEL description="Personal Learning Tracker Spring Boot Application"
