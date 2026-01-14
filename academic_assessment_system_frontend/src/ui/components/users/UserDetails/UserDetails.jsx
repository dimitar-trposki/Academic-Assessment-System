// src/components/users/UserDetails/UserDetails.jsx

import React, {useEffect, useState} from "react";
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Typography,
    Box,
    Stack,
    Divider,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    CircularProgress,
} from "@mui/material";

import useStudents from "../../../../hooks/useStudents.js";

/**
 * Format a camelCase / lower_snake key into a nicer header:
 * examName -> Exam Name, course_code -> Course Code
 */
const formatHeader = (key) => {
    if (!key) return "";
    // remove underscores
    const noUnderscore = key.replace(/_/g, " ");
    // insert space before capital letters
    const spaced = noUnderscore.replace(/([A-Z])/g, " $1");
    // capitalize first letter
    return spaced.charAt(0).toUpperCase() + spaced.slice(1);
};

/**
 * Generic mini table that renders any array of objects.
 * It automatically creates columns from the keys of the first element.
 * Hides id-like fields (id, studentId, examId, etc.).
 */
const GenericMiniTable = ({title, data}) => {
    if (!data || data.length === 0) {
        return (
            <Box mt={2}>
                <Typography variant="subtitle1" gutterBottom>
                    {title}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    No data.
                </Typography>
            </Box>
        );
    }

    // Remove id-like fields from columns
    const columns = Object.keys(data[0]).filter(
        (col) => !col.toLowerCase().includes("id")
    );

    if (columns.length === 0) {
        return (
            <Box mt={2}>
                <Typography variant="subtitle1" gutterBottom>
                    {title}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    No visible fields to display.
                </Typography>
            </Box>
        );
    }

    return (
        <Box mt={2}>
            <Typography variant="subtitle1" gutterBottom>
                {title}
            </Typography>
            <TableContainer component={Paper} variant="outlined">
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            {columns.map((col) => (
                                <TableCell key={col}>
                                    <Typography
                                        variant="caption"
                                        sx={{fontWeight: 600}}
                                    >
                                        {formatHeader(col)}
                                    </Typography>
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((row, idx) => (
                            <TableRow key={idx}>
                                {columns.map((col) => (
                                    <TableCell key={col}>
                                        {row[col] !== null &&
                                        row[col] !== undefined
                                            ? String(row[col])
                                            : ""}
                                    </TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

const UserDetails = ({open, onClose, user, student}) => {
    const {
        findStudentExamRegistrationByStudentId,
        findCourseEnrollmentByStudentId,
    } = useStudents();

    const [examRegistrations, setExamRegistrations] = useState([]);
    const [courseEnrollments, setCourseEnrollments] = useState([]);
    const [loadingRelations, setLoadingRelations] = useState(false);

    // Whenever dialog opens for a student, load their exams + courses
    useEffect(() => {
        if (!open || !student) {
            setExamRegistrations([]);
            setCourseEnrollments([]);
            setLoadingRelations(false);
            return;
        }

        const loadData = async () => {
            try {
                setLoadingRelations(true);
                const [exams, courses] = await Promise.all([
                    findStudentExamRegistrationByStudentId(student.id),
                    findCourseEnrollmentByStudentId(student.id),
                ]);
                setExamRegistrations(exams || []);
                setCourseEnrollments(courses || []);
            } catch (e) {
                console.error(
                    "Failed to load student exam registrations / enrollments",
                    e,
                );
            } finally {
                setLoadingRelations(false);
            }
        };

        loadData();
    }, [
        open,
        student,
        findStudentExamRegistrationByStudentId,
        findCourseEnrollmentByStudentId,
    ]);

    const handleClose = () => {
        onClose();
    };

    // Simple helpers to show who we’re displaying
    const isShowingStudent = Boolean(student);
    const isShowingUser = Boolean(user) && !student;

    return (
        <Dialog open={open} onClose={handleClose} fullWidth maxWidth="md">
            <DialogTitle>User details</DialogTitle>
            <DialogContent dividers>
                {/* USER (STAFF) DETAILS */}
                {isShowingUser && (
                    <Box mb={2}>
                        <Typography variant="subtitle1" gutterBottom>
                            Basic info
                        </Typography>
                        <Stack spacing={0.5}>
                            <Typography variant="body2">
                                <strong>Name:</strong> {user.firstName}{" "}
                                {user.lastName}
                            </Typography>
                            <Typography variant="body2">
                                <strong>Email:</strong> {user.email}
                            </Typography>
                            <Typography variant="body2">
                                <strong>Academic role:</strong>{" "}
                                {user.userRole}
                            </Typography>
                        </Stack>
                    </Box>
                )}

                {/* STUDENT DETAILS */}
                {isShowingStudent && (
                    <>
                        <Box mb={2}>
                            <Typography variant="subtitle1" gutterBottom>
                                Student info
                            </Typography>
                            <Stack spacing={0.5}>
                                <Typography variant="body2">
                                    <strong>Name:</strong>{" "}
                                    {student.studentFirstName}{" "}
                                    {student.studentLastName}
                                </Typography>
                                <Typography variant="body2">
                                    <strong>Email:</strong>{" "}
                                    {student.studentEmail}
                                </Typography>
                                <Typography variant="body2">
                                    <strong>Index:</strong>{" "}
                                    {student.studentIndex}
                                </Typography>
                                <Typography variant="body2">
                                    <strong>Major:</strong> {student.major}
                                </Typography>
                            </Stack>
                        </Box>

                        <Divider sx={{my: 2}}/>

                        {loadingRelations ? (
                            <Box
                                mt={2}
                                display="flex"
                                justifyContent="center"
                                alignItems="center"
                            >
                                <CircularProgress size={24}/>
                            </Box>
                        ) : (
                            <>
                                {/* Exam Registrations */}
                                <GenericMiniTable
                                    title="Exam registrations"
                                    data={examRegistrations}
                                />

                                {/* Course Enrollments */}
                                <GenericMiniTable
                                    title="Course enrollments"
                                    data={courseEnrollments}
                                />
                            </>
                        )}
                    </>
                )}

                {/* Fallback if neither user nor student is selected */}
                {!isShowingUser && !isShowingStudent && (
                    <Typography variant="body2" color="text.secondary">
                        No user selected.
                    </Typography>
                )}
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
};

export default UserDetails;
