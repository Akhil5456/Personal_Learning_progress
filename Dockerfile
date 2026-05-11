# Stage 1: Build the application with Maven
FROM eclipse-temurin:17-jdk-slim AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml first for dependency caching
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw && ./mvnw dependency:resolve -B || true

# Copy source code and build
COPY src ./src
RUN if [ -f mvnw ]; then ./mvnw package -B -DskipTests; else apt-get update && apt-get install -y maven && mvn package -B -DskipTests; fi

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/tracker-0.0.1-SNAPSHOT.jar app.jar

# Railway provides PORT dynamically
ENV PORT=8080
EXPOSE ${PORT}

# Run the application using Railway's PORT
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]
