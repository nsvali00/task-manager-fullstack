# 🧩 Task Management API (Spring Boot + JWT + Docker)

A production-style backend REST API for task management built with Spring Boot.  
The project demonstrates secure authentication (JWT access + refresh tokens), pagination, and fully containerized deployment using Docker and PostgreSQL.

---

## 🚀 Features

- User registration and login system
- JWT authentication (Access + Refresh tokens)
- Secure endpoints using Spring Security
- Task CRUD operations (Create, Read, Update, Delete)
- Pagination and scalable data fetching
- Global exception handling
- Dockerized backend + PostgreSQL database
- Stateless authentication design
- Clean layered architecture (Controller → Service → Repository)

---

## 🏗️ Tech Stack

- Java 21+
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA (Hibernate)
- PostgreSQL
- Docker & Docker Compose
- Maven

---

## 📦 Architecture Overview
Client (Postman / Frontend)
↓
REST API (Spring Boot Controllers)
↓
Service Layer (Business Logic)
↓
Repository Layer (JPA / Hibernate)
↓
PostgreSQL Database (Docker Container)

JWT Filter secures all protected endpoints


---

## 🔐 Authentication Flow

1. User registers an account
2. User logs in with credentials
3. Server returns:
    - Access Token (short-lived)
    - Refresh Token (long-lived)
4. Access token is used for protected endpoints
5. When access token expires, refresh token is used to generate a new one
6. No session storage (fully stateless authentication)

---

## 🐳 Running the Project (Docker)

### 1. Clone repository

git clone https://github.com/your-username/task-api.git
cd task-api

### 2. Start application
docker compose up --build
### 3. Access application
http://localhost:8080
⚙️ Environment Configuration

These values are configured in docker-compose.yml or .env:

SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/taskdb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

JWT_SECRET=your_secret_key
JWT_EXPIRATION=3600000
📌 API Endpoints
🔐 Authentication
POST /auth/register   → Register new user
POST /auth/login      → Login and receive tokens
POST /auth/refresh    → Refresh access token
📝 Tasks (Protected)
GET    /tasks         → Get all tasks (paginated)
POST   /tasks         → Create task
PUT    /tasks/{id}    → Update task
DELETE /tasks/{id}    → Delete task

All /tasks endpoints require a valid JWT access token.

📄 Pagination Example
GET /tasks?page=0&size=10

Response:

paginated task list
total pages
total elements
🧪 Testing the API

You can test the system using:

Postman (recommended)
curl commands
Swagger UI (if added later)
🐳 Docker Services

The application runs with two containers:

app → Spring Boot backend
db → PostgreSQL database
🔥 What This Project Demonstrates

This project shows practical backend engineering skills:

REST API design principles
Authentication & authorization (JWT)
Token refresh strategy (production-style security)
Pagination for scalable APIs
Dockerized microservice-like setup
Clean layered architecture
Real-world backend workflow
📈 Future Improvements
Role-based access control (USER / ADMIN)
Swagger / OpenAPI documentation
Unit and integration tests (JUnit, Testcontainers)
CI/CD pipeline (GitHub Actions)
Cloud deployment (Render / AWS / Fly.io)
Audit logging and monitoring

Author

Nikola Svalina

⭐ If you like this project

Feel free to star the repository and explore improvements!