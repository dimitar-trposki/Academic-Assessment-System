// src/ui/components/courses/CourseDetails/CourseDetails.jsx
import React, {useEffect, useState} from "react";
import "./CourseDetails.css";

import {
    Box,
    Button,
    Chip,
    Dialog,
    DialogContent,
    DialogTitle,
    Divider,
    Grid,
    Stack,
    Typography,
} from "@mui/material";

import PeopleAltOutlinedIcon from "@mui/icons-material/PeopleAltOutlined";
import DownloadIcon from "@mui/icons-material/Download";
import UploadIcon from "@mui/icons-material/Upload";
import PersonOutlineIcon from "@mui/icons-material/PersonOutline";

const CourseDetails = ({
                           open,
                           onClose,
                           courseId,
                           findById,
                           exportEnrolledStudentsCsv,
                           importEnrolledStudentsCsv,
                           getEnrolledStudents,
                       }) => {
    const [details, setDetails] = useState(null);
    const [students, setStudents] = useState([]);
    const [uploadFile, setUploadFile] = useState(null);

    useEffect(() => {
        if (!open || !courseId) return;

        findById(courseId).then((data) => {
            if (data) setDetails(data);
        });

        getEnrolledStudents(courseId).then((res) => {
            setStudents(res?.data || res || []);
        });
    }, [open, courseId, findById, getEnrolledStudents]);

    const handleExportCsv = () => {
        exportEnrolledStudentsCsv(courseId)
            .then((response) => {
                if (!response) return;
                const blob = new Blob([response.data], {type: "text/csv"});
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement("a");
                const code = details?.courseCode || "course";

                link.href = url;
                link.download = `course_${code}_students.csv`;
                document.body.appendChild(link);
                link.click();
                link.remove();
                window.URL.revokeObjectURL(url);
            })
            .catch((err) => {
                console.error("Error exporting CSV", err);
            });
    };

    const handleFileChange = (event) => {
        const file = event.target.files?.[0];
        setUploadFile(file || null);
    };

    const handleImportCsv = () => {
        if (!uploadFile) return;

        const formData = new FormData();
        formData.append("file", uploadFile);
        importEnrolledStudentsCsv(courseId, formData).then(() => {
            console.log("Imported CSV for course", courseId);
            setUploadFile(null);

            // reload students after import
            getEnrolledStudents(courseId).then((res) => {
                setStudents(res?.data || res || []);
            });
        });
    };

    if (!details) {
        return null;
    }

    const {courseCode, courseName, semester, academicYear, professors, assistants} =
        details;

    const formatName = (u) =>
        u.fullName ||
        [u.firstName, u.lastName].filter(Boolean).join(" ") ||
        u.email ||
        `User #${u.id}`;

    return (
        <Dialog
            open={open}
            onClose={onClose}
            maxWidth="md"
            fullWidth
            className="course-details-dialog"
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
                            {courseCode}
                        </Typography>
                        <Typography variant="h6" fontWeight={600}>
                            {courseName}
                        </Typography>
                    </Box>
                    <Stack direction="row" spacing={1}>
                        <Chip
                            size="small"
                            label={`Semester ${semester}`}
                            color="primary"
                            variant="outlined"
                        />
                        <Chip
                            size="small"
                            label={`${academicYear}/${academicYear + 1}`}
                            variant="outlined"
                        />
                    </Stack>
                </Stack>
            </DialogTitle>
            <DialogContent dividers>
                <Grid container spacing={3}>
                    {/* Info + students */}
                    <Grid item xs={12} md={6}>
                        <Typography
                            variant="subtitle2"
                            color="text.secondary"
                            gutterBottom
                        >
                            Course info
                        </Typography>
                        <Stack spacing={0.7}>
                            <Typography variant="body2">
                                <strong>Code:</strong> {courseCode}
                            </Typography>
                            <Typography variant="body2">
                                <strong>Semester:</strong> {semester}
                            </Typography>
                            <Typography variant="body2">
                                <strong>Academic year:</strong> {academicYear} /{" "}
                                {academicYear + 1}
                            </Typography>
                        </Stack>

                        <Divider sx={{my: 2}}/>

                        <Typography
                            variant="subtitle2"
                            color="text.secondary"
                            gutterBottom
                        >
                            Enrolled students
                        </Typography>

                        <Stack
                            direction="row"
                            spacing={1}
                            alignItems="center"
                            sx={{mb: 1}}
                        >
                            <PeopleAltOutlinedIcon fontSize="small"/>
                            <Typography variant="body2">
                                {students.length} students enrolled
                            </Typography>
                        </Stack>

                        <Stack direction="row" spacing={1} sx={{mb: 1}}>
                            <Button
                                size="small"
                                variant="contained"
                                startIcon={<DownloadIcon/>}
                                onClick={handleExportCsv}
                            >
                                Export CSV
                            </Button>

                            <Button
                                size="small"
                                variant="outlined"
                                startIcon={<UploadIcon/>}
                                onClick={handleImportCsv}
                                disabled={!uploadFile}
                            >
                                Import CSV
                            </Button>
                        </Stack>

                        <Button
                            component="label"
                            variant="text"
                            size="small"
                            sx={{p: 0}}
                        >
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

                    {/* Staff */}
                    <Grid item xs={12} md={6}>
                        <Typography
                            variant="subtitle2"
                            color="text.secondary"
                            gutterBottom
                        >
                            Professors
                        </Typography>
                        {professors?.length ? (
                            <Stack spacing={0.5} sx={{mb: 2}}>
                                {professors.map((p) => (
                                    <Stack
                                        key={p.id}
                                        direction="row"
                                        spacing={1}
                                        alignItems="center"
                                    >
                                        <PersonOutlineIcon fontSize="small"/>
                                        <Typography variant="body2">
                                            {formatName(p)}
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
                                No professors assigned.
                            </Typography>
                        )}

                        <Typography
                            variant="subtitle2"
                            color="text.secondary"
                            gutterBottom
                        >
                            Assistants
                        </Typography>
                        {assistants?.length ? (
                            <Stack spacing={0.5}>
                                {assistants.map((a) => (
                                    <Stack
                                        key={a.id}
                                        direction="row"
                                        spacing={1}
                                        alignItems="center"
                                    >
                                        <PersonOutlineIcon fontSize="small"/>
                                        <Typography variant="body2">
                                            {formatName(a)}
                                        </Typography>
                                    </Stack>
                                ))}
                            </Stack>
                        ) : (
                            <Typography variant="body2" color="text.secondary">
                                No assistants assigned.
                            </Typography>
                        )}
                    </Grid>
                </Grid>
            </DialogContent>
        </Dialog>
    );
};

export default CourseDetails;
