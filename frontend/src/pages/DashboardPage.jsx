import React, { useEffect, useState } from "react";
import { getTasks } from "../api/taskApi";

function DashboardPage() {
    const [tasks, setTasks] = useState([]);
    const [token, setToken] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        getTasks()
            .then((res) => {
                console.log("FULL RESPONSE:", res.data);

                setTasks(res.data.data.content || []);
            })
            .catch((err) => {
                console.error(err);
                setTasks([]);
            })
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Loading tasks...</p>;
    if (error) return <p>{error}</p>;


    return (
        <div>
            <h1>Dashboard</h1>
            <p>Your JWT token:</p>
            <code>{token}</code>

            <h2>Tasks</h2>
            {tasks.length === 0 ? (
                <p>No tasks found.</p>
            ) : (
                <ul>
                    {tasks.map((task) => (
                        <li key={task.id}>
                            <strong>{task.name}</strong> - {task.description} ({task.completed ? "Done" : "New"})
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default DashboardPage;