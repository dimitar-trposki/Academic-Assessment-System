import React, {useState} from "react";
import {
    Box,
    TextField,
    Button,
    Typography,
    Container,
    Paper,
} from "@mui/material";
import {useNavigate} from "react-router";
import userRepository from "../../../../repository/userRepository.js";
import useAuth from "../../../../hooks/useAuth.js";
import "./Login.css";

const initialFormData = {
    email: "",
    password: "",
};

const Login = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState(initialFormData);
    const [errorMessage, setErrorMessage] = useState("");
    const {login} = useAuth();

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormData({...formData, [name]: value});
        setErrorMessage("");
    };

    const handleSubmit = () => {
        if (!formData.email || !formData.password) {
            setErrorMessage("Please enter both e-mail and password.");
            return;
        }

        userRepository
            .login({
                email: formData.email,
                password: formData.password,
            })
            .then((response) => {
                login(response.data.token);
                setFormData(initialFormData);
                setErrorMessage("");
                navigate("/", {replace: true});
            })
            .catch((error) => {
                console.log(error);
                setErrorMessage("Invalid e-mail or password. Please try again.");
            });
    };

    return (
        <Box className="login-root">
            <Container maxWidth="sm" className="login-container">
                <Paper elevation={3} sx={{padding: 4, mt: 8}} className="login-card">
                    <Typography
                        variant="h5"
                        align="center"
                        gutterBottom
                        className="login-title"
                    >
                        Login
                    </Typography>

                    {errorMessage && (
                        <Typography
                            variant="body2"
                            color="error"
                            align="center"
                            sx={{mb: 2}}
                        >
                            {errorMessage}
                        </Typography>
                    )}

                    <Box>
                        <TextField
                            fullWidth
                            label="E-mail"
                            name="email"
                            margin="normal"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                        <TextField
                            fullWidth
                            label="Password"
                            name="password"
                            type="password"
                            margin="normal"
                            value={formData.password}
                            onChange={handleChange}
                            required
                        />
                        <Button
                            fullWidth
                            variant="contained"
                            type="submit"
                            sx={{mt: 2}}
                            onClick={handleSubmit}
                            className="submit-btn"
                        >
                            Login
                        </Button>
                        <Button
                            fullWidth
                            variant="outlined"
                            type="button"
                            sx={{mt: 2}}
                            onClick={() => navigate("/register")}
                            className="login-secondary-btn"
                        >
                            Register
                        </Button>
                    </Box>
                </Paper>
            </Container>
        </Box>
    );
};

export default Login;
