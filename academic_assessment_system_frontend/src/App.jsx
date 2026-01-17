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
    Menu,
    MenuItem,
} from "@mui/material";
import {
    BrowserRouter,
    Routes,
    Route,
    NavLink,
    useNavigate,
} from "react-router-dom";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

import HomePage from "./ui/pages/HomePage/HomePage.jsx";
import CoursesPage from "./ui/pages/CoursesPage/CoursesPage.jsx";
import ExamsPage from "./ui/pages/ExamsPage/ExamsPage.jsx";
import UsersPage from "./ui/pages/UsersPage/UsersPage.jsx";
import Login from "./ui/components/auth/Login/Login.jsx";
import Register from "./ui/components/auth/Register/Register.jsx";
import MePage from "./ui/pages/MyProfile/MyProfile.jsx";
import ProtectedRoute from "./ui/components/routing/ProtectedRoute/ProtectedRoute.jsx";

import useAuth from "./hooks/useAuth.js";

import "./App.css";

const pageToPath = (page) => {
    switch (page) {
        case "Courses":
            return "/courses";
        case "Exams":
            return "/exams";
        case "Users":
            return "/users";
        case "Home":
        default:
            return "/";
    }
};

const NAV_ITEMS = [
    {label: "Home", path: "/"},
    {label: "Courses", path: "/courses"},
    {label: "Exams", path: "/exams"},
    {label: "Users", path: "/users"},
];

const AppLayout = () => {
    const navigate = useNavigate();
    const {user, isLoggedIn, logout} = useAuth();

    const [profileAnchor, setProfileAnchor] = React.useState(null);
    const isProfileMenuOpen = Boolean(profileAnchor);

    const handleNavigateFromHome = (page) => {
        const path = pageToPath(page);
        navigate(path);
    };

    const openProfileMenu = (event) => {
        setProfileAnchor(event.currentTarget);
    };

    const closeProfileMenu = () => {
        setProfileAnchor(null);
    };

    const handleLogout = () => {
        closeProfileMenu();
        logout();
        navigate("/login");
    };

    const handleProfile = () => {
        closeProfileMenu();
        navigate("/me");
    };

    const displayName =
        user?.fullName || user?.username || user?.email || "Profile";

    return (
        <Box className="app-root">
            <CssBaseline/>

            <AppBar
                position="fixed"
                color="transparent"
                elevation={0}
                className="app-bar-glass"
            >
                <Toolbar className="app-toolbar">
                    <Typography
                        variant="h6"
                        className="app-logo"
                    >
                        Academic Assessment System
                    </Typography>

                    <Stack direction="row" spacing={1} alignItems="center">
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
                                style={{color: "#ffffff"}}
                            >
                                {item.label}
                            </Button>
                        ))}

                        {!isLoggedIn && (
                            <>
                                <Button
                                    color="inherit"
                                    size="small"
                                    component={NavLink}
                                    to="/login"
                                    className={({isActive}) =>
                                        isActive
                                            ? "nav-btn nav-btn-active"
                                            : "nav-btn"
                                    }
                                    style={{color: "#ffffff"}}
                                >
                                    Login
                                </Button>
                                <Button
                                    color="inherit"
                                    size="small"
                                    component={NavLink}
                                    to="/register"
                                    className={({isActive}) =>
                                        isActive
                                            ? "nav-btn nav-btn-active"
                                            : "nav-btn"
                                    }
                                    style={{color: "#ffffff"}}
                                >
                                    Register
                                </Button>
                            </>
                        )}

                        {isLoggedIn && (
                            <>
                                <Button
                                    color="inherit"
                                    size="small"
                                    onClick={openProfileMenu}
                                    className="nav-profile-trigger"
                                    endIcon={<ExpandMoreIcon className="nav-profile-icon"/>}
                                    style={{color: "#ffffff"}}
                                >
                                    {displayName}
                                </Button>

                                <Menu
                                    anchorEl={profileAnchor}
                                    open={isProfileMenuOpen}
                                    onClose={closeProfileMenu}
                                    anchorOrigin={{
                                        vertical: "bottom",
                                        horizontal: "right",
                                    }}
                                    transformOrigin={{
                                        vertical: "top",
                                        horizontal: "right",
                                    }}
                                >
                                    <MenuItem onClick={handleProfile}>
                                        My profile
                                    </MenuItem>
                                    <MenuItem onClick={handleLogout}>
                                        Logout
                                    </MenuItem>
                                </Menu>
                            </>
                        )}
                    </Stack>
                </Toolbar>
            </AppBar>

            <Box component="main" className="app-main">
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
                        <Route path="/users" element={<UsersPage/>}/>

                        <Route path="/login" element={<Login/>}/>
                        <Route path="/register" element={<Register/>}/>

                        <Route element={<ProtectedRoute/>}>
                            <Route path="/me" element={<MePage/>}/>
                        </Route>
                    </Routes>
                </Container>

                <Box component="footer" className="app-footer">
                    <Container maxWidth="lg" className="app-footer-inner">
                        <Typography variant="caption" className="app-footer-text">
                            © {new Date().getFullYear()} Academic Assessment System
                        </Typography>
                    </Container>
                </Box>
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
