# 🧩 Task Management API

![Banner](https://via.placeholder.com/1200x300.png?text=Task+Management+API+Spring+Boot+JWT+Docker)

---

## 🏷️ Tech Stack

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?logo=postgresql)
![JWT](https://img.shields.io/badge/Auth-JWT-orange)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue?logo=docker)
![Build](https://img.shields.io/badge/Build-Maven-red)
![Status](https://img.shields.io/badge/Status-Production--Ready-success)

---

## 📌 Overview

A production-style backend system for task management built using **Spring Boot**.  
It demonstrates modern backend engineering practices including **JWT authentication, refresh token handling, pagination, and full Dockerization**.

Designed as a **portfolio-ready backend project** that mimics real-world API architecture.

---

## 🎯 Key Features

- 🔐 Secure authentication (JWT Access + Refresh Tokens)
- 👤 User registration & login system
- 📋 Task CRUD operations
- 📊 Pagination support for scalable data access
- 🛡️ Spring Security integration
- ⚙️ Global exception handling
- 🐳 Dockerized application (App + PostgreSQL)
- 🧱 Clean layered architecture

---

## 🧠 Architecture


Client (Postman / Frontend)
↓
REST Controllers
↓
Service Layer (Business Logic)
↓
Repository Layer (JPA / Hibernate)
↓
PostgreSQL (Docker Container)

JWT Filter secures protected endpoints


---

## 🔐 Authentication Flow

1. User registers
2. User logs in
3. System returns:
   - Access Token (short-lived)
   - Refresh Token (long-lived)
4. Access token used for API requests
5. Refresh token used to generate new access tokens
6. Fully stateless authentication (no sessions)

---

## 🐳 Running the Project

### 📦 Clone Repository

git clone https://github.com/your-username/task-api.git
cd task-api
🚀 Start with Docker
docker compose up --build
🌐 Access Application
http://localhost:8080
⚙️ Environment Variables
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/taskdb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

JWT_SECRET=your_secret_key
JWT_EXPIRATION=3600000
📡 API Endpoints
🔐 Auth
POST /auth/register
POST /auth/login
POST /auth/refresh
📋 Tasks (Protected)
GET    /tasks
POST   /tasks
PUT    /tasks/{id}
DELETE /tasks/{id}
📄 Pagination Example
GET /tasks?page=0&size=10
📸 Screenshots

Add real screenshots from Postman or Swagger here

🔑 Login Request

📋 Tasks Response

🔐 JWT Flow

🐳 Docker Architecture
app → Spring Boot backend
db → PostgreSQL database
🔥 Engineering Highlights

This project demonstrates:

Real-world backend API design
Secure authentication flow (JWT + refresh tokens)
Stateless architecture design
Scalable pagination system
Containerized deployment (Docker)
Clean separation of concerns
Production-style configuration management
📈 Future Improvements
Role-based access control (USER / ADMIN)
Swagger / OpenAPI integration
Unit & integration testing (JUnit + Testcontainers)
CI/CD pipeline (GitHub Actions)
Cloud deployment (AWS / Render / Fly.io)
Observability (logging + metrics)
👨‍💻 Author

Nikola Svalina

⭐ If you like this project

Give it a ⭐ and explore improvements!