// src/api/taskApi.js
import api from "./axios";

export const getTasks = () => api.get("/api/tasks/all");
export const createTask = (task) => api.post("/api/tasks", task);
export const updateTask = (id, task) => api.put(`/api/tasks/${id}`, task);
export const deleteTask = (id) => api.delete(`/api/tasks/${id}`);