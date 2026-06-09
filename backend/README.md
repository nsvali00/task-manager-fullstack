# 🚀 Task Manager Backend

Spring Boot REST API powering the Task Manager Fullstack application.

## ✨ Features

### Authentication & Security

* JWT Access Tokens
* Refresh Tokens
* Refresh Token Rotation
* Refresh Token Reuse Prevention
* Secure Logout
* BCrypt Password Encryption
* Spring Security

### Task Management

* Create Tasks
* Read Tasks
* Update Tasks
* Delete Tasks
* Pagination
* Validation
* Global Exception Handling

### Persistence

* PostgreSQL
* Spring Data JPA
* Hibernate

### DevOps

* Docker Support
* Docker Compose Integration
* Environment-Based Configuration

---

## 🏗️ Architecture

```text
Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
PostgreSQL
```

---

## 📂 Package Structure

```text
src/main/java/com/example/taskmanager

├── auth
├── common
├── config
├── security
├── task
└── user
```

---

## 🛠️ Technology Stack

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA
* PostgreSQL
* JWT
* Maven
* Docker

---

## 🔐 Authentication Flow

1. User logs in.
2. Access token and refresh token are issued.
3. Access token secures API requests.
4. Refresh token generates new access tokens.
5. Refresh token rotation prevents token reuse.

---

## 📡 API Endpoints

### Authentication

| Method | Endpoint       |
| ------ | -------------- |
| POST   | /auth/register |
| POST   | /auth/login    |
| POST   | /auth/refresh  |
| POST   | /auth/logout   |

### Tasks

| Method | Endpoint    |
| ------ | ----------- |
| GET    | /tasks      |
| GET    | /tasks/{id} |
| POST   | /tasks      |
| PUT    | /tasks/{id} |
| DELETE | /tasks/{id} |

---

## ▶️ Running Locally

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🐳 Running With Docker

```bash
docker compose up --build
```

---

## 🎯 Purpose

This project was built to demonstrate production-style backend development using Spring Boot, secure authentication, REST API design, and containerized deployment.
