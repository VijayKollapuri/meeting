# Meeting Room Booking - Backend

Spring Boot backend for the Meeting Room Booking System.

## Technologies
- Java 21
- Spring Boot 3.4.3
- Spring Data JPA
- Spring Security (JWT)
- H2 Database

## Configuration
Database and JWT configurations are located in `src/main/resources/application.properties`.

## Running Locally
```bash
./mvnw spring-boot:run
```

## Running with Docker
```bash
docker build -t meeting-room-backend .
docker run -p 8080:8080 meeting-room-backend
```
