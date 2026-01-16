import React from "react";
import {Button} from "@mui/material";
import useAuth from "../../../../hooks/useAuth.js";
import {useNavigate} from "react-router";
import "./AuthenticationToggle.css";

const AuthenticationToggle = () => {
    const navigate = useNavigate();
    const {isLoggedIn, logout} = useAuth();

    const handleLogin = () => {
        navigate("/login");
    };

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <Button
            color="inherit"
            variant={!isLoggedIn ? "text" : "outlined"}
            onClick={!isLoggedIn ? handleLogin : handleLogout}
            className="auth-toggle-btn"
        >
            {!isLoggedIn ? "Login" : "Logout"}
        </Button>
    );
};

export default AuthenticationToggle;
