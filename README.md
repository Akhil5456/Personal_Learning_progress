# Personal Learning Tracker

A Spring Boot application that lets you track study sessions, subjects, topics and goals.

## Features
- Record study sessions with duration, remarks and completion status.
- Manage subjects and topics, mark topics as *Completed* or *Pending*.
- Dashboard showing total subjects, total topics, overall completion percentage and completed sessions.
- Reports page summarising total study minutes and completed sessions.
- Goal management page that displays progress for each subject.

## Prerequisites
- Java 17 (or higher)
- Maven 3.8+
- H2 in‑memory database (configured out‑of‑the‑box)

## Running locally
```bash
# Clone the repository (once it is pushed)
git clone https://github.com/Akhil5456/Personal_Learning_progress.git
cd Personal_Learning_progress

# Build and run the Spring Boot app
mvn spring-boot:run
```
The application will start on **http://localhost:8082**.

## Project structure
```
src/main/java/com/learning/tracker/   # Java source code
src/main/resources/                     # Thymeleaf templates and static assets
pom.xml                                  # Maven build file
README.md                                 # This file
```

## License
This project is licensed under the MIT License – see the `LICENSE` file.
