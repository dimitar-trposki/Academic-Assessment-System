import React, {useEffect, useState, useCallback} from "react";
import "./ExamDetails.css";

import {
    Box,
    Button,
    Chip,
    Dialog,
    DialogContent,
    DialogTitle,
    Divider,
    Grid,
    Stack, Tooltip,
    Typography,
} from "@mui/material";

import PeopleAltOutlinedIcon from "@mui/icons-material/PeopleAltOutlined";
import DownloadIcon from "@mui/icons-material/Download";
import UploadIcon from "@mui/icons-material/Upload";
import PersonOutlineIcon from "@mui/icons-material/PersonOutline";
import RoomOutlinedIcon from "@mui/icons-material/RoomOutlined";
import AccessTimeIcon from "@mui/icons-material/AccessTime";

import useUsers from "../../../../hooks/useUsers.js";

const ROLE_ADMIN = "ADMINISTRATOR";
const ROLE_STAFF = "STAFF";
const ROLE_STUDENT = "STUDENT";
const ROLE_USER = "USER";

const normalizeTime = (t) => {
    if (!t) return "";
    return t.substring(0, 5);
};

const ExamDetails = ({
                         open,
                         onClose,
                         examId,
                         findById,
                         registerForExam,
                         exportRegisteredStudents,
                         importAttendedStudents,
                         exportAttendedStudents,
                         exportAbsentStudents,
                         getRegisteredStudents,
                         getAttendedStudents,
                         getAbsentStudents,
                     }) => {
    const [details, setDetails] = useState(null);

    const [registered, setRegistered] = useState([]);
    const [attended, setAttended] = useState([]);
    const [absent, setAbsent] = useState([]);

    const [uploadFile, setUploadFile] = useState(null);

    const {me} = useUsers();
    const [role, setRole] = useState(null);

    const reloadExamData = useCallback(() => {
        if (!examId) return;

        findById(examId).then((data) => {
            if (data) setDetails(data);
        });

        getRegisteredStudents(examId).then((data) =>
            setRegistered(data || [])
        );
        getAttendedStudents(examId).then((data) =>
            setAttended(data || [])
        );
        getAbsentStudents(examId).then((data) =>
            setAbsent(data || [])
        );
    }, [examId, findById, getRegisteredStudents, getAttendedStudents, getAbsentStudents]);

    useEffect(() => {
        if (!open) return;

        reloadExamData();

        me()
            .then((user) => {
                setRole(user?.role || null);
            })
            .catch((err) => {
                console.log(err);
                setRole(null);
            });
    }, [open, reloadExamData, me]);

    const isAdmin = role === ROLE_ADMIN;
    const isStaff = role === ROLE_STAFF;
    const isStudent = role === ROLE_STUDENT;
    const isUser = role === ROLE_USER;

    const code =
        details?.course?.courseCode ||
        details?.courseCode ||
        "exam";

    const handleExportHelper = (fn, defaultName) => {
        fn(examId)
            .then((response) => {
                if (!response) return;
                const blob = new Blob([response.data], {type: "text/csv"});
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement("a");
                link.href = url;
                link.download = defaultName;
                document.body.appendChild(link);
                link.click();
                link.remove();
                window.URL.revokeObjectURL(url);
            })
            .catch((err) => console.error("Error exporting CSV", err));
    };

    const handleExportRegistered = () => {
        handleExportHelper(
            exportRegisteredStudents,
            `exam_${code}_registeredStudents.csv`
        );
    };

    const handleExportAttended = () => {
        handleExportHelper(
            exportAttendedStudents,
            `exam_${code}_attendedStudents.csv`
        );
    };

    const handleExportAbsent = () => {
        handleExportHelper(
            exportAbsentStudents,
            `exam_${code}_absentStudents.csv`
        );
    };

    const handleFileChange = (event) => {
        const file = event.target.files?.[0];
        setUploadFile(file || null);
    };

    const handleImportAttended = () => {
        if (!uploadFile) return;

        const formData = new FormData();
        formData.append("file", uploadFile);

        importAttendedStudents(examId, formData).then(() => {
            setUploadFile(null);
            reloadExamData();
        });
    };

    const handleRegister = () => {
        if (!isStudent) return;
        registerForExam(examId)
            .then(() => {
                reloadExamData();
            })
            .catch((err) => {
                console.error("Failed to register for exam", err);
            });
    };

    const courseName =
        details?.course?.courseName || details?.courseName || "Exam";
    const date = details?.dateOfExam || "";
    const startTime = normalizeTime(details?.startTime);
    const endTime = normalizeTime(details?.endTime);
    const capacity = details?.capacityOfStudents ?? "-";
    const labs =
        details?.reservedLaboratories && details.reservedLaboratories.length > 0
            ? details.reservedLaboratories.join(", ")
            : "No labs reserved";

    const formatStudentLabel = (s) =>
        `${s.studentIndex} (${s.examStatus})`;

    const isLoadingDetails = !details;

    return (
        <Dialog
            open={open}
            onClose={onClose}
            maxWidth="md"
            fullWidth
            className="dialog-theme course-dialog-root exam-details-dialog"
        >
            <DialogTitle>
                <Stack
                    direction="row"
                    spacing={1}
                    alignItems="center"
                    justifyContent="space-between"
                >
                    <Box>
                        <Typography variant="overline" gutterBottom>
                            {code}
                        </Typography>
                        <Typography variant="h6" fontWeight={600}>
                            {courseName}
                        </Typography>
                        <Stack
                            direction="row"
                            spacing={1}
                            alignItems="center"
                            sx={{mt: 0.5}}
                        >
                            <AccessTimeIcon fontSize="small"/>
                            <Typography variant="body2">
                                {date || "No date"}{" "}
                                {startTime &&
                                    `· ${startTime} - ${endTime || "?"}`}
                            </Typography>
                            <RoomOutlinedIcon fontSize="small"/>
                            <Typography variant="body2">{labs}</Typography>
                        </Stack>
                    </Box>

                    <Stack
                        direction="column"
                        spacing={0.75}
                        alignItems="flex-end"
                    >
                        <Chip
                            size="small"
                            label={`Capacity: ${capacity}`}
                            color="primary"
                            variant="outlined"
                        />

                        <Typography
                            variant="caption"
                            sx={{color: "rgba(148, 163, 184, 0.95)"}}
                        >
                            Registered: {registered.length} student
                            {registered.length === 1 ? "" : "s"}
                        </Typography>

                        <Button
                            size="small"
                            variant="contained"
                            onClick={handleRegister}
                            disabled={!isStudent}
                            sx={{
                                mt: 0.25,
                                borderRadius: "999px",
                                px: 2.5,
                                textTransform: "none",
                                fontWeight: 600,
                            }}
                        >
                            Register for exam
                        </Button>
                    </Stack>
                </Stack>
            </DialogTitle>

            <DialogContent dividers>
                {isLoadingDetails ? (
                    <Typography variant="body2" color="text.secondary">
                        Loading exam details…
                    </Typography>
                ) : (
                    <>
                        {isAdmin || isStaff || isStudent ? (
                            <Grid container spacing={3}>
                                {(isAdmin || isStaff) && (
                                    <Grid item xs={12} md={6}>
                                        <Typography
                                            variant="subtitle2"
                                            color="text.secondary"
                                            gutterBottom
                                        >
                                            Registrations & attendance
                                        </Typography>

                                        <Stack
                                            direction="row"
                                            spacing={1}
                                            alignItems="center"
                                            sx={{mb: 1}}
                                        >
                                            <PeopleAltOutlinedIcon fontSize="small"/>
                                            <Typography variant="body2">
                                                {registered.length} registered ·{" "}
                                                {attended.length} attended ·{" "}
                                                {absent.length} absent
                                            </Typography>
                                        </Stack>

                                        <Stack direction="row" spacing={1} sx={{mb: 1}}>
                                            <Button
                                                size="small"
                                                variant="contained"
                                                startIcon={<DownloadIcon/>}
                                                onClick={handleExportRegistered}
                                            >
                                                Registered CSV
                                            </Button>

                                            <Button
                                                size="small"
                                                variant="outlined"
                                                startIcon={<DownloadIcon/>}
                                                onClick={handleExportAttended}
                                            >
                                                Attended CSV
                                            </Button>

                                            <Button
                                                size="small"
                                                variant="outlined"
                                                startIcon={<DownloadIcon/>}
                                                onClick={handleExportAbsent}
                                            >
                                                Absent CSV
                                            </Button>
                                        </Stack>

                                        <Divider sx={{my: 2}}/>

                                        <Typography
                                            variant="subtitle2"
                                            color="text.secondary"
                                            gutterBottom
                                        >
                                            Import attended students
                                        </Typography>

                                        <Stack direction="row" spacing={1} sx={{mb: 1}}>
                                            <Button
                                                size="small"
                                                variant="outlined"
                                                startIcon={<UploadIcon/>}
                                                onClick={handleImportAttended}
                                                disabled={!uploadFile}
                                            >
                                                Import attended CSV
                                            </Button>
                                        </Stack>

                                        <Tooltip
                                            arrow
                                            title={
                                                "CSV format:\n" +
                                                "• Required header: studentIndex\n" +
                                                "• Each row contains ONE student index\n\n" +
                                                "Example:\n" +
                                                "studentIndex\n" +
                                                "123456\n" +
                                                "654321"
                                            }
                                            slotProps={{
                                                tooltip: {
                                                    sx: {whiteSpace: "pre-line", fontSize: "0.8rem"}
                                                }
                                            }}
                                        >
                                            <Button component="label" variant="text" size="small" sx={{p: 0}}>
                                                <Typography
                                                    variant="body2"
                                                    color="primary"
                                                    sx={{textTransform: "none"}}
                                                >
                                                    Choose CSV file…
                                                </Typography>
                                                <input
                                                    type="file"
                                                    accept=".csv"
                                                    hidden
                                                    onChange={handleFileChange}
                                                />
                                            </Button>
                                        </Tooltip>
                                        {uploadFile && (
                                            <Typography
                                                variant="caption"
                                                color="text.secondary"
                                                sx={{display: "block", mt: 0.3}}
                                            >
                                                Selected: {uploadFile.name}
                                            </Typography>
                                        )}
                                    </Grid>
                                )}

                                {(isAdmin || isStaff || isStudent) && (
                                    <Grid
                                        item
                                        xs={12}
                                        md={isAdmin || isStaff ? 6 : 12}
                                    >
                                        <Typography
                                            variant="subtitle2"
                                            color="text.secondary"
                                            gutterBottom
                                        >
                                            Registered students
                                        </Typography>
                                        {registered.length ? (
                                            <Stack spacing={0.5} sx={{mb: 2}}>
                                                {registered.map((r) => (
                                                    <Stack
                                                        key={r.id}
                                                        direction="row"
                                                        spacing={1}
                                                        alignItems="center"
                                                    >
                                                        <PersonOutlineIcon fontSize="small"/>
                                                        <Typography variant="body2">
                                                            {formatStudentLabel(r)}
                                                        </Typography>
                                                    </Stack>
                                                ))}
                                            </Stack>
                                        ) : (
                                            <Typography
                                                variant="body2"
                                                color="text.secondary"
                                                sx={{mb: 2}}
                                            >
                                                No registered students.
                                            </Typography>
                                        )}

                                        <Typography
                                            variant="subtitle2"
                                            color="text.secondary"
                                            gutterBottom
                                        >
                                            Attended students
                                        </Typography>
                                        {attended.length ? (
                                            <Stack spacing={0.5} sx={{mb: 2}}>
                                                {attended.map((r) => (
                                                    <Stack
                                                        key={r.id}
                                                        direction="row"
                                                        spacing={1}
                                                        alignItems="center"
                                                    >
                                                        <PersonOutlineIcon fontSize="small"/>
                                                        <Typography variant="body2">
                                                            {formatStudentLabel(r)}
                                                        </Typography>
                                                    </Stack>
                                                ))}
                                            </Stack>
                                        ) : (
                                            <Typography
                                                variant="body2"
                                                color="text.secondary"
                                                sx={{mb: 2}}
                                            >
                                                No attended students.
                                            </Typography>
                                        )}

                                        <Typography
                                            variant="subtitle2"
                                            color="text.secondary"
                                            gutterBottom
                                        >
                                            Absent students
                                        </Typography>
                                        {absent.length ? (
                                            <Stack spacing={0.5}>
                                                {absent.map((r) => (
                                                    <Stack
                                                        key={r.id}
                                                        direction="row"
                                                        spacing={1}
                                                        alignItems="center"
                                                    >
                                                        <PersonOutlineIcon fontSize="small"/>
                                                        <Typography variant="body2">
                                                            {formatStudentLabel(r)}
                                                        </Typography>
                                                    </Stack>
                                                ))}
                                            </Stack>
                                        ) : (
                                            <Typography
                                                variant="body2"
                                                color="text.secondary"
                                            >
                                                No absent students.
                                            </Typography>
                                        )}
                                    </Grid>
                                )}
                            </Grid>
                        ) : (
                            <Typography variant="body2" color="text.secondary">
                                No additional exam data available for your role.
                            </Typography>
                        )}
                    </>
                )}
            </DialogContent>
        </Dialog>
    );
};

export default ExamDetails;
