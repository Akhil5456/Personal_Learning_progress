# Personal Learning Tracker

A Spring Boot web application for tracking personal learning progress, study sessions, and goals.

## Features

- User authentication and authorization
- Subject and topic management
- Study session tracking
- Progress reporting and analytics
- Goal setting and monitoring
- Responsive web interface

## Tech Stack

- **Backend**: Spring Boot 3.2.4
- **Database**: H2 (in-memory)
- **Frontend**: Thymeleaf, HTML5, CSS3
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher

## Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/PersonalLearrningTracker.git
   cd PersonalLearrningTracker
   ```

2. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Access the application**
   - Open your browser and go to `http://localhost:8080`
   - Register a new account or login with existing credentials

## Project Structure

```
src/
├── main/
│   ├── java/com/learning/tracker/
│   │   ├── controller/     # REST controllers
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── TrackerApplication.java
│   └── resources/
│       ├── static/
│       │   └── css/        # Stylesheets
│       ├── templates/      # Thymeleaf templates
│       └── application.properties
└── test/                   # Unit tests
```

## API Endpoints

- `GET /` - Home page
- `GET /login` - Login page
- `GET /register` - Registration page
- `GET /dashboard` - User dashboard
- `GET /subjects` - Subject management
- `GET /study-sessions` - Study session tracking
- `GET /reports` - Progress reports

## Database

The application uses H2 in-memory database by default. The database schema is automatically created on application startup.

### Accessing H2 Console

1. Start the application
2. Go to `http://localhost:8080/h2-console`
3. Use these credentials:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

## Configuration

Key configuration properties in `src/main/resources/application.properties`:

```properties
# Server port
server.port=8080

# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Thymeleaf
spring.thymeleaf.cache=false
```

## Testing

Run the test suite:
```bash
mvn test
```

## Building for Production

Create a production-ready JAR file:
```bash
mvn clean package
```

The executable JAR will be created in the `target/` directory.

Run the production JAR:
```bash
java -jar target/tracker-0.0.1-SNAPSHOT.jar
```

## Deployment

### Using Docker (Optional)

1. Create a Dockerfile:
   ```dockerfile
   FROM openjdk:17-jdk-slim
   COPY target/tracker-0.0.1-SNAPSHOT.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

2. Build and run:
   ```bash
   docker build -t personal-learning-tracker .
   docker run -p 8080:8080 personal-learning-tracker
   ```

### Heroku Deployment

1. Install Heroku CLI
2. Create Heroku app:
   ```bash
   heroku create your-app-name
   ```
3. Deploy:
   ```bash
   git push heroku main
   ```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have questions, please:
1. Check the existing issues on GitHub
2. Create a new issue with detailed information
3. Include steps to reproduce any bugs

## Future Enhancements

- [ ] PostgreSQL/MySQL database support
- [ ] REST API for mobile apps
- [ ] Email notifications
- [ ] File attachments for study materials
- [ ] Calendar integration
- [ ] Advanced analytics and charts
- [ ] Multi-language support
