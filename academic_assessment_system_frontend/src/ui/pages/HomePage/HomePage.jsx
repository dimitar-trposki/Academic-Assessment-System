import React, {useEffect, useMemo, useState} from "react";
import {useNavigate} from "react-router-dom";
import {
    Box,
    Card,
    CardContent,
    Grid,
    Stack,
    Typography,
    Chip,
    Divider,
    Button,
    CircularProgress,
} from "@mui/material";

import SchoolOutlinedIcon from "@mui/icons-material/SchoolOutlined";
import EventAvailableOutlinedIcon from "@mui/icons-material/EventAvailableOutlined";
import PeopleAltOutlinedIcon from "@mui/icons-material/PeopleAltOutlined";
import ArrowForwardRoundedIcon from "@mui/icons-material/ArrowForwardRounded";
import CalendarMonthRoundedIcon from "@mui/icons-material/CalendarMonthRounded";
import TimerRoundedIcon from "@mui/icons-material/TimerRounded";
import PlaceOutlinedIcon from "@mui/icons-material/PlaceOutlined";
import ImportExportRoundedIcon from "@mui/icons-material/ImportExportRounded";
import VerifiedUserOutlinedIcon from "@mui/icons-material/VerifiedUserOutlined";
import AccessTimeRoundedIcon from "@mui/icons-material/AccessTimeRounded";

import useAuth from "../../../hooks/useAuth.js";
import useCourses from "../../../hooks/useCourses.js";
import useExams from "../../../hooks/useExams.js";
import useUsers from "../../../hooks/useUsers.js";

import "./HomePage.css";

const ROLE_ADMIN = "ADMINISTRATOR";
const ROLE_STAFF = "STAFF";
const ROLE_USER = "USER";

const canManage = (role) => role === ROLE_ADMIN || role === ROLE_STAFF;

const normalizeTime = (t) => {
    if (!t) return "—";
    return String(t).substring(0, 5);
};

const toDate = (dateOfExam, startTime) => {
    if (!dateOfExam) return null;
    const time = normalizeTime(startTime);
    const safeTime = time === "—" ? "00:00" : time;
    const dt = new Date(`${dateOfExam}T${safeTime}:00`);
    return Number.isNaN(dt.getTime()) ? null : dt;
};

const formatRooms = (rooms) => {
    if (!rooms || !Array.isArray(rooms) || rooms.length === 0) return "—";
    return rooms.join(", ");
};

