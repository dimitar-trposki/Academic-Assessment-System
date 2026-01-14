import React, {useEffect, useState} from "react";
import {Box, Button, Card, CardContent, CircularProgress, Typography} from "@mui/material";
import {useNavigate} from "react-router";
import useUsers from "../../../hooks/useUsers.js";
import useAuth from "../../../hooks/useAuth.js";

const MyProfile = () => {
    const {me} = useUsers();
    const {logout} = useAuth();
    const navigate = useNavigate();

    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadProfile = async () => {
            try {
                // 1) земи JwtUserPrincipal
                const principal = await me(); // { id, email, role }

                // 2) земи DisplayUserDto преку id
                const response = await fetch(`http://localhost:8080/api/users/${principal.id}`, {
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`,
                    },
                });
                if (!response.ok) {
                    throw new Error("Failed to fetch user details");
                }
                const userData = await response.json();
                setProfile(userData);
            } catch (error) {
                console.log(error);
                logout();
                navigate("/login", {replace: true});
            } finally {
                setLoading(false);
            }
        };

        loadProfile();
    }, [me, logout, navigate]);

    const handleLogout = () => {
        logout();
        navigate("/", {replace: true}); // HomePage
    };

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" mt={8}>
                <CircularProgress/>
            </Box>
        );
    }

    if (!profile) {
        return (
            <Box display="flex" justifyContent="center" mt={8}>
                <Typography variant="body1">Could not load profile.</Typography>
            </Box>
        );
    }

    return (
        <Box display="flex" justifyContent="center" mt={4}>
            <Card sx={{minWidth: 400}}>
                <CardContent>
                    <Typography variant="h5" gutterBottom>
                        My Profile
                    </Typography>
                    <Typography variant="body1">
                        <b>First name:</b> {profile.firstName}
                    </Typography>
                    <Typography variant="body1">
                        <b>Last name:</b> {profile.lastName}
                    </Typography>
                    <Typography variant="body1">
                        <b>E-mail:</b> {profile.email}
                    </Typography>
                    <Typography variant="body1">
                        <b>Role:</b> {profile.userRole}
                    </Typography>

                    <Button
                        variant="contained"
                        sx={{mt: 3}}
                        onClick={handleLogout}
                    >
                        Logout
                    </Button>
                </CardContent>
            </Card>
        </Box>
    );
};

export default MyProfile;
