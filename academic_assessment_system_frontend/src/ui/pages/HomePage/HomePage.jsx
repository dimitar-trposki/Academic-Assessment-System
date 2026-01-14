import React, {useEffect, useState} from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    Grid,
    Stack,
    Typography,
    Chip,
    Divider,
} from "@mui/material";
import {useNavigate} from "react-router";
import useAuth from "../../../hooks/useAuth.js";
import "./HomePage.css";

const quickActions = [
    {label: "Home", variant: "outlined", kind: "home"},
    {label: "Register", variant: "contained", kind: "register"},
    {label: "Log in", variant: "contained", kind: "login"},
    {label: "Courses", variant: "outlined", kind: "courses"},
    {label: "Exams", variant: "outlined", kind: "exams"},
    {label: "Users", variant: "outlined", kind: "users"},
    {label: "Students", variant: "outlined", kind: "student"},
];

const HomePage = ({
                      onNavigate = () => {
                      },
                  }) => {
    const [now, setNow] = useState(new Date());
    const navigate = useNavigate();
    const {isLoggedIn} = useAuth();

    useEffect(() => {
        const id = setInterval(() => setNow(new Date()), 1000);
        return () => clearInterval(id);
    }, []);

    const formattedDate = now.toLocaleDateString(undefined, {
        weekday: "long",
        year: "numeric",
        month: "long",
        day: "numeric",
    });

    const formattedTime = now.toLocaleTimeString(undefined, {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });

    const handleActionClick = (kind) => {
        // old navigation for your internal 'views'
        if (kind === "home") onNavigate("Home");
        if (kind === "courses") onNavigate("Courses");
        if (kind === "exams") onNavigate("Exams");
        if (kind === "users") onNavigate("Users");
        if (kind === "student") onNavigate("Students");

        // actual routes for auth
        if (kind === "register") {
            navigate("/register");
        }
        if (kind === "login") {
            navigate("/login");
        }
    };

    return (
        <Box className="home-root">
            {/* Hero section */}
            <Grid container spacing={3} sx={{mb: 3}}>
                <Grid item xs={12} md={7}>
                    <Card elevation={1}>
                        <CardContent>
                            <Stack spacing={2}>
                                <Chip
                                    label="Academic Assessment System"
                                    color="primary"
                                    sx={{alignSelf: "flex-start"}}
                                />
                                <Typography variant="h4" fontWeight={700}>
                                    All your courses, exams and students in one place.
                                </Typography>
                                <Typography variant="body1" color="text.secondary">
                                    Manage course assignments, exam sessions, student
                                    registrations and user roles from a single dashboard.
                                    As a student you can quickly see upcoming exams and
                                    what you are enrolled in. As staff you can keep
                                    everything organised and transparent.
                                </Typography>

                                <Stack direction="row" spacing={1} flexWrap="wrap">
                                    {quickActions.map((action) => (
                                        <Button
                                            key={action.label}
                                            variant={action.variant}
                                            color={
                                                ["Register", "Log in"].includes(action.label)
                                                    ? "secondary"
                                                    : "primary"
                                            }
                                            size="small"
                                            sx={{mt: 1}}
                                            onClick={() => handleActionClick(action.kind)}
                                        >
                                            {action.label}
                                        </Button>
                                    ))}

                                    {/* Only visible when user is logged in */}
                                    {isLoggedIn && (
                                        <Button
                                            variant="text"
                                            color="secondary"
                                            size="small"
                                            sx={{mt: 1}}
                                            onClick={() => navigate("/me")}
                                        >
                                            My profile
                                        </Button>
                                    )}
                                </Stack>
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>

                {/* Today / clock card */}
                <Grid item xs={12} md={5}>
                    <Card elevation={1}>
                        <CardContent>
                            <Typography variant="subtitle2" color="text.secondary">
                                Today
                            </Typography>
                            <Typography variant="h5" fontWeight={600} sx={{mb: 1}}>
                                {formattedDate}
                            </Typography>

                            <Typography
                                variant="h3"
                                fontWeight={700}
                                sx={{mb: 2, fontVariantNumeric: "tabular-nums"}}
                            >
                                {formattedTime}
                            </Typography>

                            <Divider sx={{my: 2}}/>

                            <Typography variant="body2" color="text.secondary">
                                When you log in, this area can show your upcoming exams,
                                deadlines and reminders for today.
                            </Typography>

                            <Stack spacing={1} sx={{mt: 2}}>
                                <Typography variant="body2">
                                    • No upcoming events for today yet.
                                </Typography>
                                <Typography variant="body2">
                                    • Use the <b>Courses</b> and <b>Exams</b> pages to
                                    start adding data.
                                </Typography>
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            {/* Logged-in preview / explanation */}
            <Grid container spacing={3}>
                <Grid item xs={12} md={6}>
                    <Card elevation={1}>
                        <CardContent>
                            <Typography variant="h6" fontWeight={600} gutterBottom>
                                For Students
                            </Typography>
                            <Typography variant="body2" color="text.secondary" paragraph>
                                Once logged in, students will see:
                            </Typography>
                            <Stack spacing={0.7}>
                                <Typography variant="body2">
                                    • Personal list of enrolled courses
                                </Typography>
                                <Typography variant="body2">
                                    • Upcoming exam terms and locations
                                </Typography>
                                <Typography variant="body2">
                                    • Quick access to registration / deregistration
                                </Typography>
                                <Typography variant="body2">
                                    • Summary of passed exams and points
                                </Typography>
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} md={6}>
                    <Card elevation={1}>
                        <CardContent>
                            <Typography variant="h6" fontWeight={600} gutterBottom>
                                For Professors & Admins
                            </Typography>
                            <Typography variant="body2" color="text.secondary" paragraph>
                                Logged-in staff will be able to:
                            </Typography>
                            <Stack spacing={0.7}>
                                <Typography variant="body2">
                                    • Manage courses and assign teaching staff
                                </Typography>
                                <Typography variant="body2">
                                    • Create and edit exam sessions
                                </Typography>
                                <Typography variant="body2">
                                    • Import / export students and registrations
                                </Typography>
                                <Typography variant="body2">
                                    • Manage user roles and permissions
                                </Typography>
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        </Box>
    );
};

export default HomePage;