const HomePage = () => {
    const navigate = useNavigate();
    const go = (path) => navigate(path);

    const [now, setNow] = useState(new Date());

    const {isLoggedIn, user} = useAuth();
    const {courses, loading: coursesLoading} = useCourses();
    const {exams, loading: examsLoading} = useExams();
    const {users, loading: usersLoading} = useUsers();

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

    const role = user?.role || ROLE_USER;
    const isManager = canManage(role);

    const loading = coursesLoading || examsLoading || usersLoading;

    const courseCount = courses?.length ?? 0;
    const examCount = exams?.length ?? 0;
    const userCount = users?.length ?? 0;

    const upcomingExams = useMemo(() => {
        const nowTs = Date.now();

        const sorted = (exams || [])
            .map((e) => ({exam: e, dt: toDate(e.dateOfExam, e.startTime)}))
            .filter((x) => x.dt)
            .sort((a, b) => a.dt.getTime() - b.dt.getTime());

        return sorted
            .filter((x) => x.dt.getTime() >= nowTs)
            .slice(0, 3)
            .map((x) => x.exam);
    }, [exams]);

    const headline = useMemo(() => {
        if (!isLoggedIn) return "All your academic data in one clear view.";
        if (role === ROLE_ADMIN) return "Full control over courses, exams and roles.";
        if (role === ROLE_STAFF) return "Keep teaching and assessment aligned — fast and consistent.";
        return "Browse everything safely — view-only access.";
    }, [isLoggedIn, role]);

    const subtitle =
        "Courses, exam sessions, enrollments and role-based access are organized in one place. " +
        "Data stays consistent with the system record, while CSV import/export keeps workflows fast.";

    return (
        <Box className="home-root">
            <Grid container spacing={3} className="home-top-grid" alignItems="flex-start">
                <Grid item xs={12} className="home-full">
                    <Card className="home-card home-hero-card home-full-card">
                        <CardContent className="home-card-content home-hero-content">
                            <Box className="home-hero-head">
                                <Box className="home-hero-left">
                                    <Chip
                                        label="Academic Assessment System"
                                        color="primary"
                                        className="home-chip"
                                    />

                                    <Typography variant="h4" className="home-title">
                                        {headline}
                                    </Typography>

                                    <Typography variant="body1" className="home-subtitle">
                                        {subtitle}
                                    </Typography>

                                    <Box className="home-pill-row">
                                        <Box className="home-pill" onClick={() => go("/courses")} role="button"
                                             tabIndex={0}>
                                            <SchoolOutlinedIcon className="home-pill-icon"/>
                                            <Typography className="home-pill-text">
                                                {loading ? "…" : `${courseCount}`} courses
                                            </Typography>
                                        </Box>

                                        <Box className="home-pill" onClick={() => go("/exams")} role="button"
                                             tabIndex={0}>
                                            <EventAvailableOutlinedIcon className="home-pill-icon"/>
                                            <Typography className="home-pill-text">
                                                {loading ? "…" : `${examCount}`} exams
                                            </Typography>
                                        </Box>

                                        <Box className="home-pill" onClick={() => go("/users")} role="button"
                                             tabIndex={0}>
                                            <PeopleAltOutlinedIcon className="home-pill-icon"/>
                                            <Typography className="home-pill-text">
                                                {loading ? "…" : `${userCount}`} users
                                            </Typography>
                                        </Box>
                                    </Box>

                                    <Divider className="home-divider"/>

                                    <Grid container spacing={2} className="home-hero-stats">
                                        <Grid item xs={12}>
                                            <Box className="home-stat">
                                                <Typography className="home-stat-label">CSV WORKFLOWS</Typography>
                                                <Typography className="home-stat-title">Import / Export</Typography>
                                                <Typography className="home-stat-text">
                                                    Export lists and import attendance after the exam — fast reporting
                                                    with consistent data.
                                                </Typography>
                                            </Box>
                                        </Grid>

                                        <Grid item xs={12}>
                                            <Box className="home-stat">
                                                <Typography className="home-stat-label">EXAMS</Typography>
                                                <Typography className="home-stat-title">Clear scheduling</Typography>
                                                <Typography className="home-stat-text">
                                                    Session, date, time window, capacity and reserved rooms in a single
                                                    place.
                                                </Typography>
                                            </Box>
                                        </Grid>

                                        <Grid item xs={12}>
                                            <Box className="home-stat">
                                                <Typography className="home-stat-label">COURSES</Typography>
                                                <Typography className="home-stat-title">Structured overview</Typography>
                                                <Typography className="home-stat-text">
                                                    Course code, name, semester and academic year — plus assigned staff
                                                    and enrolled students.
                                                </Typography>
                                            </Box>
                                        </Grid>
                                    </Grid>

                                    {loading && (
                                        <Box className="home-loading">
                                            <CircularProgress size={22}/>
                                            <Typography className="home-loading-text">
                                                Syncing overview with the database…
                                            </Typography>
                                        </Box>
                                    )}
                                </Box>

                                <Box className="home-hero-right">
                                    <Box className="home-right-stack">
                                        <Card className="home-card home-right-card">
                                            <CardContent className="home-card-content home-today-content">
                                                <Box className="home-today-header">
                                                    <Box className="home-today-left">
                                                        <Typography className="home-today-label">Today</Typography>
                                                        <Typography
                                                            className="home-today-date">{formattedDate}</Typography>
                                                    </Box>

                                                    <Box className="home-today-timebox">
                                                        <AccessTimeRoundedIcon className="home-today-time-icon"/>
                                                        <Typography
                                                            className="home-today-time">{formattedTime}</Typography>
                                                    </Box>
                                                </Box>

                                                <Divider className="home-today-divider"/>

                                                <Typography className="home-today-caption">
                                                    <VerifiedUserOutlinedIcon className="home-caption-icon"/>
                                                    {isManager
                                                        ? "You can navigate and inspect data."
                                                        : "You can navigate and inspect data."}
                                                </Typography>
                                            </CardContent>
                                        </Card>

                                        <Card className="home-card home-right-card">
                                            <CardContent className="home-card-content">
                                                <Typography className="home-side-title">Upcoming exams</Typography>

                                                <Divider className="home-divider"/>

                                                {upcomingExams.length === 0 ? (
                                                    <Box className="home-empty">
                                                        <CalendarMonthRoundedIcon className="home-empty-icon"/>
                                                        <Typography className="home-empty-title">No upcoming
                                                            exams</Typography>
                                                        <Typography className="home-empty-sub">
                                                            Add an exam term or check course assignments.
                                                        </Typography>
                                                    </Box>
                                                ) : (
                                                    <Stack spacing={1} className="home-upcoming-list">
                                                        {upcomingExams.map((e) => (
                                                            <Box
                                                                key={e.id}
                                                                className="home-upcoming-item"
                                                                onClick={() => go("/exams")}
                                                                role="button"
                                                                tabIndex={0}
                                                                onKeyDown={(ev) => {
                                                                    if (ev.key === "Enter" || ev.key === " ") go("/exams");
                                                                }}
                                                            >
                                                                <Box className="home-upcoming-top">
                                                                    <Typography className="home-upcoming-item-title">
                                                                        {e.course?.courseName || "Exam session"}
                                                                    </Typography>
                                                                    <Chip
                                                                        size="small"
                                                                        label={e.session || "Session"}
                                                                        className="home-mini-chip"
                                                                    />
                                                                </Box>

                                                                <Typography className="home-upcoming-meta">
                                                                    <CalendarMonthRoundedIcon
                                                                        className="home-mini-icon"/>
                                                                    {e.dateOfExam || "—"}
                                                                    <span className="home-dot"/>
                                                                    <TimerRoundedIcon className="home-mini-icon"/>
                                                                    {normalizeTime(e.startTime)} - {normalizeTime(e.endTime)}
                                                                </Typography>

                                                                <Typography className="home-upcoming-meta">
                                                                    <PlaceOutlinedIcon className="home-mini-icon"/>
                                                                    {formatRooms(e.reservedLaboratories)}
                                                                </Typography>
                                                            </Box>
                                                        ))}
                                                    </Stack>
                                                )}

                                                <Box className="home-upcoming-actions">
                                                    <Button
                                                        variant="contained"
                                                        endIcon={<ArrowForwardRoundedIcon/>}
                                                        onClick={() => go("/exams")}
                                                        className="home-action-primary"
                                                        fullWidth
                                                    >
                                                        Open Exams
                                                    </Button>
                                                </Box>
                                            </CardContent>
                                        </Card>
                                    </Box>
                                </Box>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            <Grid container spacing={3} className="home-bottom-grid">
                <Grid item xs={12} className="home-full">
                    <Box className="home-bottom-stack">
                        <Card className="home-card home-snapshot-card home-full-card">
                            <CardContent className="home-card-content">
                                <Typography className="home-snapshot-title">Courses snapshot</Typography>
                                <Typography className="home-snapshot-subtitle">
                                    Browse courses by semester and academic year, with staff assignments and
                                    enrollments.
                                </Typography>

                                <Box className="home-snapshot-kpis">
                                    <Box className="home-kpi">
                                        <Typography
                                            className="home-kpi-value">{loading ? "…" : courseCount}</Typography>
                                        <Typography className="home-kpi-label">Total courses</Typography>
                                    </Box>

                                    <Box className="home-kpi">
                                        <Typography className="home-kpi-value">
                                            <ImportExportRoundedIcon fontSize="small"/>
                                        </Typography>
                                        <Typography className="home-kpi-label">CSV Enrollments</Typography>
                                    </Box>
                                </Box>

                                <Button
                                    fullWidth
                                    variant="outlined"
                                    className="home-snapshot-btn"
                                    onClick={() => go("/courses")}
                                >
                                    Open Courses
                                </Button>
                            </CardContent>
                        </Card>

                        <Card className="home-card home-snapshot-card home-full-card">
                            <CardContent className="home-card-content">
                                <Typography className="home-snapshot-title">Exams snapshot</Typography>
                                <Typography className="home-snapshot-subtitle">
                                    Sessions with rooms, time windows, capacity and student registrations.
                                </Typography>

                                <Box className="home-snapshot-kpis">
                                    <Box className="home-kpi">
                                        <Typography className="home-kpi-value">{loading ? "…" : examCount}</Typography>
                                        <Typography className="home-kpi-label">Exam terms</Typography>
                                    </Box>

                                    <Box className="home-kpi">
                                        <Typography className="home-kpi-value">
                                            <ImportExportRoundedIcon fontSize="small"/>
                                        </Typography>
                                        <Typography className="home-kpi-label">CSV Reg/Attend</Typography>
                                    </Box>
                                </Box>

                                <Button
                                    fullWidth
                                    variant="outlined"
                                    className="home-snapshot-btn"
                                    onClick={() => go("/exams")}
                                >
                                    Open Exams
                                </Button>
                            </CardContent>
                        </Card>

                        <Card className="home-card home-snapshot-card home-full-card">
                            <CardContent className="home-card-content">
                                <Typography className="home-snapshot-title">Role permissions</Typography>
                                <Typography className="home-snapshot-subtitle">
                                    Everyone can navigate and inspect data; management actions depend on role.
                                </Typography>

                                <Stack spacing={0.8} className="home-perms">
                                    <Box className="home-perm-row">
                                        <Typography className="home-perm-pill">ADMIN</Typography>
                                        <Typography className="home-perm-text">
                                            Full access: add, edit, delete, import/export, users & roles.
                                        </Typography>
                                    </Box>

                                    <Box className="home-perm-row">
                                        <Typography className="home-perm-pill">STAFF</Typography>
                                        <Typography className="home-perm-text">
                                            Manage courses and exams, registrations and attendance lists.
                                        </Typography>
                                    </Box>

                                    <Box className="home-perm-row">
                                        <Typography className="home-perm-pill home-perm-pill-user">STUDENT</Typography>
                                        <Typography className="home-perm-text">
                                            View courses/exams and follow your academic plan.
                                        </Typography>
                                    </Box>

                                    <Box className="home-perm-row">
                                        <Typography className="home-perm-pill home-perm-pill-user">USER</Typography>
                                        <Typography className="home-perm-text">
                                            Browse and inspect data with safe, read-only access.
                                        </Typography>
                                    </Box>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Box>
                </Grid>
            </Grid>
        </Box>
    );
};

export default HomePage;
