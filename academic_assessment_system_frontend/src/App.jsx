// src/App.jsx
import React from "react";
import {
    AppBar,
    Box,
    CssBaseline,
    Toolbar,
    Typography,
    Button,
    Container,
    Stack,
} from "@mui/material";
import {
    BrowserRouter,
    Routes,
    Route,
    NavLink,
    useNavigate,
} from "react-router-dom";

import HomePage from "./ui/pages/HomePage/HomePage.jsx";
import CoursesPage from "./ui/pages/CoursesPage/CoursesPage.jsx";
import ExamsPage from "./ui/pages/ExamsPage/ExamsPage.jsx";
import StudentsPage from "./ui/pages/StudentsPage/StudentsPage.jsx";
import UsersPage from "./ui/pages/UsersPage/UsersPage.jsx";
import Login from "./ui/components/auth/Login/Login.jsx";
import Register from "./ui/components/auth/Register/Register.jsx";
import MePage from "./ui/pages/MyProfile/MyProfile.jsx";
import ProtectedRoute from "./ui/components/routing/ProtectedRoute/ProtectedRoute.jsx";

import "./App.css";

// define nav items once
const NAV_ITEMS = [
    {label: "Home", path: "/"},
    {label: "Courses", path: "/courses"},
    {label: "Exams", path: "/exams"},
    // {label: "Students", path: "/students"},
    {label: "Users", path: "/users"},
];

// small helper to map old "page name" -> route
const pageToPath = (page) => {
    switch (page) {
        case "Courses":
            return "/courses";
        case "Exams":
            return "/exams";
        // case "Students":
        //     return "/students";
        case "Users":
            return "/users";
        case "Home":
        default:
            return "/";
    }
};

// Inner layout that can use React Router hooks
const AppLayout = () => {
    const navigate = useNavigate();

    const handleNavigateFromHome = (page) => {
        const path = pageToPath(page);
        navigate(path);
    };

    return (
        <Box className="app-root">
            <CssBaseline/>

            {/* Top navigation bar */}
            <AppBar
                position="fixed"
                color="transparent"
                elevation={0}
                className="app-bar-glass"
            >
                <Toolbar>
                    <Typography
                        variant="h6"
                        sx={{flexGrow: 1, fontWeight: 600}}
                    >
                        Academic Assessment System
                    </Typography>

                    <Stack direction="row" spacing={1}>
                        {NAV_ITEMS.map((item) => (
                            <Button
                                key={item.path}
                                color="inherit"
                                size="small"
                                component={NavLink}
                                to={item.path}
                                className={({isActive}) =>
                                    isActive
                                        ? "nav-btn nav-btn-active"
                                        : "nav-btn"
                                }
                            >
                                {item.label}
                            </Button>
                        ))}
                    </Stack>
                </Toolbar>
            </AppBar>

            {/* Main content */}
            <Box component="main" className="app-main">
                {/* spacer so content is not hidden under AppBar */}
                <Toolbar/>

                <Container maxWidth="lg" className="app-container">
                    <Routes>
                        <Route
                            path="/"
                            element={
                                <HomePage onNavigate={handleNavigateFromHome}/>
                            }
                        />

                        <Route path="/courses" element={<CoursesPage/>}/>
                        <Route path="/exams" element={<ExamsPage/>}/>
                        {/* <Route path="/students" element={<StudentsPage/>}/> */}
                        <Route path="/users" element={<UsersPage/>}/>

                        {/* Auth routes */}
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/register" element={<Register/>}/>

                        {/* Protected /me route */}
                        <Route element={<ProtectedRoute/>}>
                            <Route path="/me" element={<MePage/>}/>
                        </Route>

                        {/* optional fallback */}
                        {/* <Route path="*" element={<HomePage onNavigate={handleNavigateFromHome} />} /> */}
                    </Routes>
                </Container>
            </Box>
        </Box>
    );
};

const App = () => {
    return (
        <BrowserRouter>
            <AppLayout/>
        </BrowserRouter>
    );
};

export default App;
