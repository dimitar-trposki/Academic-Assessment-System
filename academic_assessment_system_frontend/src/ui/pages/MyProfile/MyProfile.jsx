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

    const [principal, setPrincipal] = useState(null); // JwtUserPrincipal
    const [profile, setProfile] = useState(null);     // DisplayUserDto

    const [staffCourses, setStaffCourses] = useState([]);            // List<DisplayCourseStaffAssignmentDto>
    const [courseEnrollments, setCourseEnrollments] = useState([]);  // List<DisplayCourseEnrollmentDto>
    const [examRegistrations, setExamRegistrations] = useState([]);  // List<DisplayStudentExamRegistrationDto>

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadStudentData = async (userId) => {
            // 1) земи ги DisplayUserStudentDto за сите студенти преку useUsers hook
            const usersStudents = await findAllStudents();
            if (!usersStudents) return;

            // 2) најди го студентскиот профил за тековниот user
            const myStudent = usersStudents.find((s) => s.userId === userId);

            if (!myStudent) {
                // user има role STUDENT, ама нема student profile
                return;
            }

            const studentId = myStudent.id;

            // 3) course enrollments (преку useStudents hook)
            const enrollments = await findCourseEnrollmentByStudentId(studentId);
            setCourseEnrollments(enrollments || []);

            // 4) exam registrations (преку useStudents hook)
            const registrations = await findStudentExamRegistrationByStudentId(studentId);
            setExamRegistrations(registrations || []);
        };

        const loadProfile = async () => {
            try {
                // 1) JwtUserPrincipal
                const principalData = await me();
                if (!principalData) {
                    throw new Error("Could not load principal");
                }
                setPrincipal(principalData);

                // 2) DisplayUserDto
                const userData = await findById(principalData.id);
                if (!userData) {
                    throw new Error("Could not load user profile");
                }
                setProfile(userData);

                // 3) role-specific data
                const currentRole =
                    principalData.role ||
                    principalData.userRole ||
                    userData.userRole;

                if (currentRole === "STAFF") {
                    // Assigned courses за staff преку useUsers hook
                    const assigned = await findCoursesByUserId(principalData.id);
                    setStaffCourses(assigned || []);
                } else if (currentRole === "STUDENT") {
                    // Enrollments + exam registrations за student
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

    const role =
        principal?.role ||
        principal?.userRole ||
        profile.userRole;

    return (
        <Box display="flex" justifyContent="center" mt={4}>
            <Card sx={{minWidth: 500}}>
                <CardContent>
                    <Typography variant="h5" gutterBottom>
                        My Profile
                    </Typography>

                    {/* Basic info – за сите роли */}
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

                    {/* STAFF – assigned courses */}
                    {role === "STAFF" && (
                        <>
                            <Divider sx={{my: 2}}/>
                            <Typography variant="h6" gutterBottom>
                                Assigned Courses
                            </Typography>

                            {staffCourses.length === 0 ? (
                                <Typography variant="body2" color="text.secondary">
                                    You have no assigned courses yet.
                                </Typography>
                            ) : (
                                <List dense>
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
                        </>
                    )}

                    {/* STUDENT – course enrollments & exam registrations */}
                    {role === "STUDENT" && (
                        <>
                            <Divider sx={{my: 2}}/>

                            <Typography variant="h6" gutterBottom>
                                My Enrolled Courses
                            </Typography>
                            {courseEnrollments.length === 0 ? (
                                <Typography variant="body2" color="text.secondary">
                                    You are not enrolled in any courses yet.
                                </Typography>
                            ) : (
                                <List dense>
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

                            <Divider sx={{my: 2}}/>

                            <Typography variant="h6" gutterBottom>
                                My Exam Registrations
                            </Typography>
                            {examRegistrations.length === 0 ? (
                                <Typography variant="body2" color="text.secondary">
                                    You have no exam registrations yet.
                                </Typography>
                            ) : (
                                <List dense>
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
                        </>
                    )}

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
