import { useState } from "react";
import { register } from "../services/authService";
import { useNavigate } from "react-router-dom";

export default function RegisterPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();

        try {
            await register({ email, password });
            navigate("/");
        } catch (err) {
            alert("Registration failed");
        }
    };

    return (
        <form onSubmit={handleRegister}>
            <h2>Register</h2>

            <input
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />

            <input
                placeholder="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />

            <button type="submit">Register</button>
        </form>
    );
}