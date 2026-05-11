# Deployment Guide

This document provides comprehensive instructions for deploying the Personal Learning Tracker application to various platforms.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Local Deployment](#local-deployment)
3. [Docker Deployment](#docker-deployment)
4. [Cloud Platform Deployment](#cloud-platform-deployment)
   - [Heroku](#heroku)
   - [AWS Elastic Beanstalk](#aws-elastic-beanstalk)
   - [Google Cloud Platform](#google-cloud-platform)
   - [DigitalOcean](#digitalocean)
5. [Environment Configuration](#environment-configuration)
6. [Database Setup](#database-setup)
7. [Monitoring and Logging](#monitoring-and-logging)
8. [Security Considerations](#security-considerations)

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- Git
- Docker (for containerized deployment)
- Cloud platform account (for cloud deployment)

## Local Deployment

### Development Mode

```bash
# Clone the repository
git clone https://github.com/your-username/PersonalLearrningTracker.git
cd PersonalLearrningTracker

# Run in development mode
mvn spring-boot:run
```

### Production Mode

```bash
# Build the application
mvn clean package -DskipTests

# Run with production profile
java -jar target/tracker-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Docker Deployment

### Building the Docker Image

```bash
# Build the image
docker build -t personal-learning-tracker .

# Run the container
docker run -d \
  --name learning-tracker \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  personal-learning-tracker
```

### Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    volumes:
      - ./data:/app/data
    restart: unless-stopped
    
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: tracker_db
      POSTGRES_USER: tracker_user
      POSTGRES_PASSWORD: secure_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    restart: unless-stopped

volumes:
  postgres_data:
```

Run with Docker Compose:

```bash
docker-compose up -d
```

## Cloud Platform Deployment

### Heroku

1. **Install Heroku CLI**
   ```bash
   # macOS
   brew tap heroku/brew && brew install heroku
   
   # Windows
   # Download from https://devcenter.heroku.com/articles/heroku-cli
   ```

2. **Login to Heroku**
   ```bash
   heroku login
   ```

3. **Create Heroku App**
   ```bash
   heroku create your-app-name
   ```

4. **Add PostgreSQL Database**
   ```bash
   heroku addons:create heroku-postgresql:hobby-dev
   ```

5. **Set Environment Variables**
   ```bash
   heroku config:set SPRING_PROFILES_ACTIVE=prod
   heroku config:set DATABASE_URL=$(heroku config:get DATABASE_URL)
   ```

6. **Deploy**
   ```bash
   git push heroku main
   ```

7. **Open the App**
   ```bash
   heroku open
   ```

### AWS Elastic Beanstalk

1. **Install AWS CLI**
   ```bash
   pip install awscli
   aws configure
   ```

2. **Create Application Bundle**
   ```bash
   mvn clean package
   zip -r deployment.zip target/tracker-0.0.1-SNAPSHOT.jar .ebextensions/
   ```

3. **Create EB Application**
   ```bash
   eb init learning-tracker
   eb create production
   ```

4. **Deploy**
   ```bash
   eb deploy
   ```

### Google Cloud Platform

1. **Install Google Cloud SDK**
   ```bash
   # Follow instructions at https://cloud.google.com/sdk/docs/install
   ```

2. **Enable Cloud Build API**
   ```bash
   gcloud services enable cloudbuild.googleapis.com
   ```

3. **Create App Engine App**
   ```bash
   gcloud app create
   ```

4. **Deploy**
   ```bash
   gcloud app deploy
   ```

### DigitalOcean

1. **Create Droplet**
   - Choose Ubuntu 22.04
   - Select appropriate size
   - Add SSH keys

2. **Setup Server**
   ```bash
   # SSH into droplet
   ssh root@your-droplet-ip
   
   # Update system
   apt update && apt upgrade -y
   
   # Install Java 17
   apt install openjdk-17-jdk -y
   
   # Install Maven
   apt install maven -y
   
   # Clone repository
   git clone https://github.com/your-username/PersonalLearrningTracker.git
   cd PersonalLearrningTracker
   ```

3. **Run Application**
   ```bash
   # Build and run
   mvn clean package -DskipTests
   java -jar target/tracker-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &
   ```

4. **Setup Systemd Service**
   ```bash
   # Create service file
   nano /etc/systemd/system/learning-tracker.service
   ```

   Content:
   ```ini
   [Unit]
   Description=Personal Learning Tracker
   After=network.target

   [Service]
   User=root
   WorkingDirectory=/root/PersonalLearrningTracker
   ExecStart=/usr/bin/java -jar target/tracker-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   Restart=always

   [Install]
   WantedBy=multi-user.target
   ```

   ```bash
   # Enable and start service
   systemctl enable learning-tracker
   systemctl start learning-tracker
   ```

## Environment Configuration

### Environment Variables

Create `.env` file for production:

```bash
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/tracker_db
DATABASE_USERNAME=tracker_user
DATABASE_PASSWORD=secure_password

# Application Configuration
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080

# Security
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION=86400000

# Email (if using email features)
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### Profile-Specific Configuration

- `application.properties` - Development settings
- `application-prod.properties` - Production settings
- `application-test.properties` - Test settings

## Database Setup

### PostgreSQL (Recommended for Production)

1. **Install PostgreSQL**
   ```bash
   # Ubuntu/Debian
   apt install postgresql postgresql-contrib
   
   # macOS
   brew install postgresql
   brew services start postgresql
   ```

2. **Create Database and User**
   ```sql
   CREATE DATABASE tracker_db;
   CREATE USER tracker_user WITH PASSWORD 'secure_password';
   GRANT ALL PRIVILEGES ON DATABASE tracker_db TO tracker_user;
   ```

3. **Update Configuration**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/tracker_db
   spring.datasource.username=tracker_user
   spring.datasource.password=secure_password
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   ```

### MySQL Alternative

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tracker_db
spring.datasource.username=tracker_user
spring.datasource.password=secure_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

## Monitoring and Logging

### Application Monitoring

Add these dependencies to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

### Health Check Endpoints

- `/actuator/health` - Application health
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

### Log Management

Configure logging in `application-prod.properties`:

```properties
# File logging
logging.file.name=logs/application.log
logging.logback.rollingpolicy.max-file-size=10MB
logging.logback.rollingpolicy.max-history=30

# Log levels
logging.level.root=WARN
logging.level.com.learning.tracker=INFO
```

## Security Considerations

### SSL/TLS Configuration

```properties
# Enable HTTPS
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your-keystore-password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
```

### Security Headers

```properties
# Security headers
security.headers.frame-options=DENY
security.headers.content-type-options=nosniff
security.headers.xss-protection=1; mode=block
security.headers.referrer-policy=strict-origin-when-cross-origin
```

### Database Security

- Use strong passwords
- Limit database user permissions
- Enable SSL connections
- Regular backups

### Application Security

- Keep dependencies updated
- Use environment variables for secrets
- Enable CSRF protection
- Implement rate limiting
- Regular security audits

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Find and kill process
   netstat -tulpn | grep :8080
   kill -9 <PID>
   ```

2. **Database Connection Issues**
   - Check database is running
   - Verify connection string
   - Check firewall settings

3. **Memory Issues**
   ```bash
   # Increase heap size
   java -Xmx2g -jar target/tracker-0.0.1-SNAPSHOT.jar
   ```

### Health Checks

```bash
# Check if application is running
curl -f http://localhost:8080/actuator/health

# Check logs
tail -f logs/application.log
```

## Backup and Recovery

### Database Backup

```bash
# PostgreSQL
pg_dump tracker_db > backup.sql

# Restore
psql tracker_db < backup.sql
```

### Application Backup

```bash
# Backup entire application
tar -czf learning-tracker-backup.tar.gz \
  target/tracker-0.0.1-SNAPSHOT.jar \
  data/ \
  logs/ \
  application-prod.properties
```

## Performance Optimization

### JVM Tuning

```bash
java -Xms512m -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar target/tracker-0.0.1-SNAPSHOT.jar
```

### Database Optimization

- Add indexes to frequently queried columns
- Use connection pooling
- Enable query caching
- Monitor slow queries

### Caching

```properties
# Enable caching
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
```

## Support and Maintenance

### Regular Maintenance Tasks

1. Update dependencies
2. Monitor application logs
3. Check disk space
4. Update SSL certificates
5. Backup database
6. Performance monitoring

### Getting Help

- Check application logs
- Review GitHub issues
- Consult Spring Boot documentation
- Contact support team

---

**Note**: Always test deployment procedures in a staging environment before applying them to production.
