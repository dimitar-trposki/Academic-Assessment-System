// src/ui/pages/MyProfile/MyProfile.jsx
import React, {useEffect, useState} from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Typography,
    List,
    ListItem,
    ListItemText,
    Divider,
} from "@mui/material";
import {useNavigate} from "react-router";

import useUsers from "../../../hooks/useUsers.js";
import useStudents from "../../../hooks/useStudents.js";
import useAuth from "../../../hooks/useAuth.js";

import "./MyProfile.css";

const MyProfile = () => {
    const {
        me,
        findById,
        findCoursesByUserId,
        findAllStudents,
    } = useUsers();

    const {
        findCourseEnrollmentByStudentId,
        findStudentExamRegistrationByStudentId,
    } = useStudents();

    const {logout} = useAuth();
    const navigate = useNavigate();

    const [principal, setPrincipal] = useState(null);
    const [profile, setProfile] = useState(null);

    const [staffCourses, setStaffCourses] = useState([]);
    const [courseEnrollments, setCourseEnrollments] = useState([]);
    const [examRegistrations, setExamRegistrations] = useState([]);
    const [studentProfile, setStudentProfile] = useState(null); // DisplayUserStudentDto

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadStudentData = async (userId) => {
            // 1) all DisplayUserStudentDto
            const usersStudents = await findAllStudents();
            if (!usersStudents) return;

            // 2) find this user's student profile
            const myStudent = usersStudents.find((s) => s.userId === userId);
            if (!myStudent) return;

            setStudentProfile(myStudent);

            const studentId = myStudent.id;

            // 3) course enrollments
            const enrollments =
                await findCourseEnrollmentByStudentId(studentId);
            setCourseEnrollments(enrollments || []);

            // 4) exam registrations
            const registrations =
                await findStudentExamRegistrationByStudentId(studentId);
            setExamRegistrations(registrations || []);
        };

        const loadProfile = async () => {
            try {
                const principalData = await me();
                if (!principalData) {
                    throw new Error("Could not load principal");
                }
                setPrincipal(principalData);

                const userData = await findById(principalData.id);
                if (!userData) {
                    throw new Error("Could not load user profile");
                }
                setProfile(userData);

                const currentRole =
                    principalData.role ||
                    principalData.userRole ||
                    userData.userRole;

                if (currentRole === "STAFF") {
                    const assigned =
                        await findCoursesByUserId(principalData.id);
                    setStaffCourses(assigned || []);
                } else if (currentRole === "STUDENT") {
                    await loadStudentData(principalData.id);
                }
            } catch (error) {
                console.error(error);
                logout();
                navigate("/login", {replace: true});
            } finally {
                setLoading(false);
            }
        };

        loadProfile();
    }, [
        me,
        findById,
        findCoursesByUserId,
        findAllStudents,
        findCourseEnrollmentByStudentId,
        findStudentExamRegistrationByStudentId,
        logout,
        navigate,
    ]);

    const handleLogout = () => {
        logout();
        navigate("/", {replace: true});
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
                <Typography variant="body1">
                    Could not load profile.
                </Typography>
            </Box>
        );
    }

    const role =
        principal?.role ||
        principal?.userRole ||
        profile.userRole;

    const initials = `${profile.firstName?.[0] || ""}${profile.lastName?.[0] || ""}`.toUpperCase();

    return (
        <Box className="my-profile-root">
            <Card className="my-profile-card">
                <CardContent className="my-profile-card-content">
                    {/* HEADER */}
                    <Box className="my-profile-header">
                        <Box className="my-profile-avatar">
                            <span>{initials || "U"}</span>
                        </Box>

                        <Box>
                            <Typography className="my-profile-overline">
                                ACCOUNT OVERVIEW
                            </Typography>
                            <Typography
                                variant="h5"
                                className="my-profile-title"
                            >
                                My profile
                            </Typography>
                            <Typography className="my-profile-subtitle">
                                Signed in as{" "}
                                <span>{profile.email}</span>
                            </Typography>
                        </Box>
                    </Box>

                    {/* PROFILE FIELD CARDS */}
                    <Box className="my-profile-fields-grid">
                        <div className="my-profile-field">
                            <label>FIRST NAME</label>
                            <span>{profile.firstName}</span>
                        </div>

                        <div className="my-profile-field">
                            <label>LAST NAME</label>
                            <span>{profile.lastName}</span>
                        </div>

                        <div className="my-profile-field">
                            <label>E-MAIL</label>
                            <span>{profile.email}</span>
                        </div>

                        <div className="my-profile-field">
                            <label>ROLE</label>
                            <span className="my-profile-role-pill">
                                {profile.userRole}
                            </span>
                        </div>

                        {/* Student-only extra fields */}
                        {role === "STUDENT" && studentProfile && (
                            <>
                                <div className="my-profile-field">
                                    <label>STUDENT INDEX</label>
                                    <span>{studentProfile.studentIndex}</span>
                                </div>

                                <div className="my-profile-field">
                                    <label>MAJOR</label>
                                    <span>{studentProfile.major}</span>
                                </div>
                            </>
                        )}
                    </Box>

                    {/* STAFF – ASSIGNED COURSES */}
                    {role === "STAFF" && (
                        <Box className="my-profile-section-card">
                            <Typography
                                variant="subtitle1"
                                className="my-profile-section-title"
                            >
                                Assigned courses
                            </Typography>

                            {staffCourses.length === 0 ? (
                                <Typography
                                    variant="body2"
                                    className="my-profile-muted"
                                >
                                    You have no assigned courses yet.
                                </Typography>
                            ) : (
                                <List dense className="my-profile-list">
                                    {staffCourses.map((assignment) => (
                                        <ListItem key={assignment.id}>
                                            <ListItemText
                                                primary={`${assignment.courseCode} – ${assignment.courseName}`}
                                                secondary={`Semester: ${assignment.courseSemester} | Academic year: ${assignment.courseAcademicYear} | Staff role: ${assignment.staffRole}`}
                                            />
                                        </ListItem>
                                    ))}
                                </List>
                            )}
                        </Box>
                    )}

                    {/* STUDENT – ENROLLED COURSES */}
                    {role === "STUDENT" && (
                        <>
                            <Box className="my-profile-section-card">
                                <Typography
                                    variant="subtitle1"
                                    className="my-profile-section-title"
                                >
                                    My enrolled courses
                                </Typography>

                                {courseEnrollments.length === 0 ? (
                                    <Typography
                                        variant="body2"
                                        className="my-profile-muted"
                                    >
                                        You are not enrolled in any courses yet.
                                    </Typography>
                                ) : (
                                    <List dense className="my-profile-list">
                                        {courseEnrollments.map((enr) => (
                                            <ListItem key={enr.id}>
                                                <ListItemText
                                                    primary={`${enr.courseCode} – ${enr.courseName}`}
                                                    secondary={`Semester: ${enr.courseSemester}`}
                                                />
                                            </ListItem>
                                        ))}
                                    </List>
                                )}
                            </Box>

                            {/* STUDENT – EXAM REGISTRATIONS */}
                            <Box className="my-profile-section-card">
                                <Typography
                                    variant="subtitle1"
                                    className="my-profile-section-title"
                                >
                                    My exam registrations
                                </Typography>

                                {examRegistrations.length === 0 ? (
                                    <Typography
                                        variant="body2"
                                        className="my-profile-muted"
                                    >
                                        You have no exam registrations yet.
                                    </Typography>
                                ) : (
                                    <List dense className="my-profile-list">
                                        {examRegistrations.map((reg) => (
                                            <ListItem key={reg.id}>
                                                <ListItemText
                                                    primary={`${reg.examCourse} – ${reg.examSession}`}
                                                    secondary={`Date: ${reg.examDate} | Start: ${reg.startTime} | Status: ${reg.examStatus}`}
                                                />
                                            </ListItem>
                                        ))}
                                    </List>
                                )}
                            </Box>
                        </>
                    )}

                    {/* FOOTER / LOGOUT */}
                    <Box className="my-profile-footer">
                        <Button
                            variant="contained"
                            className="my-profile-logout"
                            onClick={handleLogout}
                        >
                            Logout
                        </Button>
                    </Box>
                </CardContent>
            </Card>
        </Box>
    );
};

export default MyProfile;
