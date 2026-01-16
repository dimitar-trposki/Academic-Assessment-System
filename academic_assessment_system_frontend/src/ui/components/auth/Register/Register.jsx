import React, {useState} from "react";
import {
    Box,
    TextField,
    Button,
    Typography,
    Container,
    Paper,
} from "@mui/material";
import userRepository from "../../../../repository/userRepository.js";
import {useNavigate} from "react-router";
import "./Register.css";

const initialFormData = {
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
};

const Register = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState(initialFormData);
    const [fieldErrors, setFieldErrors] = useState({});
    const [generalError, setGeneralError] = useState("");

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormData({...formData, [name]: value});
        setFieldErrors((prev) => ({...prev, [name]: ""}));
        setGeneralError("");
    };

    const validate = () => {
        const errors = {};

        if (!formData.firstName) {
            errors.firstName = "First name is required.";
        }
        if (!formData.lastName) {
            errors.lastName = "Last name is required.";
        }
        if (!formData.email) {
            errors.email = "E-mail is required.";
        }

        if (!formData.password) {
            errors.password = "Password is required.";
        } else if (formData.password.length < 6 || formData.password.length > 20) {
            errors.password = "Password must be between 6 and 20 characters.";
        }

        if (!formData.confirmPassword) {
            errors.confirmPassword = "Please confirm your password.";
        } else if (formData.password !== formData.confirmPassword) {
            errors.confirmPassword = "Passwords do not match.";
        }

        setFieldErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleSubmit = () => {
        if (!validate()) {
            return;
        }

        const payload = {
            firstName: formData.firstName,
            lastName: formData.lastName,
            email: formData.email,
            password: formData.password,
            userRole: "USER",
        };

        userRepository
            .register(payload)
            .then(() => {
                setFormData(initialFormData);
                setFieldErrors({});
                setGeneralError("");
                navigate("/login", {replace: true});
            })
            .catch((error) => {
                console.log(error);
                setGeneralError("Registration failed. Please try again.");
            });
    };

    return (
        <Box className="register-root">
            <Container maxWidth="sm" className="register-container">
                <Paper elevation={3} sx={{padding: 4, mt: 8}} className="register-card">
                    <Typography
                        variant="h5"
                        align="center"
                        gutterBottom
                        className="register-title"
                    >
                        Register
                    </Typography>

                    <Typography
                        variant="body2"
                        color="text.secondary"
                        align="center"
                        sx={{mb: 2}}
                    >
                        Password must be between 6 and 20 characters.
                    </Typography>

                    {generalError && (
                        <Typography
                            variant="body2"
                            color="error"
                            align="center"
                            sx={{mb: 2}}
                        >
                            {generalError}
                        </Typography>
                    )}

                    <Box>
                        <TextField
                            fullWidth
                            label="First name"
                            name="firstName"
                            margin="normal"
                            value={formData.firstName}
                            onChange={handleChange}
                            required
                            error={Boolean(fieldErrors.firstName)}
                            helperText={fieldErrors.firstName}
                        />
                        <TextField
                            fullWidth
                            label="Last name"
                            name="lastName"
                            margin="normal"
                            value={formData.lastName}
                            onChange={handleChange}
                            required
                            error={Boolean(fieldErrors.lastName)}
                            helperText={fieldErrors.lastName}
                        />
                        <TextField
                            fullWidth
                            label="E-mail"
                            name="email"
                            type="email"
                            margin="normal"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            error={Boolean(fieldErrors.email)}
                            helperText={fieldErrors.email}
                        />
                        <TextField
                            fullWidth
                            label="Password"
                            name="password"
                            type="password"
                            value={formData.password}
                            onChange={handleChange}
                            margin="normal"
                            required
                            error={Boolean(fieldErrors.password)}
                            helperText={
                                fieldErrors.password ||
                                "6–20 characters, choose a strong password."
                            }
                        />
                        <TextField
                            fullWidth
                            label="Confirm password"
                            name="confirmPassword"
                            type="password"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            margin="normal"
                            required
                            error={Boolean(fieldErrors.confirmPassword)}
                            helperText={fieldErrors.confirmPassword}
                        />
                        <Button
                            fullWidth
                            variant="contained"
                            type="submit"
                            sx={{mt: 2}}
                            onClick={handleSubmit}
                            className="register-submit-btn"
                        >
                            Register
                        </Button>
                    </Box>
                </Paper>
            </Container>
        </Box>
    );
};

export default Register;
