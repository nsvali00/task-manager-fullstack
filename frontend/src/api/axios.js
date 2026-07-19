import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("accessToken");

    console.log("🧠 INTERCEPTOR RUNNING");
    console.log("URL:", config.url);
    console.log("TOKEN:", token);

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    console.log("HEADERS:", config.headers);

    return config;
});

export default api;