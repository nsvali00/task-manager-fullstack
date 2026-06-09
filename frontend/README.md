# 🎨 Task Manager Frontend

React frontend for the Task Manager Fullstack application.

The frontend communicates with the Spring Boot backend through a REST API and provides a modern user experience for task management.

---

## ✨ Features

### Authentication

* Login
* Registration
* Logout
* Protected Routes
* JWT Authentication

### Task Management

* View Tasks
* Create Tasks
* Edit Tasks
* Delete Tasks
* Pagination Support

### User Experience

* Responsive Design
* Form Validation
* Error Handling
* Loading States

---

## 🏗️ Architecture

```text
Pages
  │
  ▼
Components
  │
  ▼
Services
  │
  ▼
Axios API Layer
  │
  ▼
Spring Boot Backend
```

---

## 📂 Project Structure

```text
src/

├── api/
├── components/
├── pages/
├── routes/
├── services/
├── App.jsx
└── main.jsx
```

---

## 🛠️ Technology Stack

* React
* Vite
* Axios
* React Router

---

## 🔌 Backend Connection

The frontend communicates with the backend REST API.

Default backend URL:

```text
http://localhost:8080
```

Configure API endpoints in:

```text
src/api/axios.js
```

---

## ▶️ Running Locally

Install dependencies:

```bash
npm install
```

Start development server:

```bash
npm run dev
```

Frontend available at:

```text
http://localhost:5173
```

---

## 📱 Planned Improvements

* Dark Mode
* Advanced Filtering
* Sorting
* User Profile Management
* Dashboard Statistics

---

## 🎯 Purpose

This frontend demonstrates modern React development practices, API integration, routing, authentication handling, and responsive UI design while working together with a secure Spring Boot backend.
