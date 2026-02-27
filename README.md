# Meeting Room Booking System

Full-stack application for booking meeting rooms.

## Project Structure
- `backend/`: Spring Boot application (Java 21)
- `frontend/`: React application (Node.js 18)

## Technologies
- Spring Boot 3.4.3
- Spring Data JPA
- Spring Security (JWT)
- MySQL
- React 18

## Prerequisites
- Docker and Docker Compose
- Java 21 (for local development)
- Node.js 18 (for local development)
- MySQL 8 (for local development)

## Configuration
Database and JWT configurations are located in `backend/src/main/resources/application.properties`.

## Running Locally

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm start
```

- Backend API: `http://localhost:8080`
- Frontend UI: `http://localhost:3000`

## Getting Started with Docker

To run the entire application using Docker Compose:

```bash
docker-compose up --build
```

## Testing the Application

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```
