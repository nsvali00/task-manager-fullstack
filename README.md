# 🚀 Task Manager Fullstack

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED)
![React](https://img.shields.io/badge/React-Frontend-61DAFB)
![JWT](https://img.shields.io/badge/JWT-Authentication-yellow)
![License](https://img.shields.io/badge/License-MIT-green)

## 📖 Overview

Task Manager Fullstack is a production-style full-stack application built to demonstrate modern backend and frontend development practices.

The project features secure JWT authentication, refresh token rotation, role-based security, Dockerized deployment, PostgreSQL persistence, and a modern React frontend.

The goal of this project is to showcase real-world software engineering concepts rather than simple CRUD functionality.

---

## ✨ Features

### 🔐 Authentication & Security

* JWT Access Tokens
* Refresh Tokens
* Refresh Token Rotation
* Refresh Token Reuse Prevention
* Secure Logout
* Password Encryption with BCrypt
* Spring Security Integration
* Protected API Endpoints

### 📋 Task Management

* Create Tasks
* View Tasks
* Update Tasks
* Delete Tasks
* Pagination Support
* Input Validation
* Global Exception Handling

### 🐳 DevOps

* Dockerized Backend
* Dockerized Database
* Docker Compose Setup
* Environment Configuration
* Easy Local Deployment

### 🎨 Frontend

* React + Vite
* Axios API Integration
* Authentication Flow
* Protected Routes
* Responsive UI

---

## 🏗️ Architecture

```text
Frontend (React)
       │
       ▼
Spring Boot REST API
       │
       ▼
Spring Security + JWT
       │
       ▼
PostgreSQL Database
```

---

## 📂 Project Structure

```text
task-manager-fullstack/
│
├── backend/
│   ├── src/
│   ├── Dockerfile
│   ├── pom.xml
│   └── ...
│
├── frontend/
│   ├── src/
│   ├── package.json
│   ├── vite.config.js
│   └── ...
│
├── docker-compose.yml
└── README.md
```

---

## 🛠️ Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* JWT
* Maven

### Frontend

* React
* Vite
* Axios
* React Router

### DevOps

* Docker
* Docker Compose

---

## 🔄 Authentication Flow

```text
User Login
    │
    ▼
Access Token + Refresh Token Issued
    │
    ▼
Access Token Used For API Requests
    │
    ▼
Access Token Expires
    │
    ▼
Refresh Token Endpoint
    │
    ▼
New Access Token + New Refresh Token
```

Refresh tokens are stored and managed securely to support token rotation and reuse prevention.

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

## ⚡ Quick Start

### Clone Repository

```bash
git clone https://github.com/your-username/task-manager-fullstack.git
cd task-manager-fullstack
```

### Start Entire Application

```bash
docker compose up --build
```

### Backend

Available at:

```text
http://localhost:8080
```

### Frontend

Available at:

```text
http://localhost:5173
```

---

## 🧪 Manual Testing Flow

1. Register a new user
2. Login
3. Receive access and refresh tokens
4. Create tasks
5. Update tasks
6. Refresh expired access token
7. Logout
8. Verify protected endpoints reject invalid tokens

---

## 📈 Future Improvements

* Automated Integration Tests
* GitHub Actions CI/CD
* Swagger/OpenAPI Documentation
* Role-Based Authorization Expansion
* Monitoring & Metrics
* Cloud Deployment

---

## 🎯 Key Learning Outcomes

This project demonstrates:

* Secure Authentication Design
* REST API Development
* Spring Security
* JWT Lifecycle Management
* Docker Containerization
* PostgreSQL Integration
* Frontend/Backend Integration
* Production-Oriented Architecture

---

## 👨‍💻 Author

Nikola Svalina

Backend Developer focused on Java, Spring Boot, Security, and DevOps.

GitHub: https://github.com/nsvali00

---

## ⭐ Support

If you found this project useful, consider giving it a star.
