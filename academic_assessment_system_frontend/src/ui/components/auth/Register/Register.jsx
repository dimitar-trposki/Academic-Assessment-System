import React, {useState} from 'react';
import {
    Box, TextField, Button, Typography, Container, Paper
} from '@mui/material';
import userRepository from "../../../../repository/userRepository.js";
import {useNavigate} from "react-router";

const initialFormData = {
    "firstName": "",
    "lastName": "",
    "email": "",
    "password": "",
    "confirmPassword": "",
};

const Register = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState(initialFormData);

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormData({...formData, [name]: value});
    };

    const handleSubmit = () => {
        if (formData.password !== formData.confirmPassword) {
            alert("Passwords do not match.");
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
                console.log("The user is registered successfully!");
                setFormData(initialFormData);
                navigate("/login", {replace: true});
            })
            .catch((error) => console.log(error));
    };

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{padding: 4, mt: 8}}>
                <Typography variant="h5" align="center" gutterBottom>Register</Typography>
                <Box>
                    <TextField
                        fullWidth
                        label="First name"
                        name="firstName"
                        margin="normal"
                        value={formData.firstName}
                        onChange={handleChange}
                        required
                    />
                    <TextField
                        fullWidth
                        label="Last name"
                        name="lastName"
                        margin="normal"
                        value={formData.lastName}
                        onChange={handleChange}
                        required
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
                    />
                    <Button fullWidth variant="contained" type="submit" sx={{mt: 2}} onClick={handleSubmit}>
                        Register
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default Register;