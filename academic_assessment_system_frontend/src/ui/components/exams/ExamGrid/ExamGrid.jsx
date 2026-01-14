// src/ui/components/exams/ExamGrid/ExamGrid.jsx
import React, {useMemo, useState} from "react";
import "./ExamGrid.css";

import {
    Box,
    CircularProgress,
    Fab,
    Fade,
    Grid,
    IconButton,
    InputAdornment,
    TextField,
    Tooltip,
    Typography,
} from "@mui/material";

import SearchIcon from "@mui/icons-material/Search";
import RefreshIcon from "@mui/icons-material/Refresh";
import AddIcon from "@mui/icons-material/Add";

import useExams from "../../../../hooks/useExams.js";
import ExamCard from "../ExamCard/ExamCard.jsx";
import AddExamDialog from "../AddExamDialog/AddExamDialog.jsx";
import EditExamDialog from "../EditExamDialog/EditExamDialog.jsx";
import DeleteExamDialog from "../DeleteExamDialog/DeleteExamDialog.jsx";
import ExamDetails from "../ExamDetails/ExamDetails.jsx";
import useStudentExamRegistrations from "../../../../hooks/useStudentExamRegistrations.js";

const ExamGrid = () => {
    const {
        exams,
        loading,
        onAdd,
        onEdit,
        onDelete,
        findById,
    } = useExams();

    const {
        registerForExam,
        exportRegisteredStudents,
        importAttendedStudents,
        exportAttendedStudents,
        exportAbsentStudents,
        getRegisteredStudents,
        getAttendedStudents,
        getAbsentStudents,
    } = useStudentExamRegistrations();

    // нормализирај како кај courses
    const allExams = Array.isArray(exams)
        ? exams
        : Array.isArray(exams?.exams)
            ? exams.exams
            : Array.isArray(exams?.data)
                ? exams.data
                : [];

    const [search, setSearch] = useState("");
    const [selectedExam, setSelectedExam] = useState(null);

    const [isAddOpen, setIsAddOpen] = useState(false);
    const [isEditOpen, setIsEditOpen] = useState(false);
    const [isDeleteOpen, setIsDeleteOpen] = useState(false);
    const [isDetailsOpen, setIsDetailsOpen] = useState(false);

    const filteredExams = useMemo(() => {
        const term = search.trim().toLowerCase();
        if (!term) return allExams;

        return allExams.filter((e) => {
            const courseName = (e.course?.courseName || "").toLowerCase();
            const courseCode = (e.course?.courseCode || "").toLowerCase();
            const session = (e.session || "").toLowerCase();
            return (
                courseName.includes(term) ||
                courseCode.includes(term) ||
                session.includes(term)
            );
        });
    }, [allExams, search]);

    const handleOpenDetails = (exam) => {
        setSelectedExam(exam);
        setIsDetailsOpen(true);
    };

    const handleOpenEdit = (exam) => {
        setSelectedExam(exam);
        setIsEditOpen(true);
    };

    const handleOpenDelete = (exam) => {
        setSelectedExam(exam);
        setIsDeleteOpen(true);
    };

    const closeAllDialogs = () => {
        setIsAddOpen(false);
        setIsEditOpen(false);
        setIsDeleteOpen(false);
        setIsDetailsOpen(false);
        setSelectedExam(null);
    };

    return (
        // ✅ исти класи како CourseGrid
        <Box className="course-grid-root">
            {/* Toolbar */}
            <Box className="course-grid-toolbar">
                <TextField
                    size="small"
                    placeholder="Search by course, code or session..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="course-grid-search"
                    InputProps={{
                        startAdornment: (
                            <InputAdornment position="start">
                                <SearchIcon fontSize="small"/>
                            </InputAdornment>
                        ),
                    }}
                />

                <Box className="course-grid-toolbar-actions">
                    <Tooltip title="Reload page">
                        <IconButton
                            size="small"
                            onClick={() => window.location.reload()}
                        >
                            <RefreshIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>

                    <Tooltip title="Add new exam">
                        <Fab
                            size="small"
                            color="primary"
                            className="course-grid-add-fab"
                            onClick={() => setIsAddOpen(true)}
                        >
                            <AddIcon/>
                        </Fab>
                    </Tooltip>
                </Box>
            </Box>

            {/* Content */}
            {loading ? (
                <Box className="course-grid-loading">
                    <CircularProgress/>
                    <Typography variant="body2" sx={{mt: 1}}>
                        Loading exams...
                    </Typography>
                </Box>
            ) : filteredExams.length === 0 ? (
                <Box className="course-grid-empty">
                    <Typography variant="h6" gutterBottom>
                        No exams found
                    </Typography>
                    <Typography
                        variant="body2"
                        color="text.secondary"
                        paragraph
                    >
                        Try a different search term or create a new exam.
                    </Typography>
                    <Fab
                        variant="extended"
                        color="primary"
                        size="medium"
                        onClick={() => setIsAddOpen(true)}
                    >
                        <AddIcon sx={{mr: 1}}/>
                        Add exam
                    </Fab>
                </Box>
            ) : (
                <Grid container spacing={3}>
                    {filteredExams.map((exam) => (
                        <Grid item xs={12} sm={6} md={4} key={exam.id}>
                            <Fade in timeout={400}>
                                <div>
                                    <ExamCard
                                        exam={exam}
                                        onOpenDetails={() =>
                                            handleOpenDetails(exam)
                                        }
                                        onEdit={() => handleOpenEdit(exam)}
                                        onDelete={() =>
                                            handleOpenDelete(exam)
                                        }
                                    />
                                </div>
                            </Fade>
                        </Grid>
                    ))}
                </Grid>
            )}

            {/* Dialogs */}
            <AddExamDialog
                open={isAddOpen}
                onClose={closeAllDialogs}
                onAdd={onAdd}
            />

            {selectedExam && (
                <>
                    <EditExamDialog
                        open={isEditOpen}
                        onClose={closeAllDialogs}
                        exam={selectedExam}
                        onEdit={onEdit}
                    />

                    <DeleteExamDialog
                        open={isDeleteOpen}
                        onClose={closeAllDialogs}
                        exam={selectedExam}
                        onDelete={onDelete}
                    />

                    <ExamDetails
                        open={isDetailsOpen}
                        onClose={closeAllDialogs}
                        examId={selectedExam.id}
                        findById={findById}
                        registerForExam={registerForExam}
                        exportRegisteredStudents={exportRegisteredStudents}
                        importAttendedStudents={importAttendedStudents}
                        exportAttendedStudents={exportAttendedStudents}
                        exportAbsentStudents={exportAbsentStudents}
                        getRegisteredStudents={getRegisteredStudents}
                        getAttendedStudents={getAttendedStudents}
                        getAbsentStudents={getAbsentStudents}
                    />
                </>
            )}
        </Box>
    );
};

export default ExamGrid;
