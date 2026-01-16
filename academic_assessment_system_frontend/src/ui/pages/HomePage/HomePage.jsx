import React, {useEffect, useState} from "react";
import {
    Box,
    Card,
    CardContent,
    Grid,
    Stack,
    Typography,
    Chip,
} from "@mui/material";
import useAuth from "../../../hooks/useAuth.js";
import "./HomePage.css";

const HomePage = ({
                      onNavigate = () => {
                      }
                  }) => {
    const [now, setNow] = useState(new Date());
    const {isLoggedIn} = useAuth();

    useEffect(() => {
        const id = setInterval(() => setNow(new Date()), 1000);
        return () => clearInterval(id);
    }, []);

    const formattedDate = now.toLocaleDateString(undefined, {
        weekday: "long",
        year: "numeric",
        month: "short",
        day: "numeric",
    });

    const formattedTime = now.toLocaleTimeString(undefined, {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });

    return (
        <Box className="home-root">
            <Grid container spacing={3} className="home-top-grid">
                <Grid item xs={12} md={8}>
                    <Card className="home-card home-hero-card">
                        <CardContent className="home-card-content home-hero-content">
                            <Box className="home-hero-main">
                                <Box className="home-hero-text">
                                    <Chip
                                        label="Academic Assessment System"
                                        color="primary"
                                        className="home-chip"
                                    />
                                    <Typography variant="h4" className="home-title">
                                        All your academic data in one clear view.
                                    </Typography>
                                    <Typography variant="body1" className="home-subtitle">
                                        Courses, exams, enrollments and user roles are organised
                                        in a single place. Students always know what is next, and
                                        staff keep teaching and assessment aligned.
                                    </Typography>
                                </Box>
                            </Box>

                            <Grid container spacing={2} className="home-hero-stats">
                                <Grid item xs={12} sm={4}>
                                    <Box className="home-stat">
                                        <Typography
                                            variant="subtitle2"
                                            className="home-stat-label"
                                        >
                                            Courses
                                        </Typography>
                                        <Typography
                                            variant="body1"
                                            className="home-stat-title"
                                        >
                                            Structured overview
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                            className="home-stat-text"
                                        >
                                            Organisation of courses per semester with assigned staff
                                            and enrolled students.
                                        </Typography>
                                    </Box>
                                </Grid>
                                <Grid item xs={12} sm={4}>
                                    <Box className="home-stat">
                                        <Typography
                                            variant="subtitle2"
                                            className="home-stat-label"
                                        >
                                            Exams
                                        </Typography>
                                        <Typography
                                            variant="body1"
                                            className="home-stat-title"
                                        >
                                            Clear scheduling
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                            className="home-stat-text"
                                        >
                                            Exam terms with date, time, room and registration status
                                            in one place.
                                        </Typography>
                                    </Box>
                                </Grid>
                                <Grid item xs={12} sm={4}>
                                    <Box className="home-stat">
                                        <Typography
                                            variant="subtitle2"
                                            className="home-stat-label"
                                        >
                                            Roles
                                        </Typography>
                                        <Typography
                                            variant="body1"
                                            className="home-stat-title"
                                        >
                                            Role-based access
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                            className="home-stat-text"
                                        >
                                            Tailored views for students, staff, administrators and
                                            registered users.
                                        </Typography>
                                    </Box>
                                </Grid>
                            </Grid>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} md={4}>
                    <Card className="home-card home-today-card">
                        <CardContent className="home-card-content home-today-content">
                            <Grid container spacing={2} className="home-today-grid">
                                <Grid item xs={12} sm={6}>
                                    <Box className="home-today-left">
                                        <Typography
                                            variant="subtitle2"
                                            className="home-today-label"
                                        >
                                            Today
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                            className="home-today-date"
                                        >
                                            {formattedDate}
                                        </Typography>
                                        <Typography
                                            variant="h4"
                                            className="home-today-time"
                                        >
                                            {formattedTime}
                                        </Typography>
                                    </Box>
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <Box className="home-today-right">
                                        <Typography
                                            variant="subtitle2"
                                            className="home-today-focus-title"
                                        >
                                            Focus for the day
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                            className="home-today-text"
                                        >
                                            Students review upcoming exams and enrolled courses.
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                            className="home-today-text"
                                        >
                                            Staff confirm exam sessions and registrations.
                                        </Typography>
                                        <Typography
                                            variant="body2"
                                            className="home-today-text"
                                        >
                                            Admins keep track of changes across the system.
                                        </Typography>
                                    </Box>
                                </Grid>
                            </Grid>

                            {isLoggedIn && (
                                <Box className="home-today-footer">
                                    <Typography
                                        variant="caption"
                                        className="home-today-caption"
                                    >
                                        Use the navigation bar above to move between Courses,
                                        Exams, Users and your profile.
                                    </Typography>
                                </Box>
                            )}
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            <Grid container spacing={3} className="home-bottom-grid">
                <Grid item xs={12} md={4}>
                    <Card className="home-card home-feature-card home-feature-student">
                        <CardContent className="home-card-content">
                            <Typography
                                variant="h6"
                                className="home-feature-title"
                            >
                                Student experience
                            </Typography>
                            <Typography
                                variant="body2"
                                className="home-feature-subtitle"
                            >
                                A focused view of what matters right now.
                            </Typography>
                            <Stack spacing={0.6}>
                                <Typography variant="body2" className="home-feature-text">
                                    • Enrolled courses grouped by semester.
                                </Typography>
                                <Typography variant="body2" className="home-feature-text">
                                    • Registered and upcoming exam terms.
                                </Typography>
                                <Typography variant="body2" className="home-feature-text">
                                    • Status of attendance and passed exams.
                                </Typography>
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} md={4}>
                    <Card className="home-card home-feature-card home-feature-staff">
                        <CardContent className="home-card-content">
                            <Typography
                                variant="h6"
                                className="home-feature-title"
                            >
                                Staff workflow
                            </Typography>
                            <Typography
                                variant="body2"
                                className="home-feature-subtitle"
                            >
                                Tools that keep teaching and assessment aligned.
                            </Typography>
                            <Stack spacing={0.6}>
                                <Typography variant="body2" className="home-feature-text">
                                    • Maintain courses and exam sessions.
                                </Typography>
                                <Typography variant="body2" className="home-feature-text">
                                    • Manage registrations and attendance lists.
                                </Typography>
                                <Typography variant="body2" className="home-feature-text">
                                    • Import and export data for reporting.
                                </Typography>
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} md={4}>
                    <Card className="home-card home-feature-card home-feature-admin">
                        <CardContent className="home-card-content">
                            <Typography
                                variant="h6"
                                className="home-feature-title"
                            >
                                Administration
                            </Typography>
                            <Typography
                                variant="body2"
                                className="home-feature-subtitle home-feature-subtitle-light"
                            >
                                Consistent data and clear responsibilities.
                            </Typography>
                            <Stack spacing={0.6}>
                                <Typography variant="body2" className="home-feature-text">
                                    • Configure user roles and permissions.
                                </Typography>
                                <Typography variant="body2" className="home-feature-text">
                                    • Ensure integrity of course and exam data.
                                </Typography>
                                <Typography variant="body2" className="home-feature-text">
                                    • Maintain a transparent academic record.
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
