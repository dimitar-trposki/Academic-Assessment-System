import React, {useMemo, useState} from "react";
import "./CourseGrid.css";

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

import useCourses from "../../../../hooks/useCourses.js";
import CourseCard from "../CourseCard/CourseCard.jsx";
import AddCourseDialog from "../AddCourseDialog/AddCourseDialog.jsx";
import EditCourseDialog from "../EditCourseDialog/EditCourseDialog.jsx";
import DeleteCourseDialog from "../DeleteCourseDialog/DeleteCourseDialog.jsx";
import CourseDetails from "../CourseDetails/CourseDetails.jsx";
import useCourseEnrollments from "../../../../hooks/useCourseEnrollments.js";

const CourseGrid = () => {
    const {
        courses,
        loading,
        onAdd,
        onEdit,
        onDelete,
        findById,
    } = useCourses();

    const {
        exportEnrolledStudentsCsv,
        importEnrolledStudentsCsv,
        getEnrolledStudents,
    } = useCourseEnrollments();

    const [search, setSearch] = useState("");
    const [selectedCourse, setSelectedCourse] = useState(null);

    const [isAddOpen, setIsAddOpen] = useState(false);
    const [isEditOpen, setIsEditOpen] = useState(false);
    const [isDeleteOpen, setIsDeleteOpen] = useState(false);
    const [isDetailsOpen, setIsDetailsOpen] = useState(false);

    const filteredCourses = useMemo(() => {
        const term = search.trim().toLowerCase();
        if (!term) return courses;

        return courses.filter((c) => {
            const name = (c.courseName || "").toLowerCase();
            const code = (c.courseCode || "").toLowerCase();
            return name.includes(term) || code.includes(term);
        });
    }, [courses, search]);

    const handleOpenDetails = (course) => {
        setSelectedCourse(course);
        setIsDetailsOpen(true);
    };

    const handleOpenEdit = (course) => {
        setSelectedCourse(course);
        setIsEditOpen(true);
    };

    const handleOpenDelete = (course) => {
        setSelectedCourse(course);
        setIsDeleteOpen(true);
    };

    const closeAllDialogs = () => {
        setIsAddOpen(false);
        setIsEditOpen(false);
        setIsDeleteOpen(false);
        setIsDetailsOpen(false);
        setSelectedCourse(null);
    };

    return (
        <Box className="course-grid-root">
            {/* Toolbar */}
            <Box className="course-grid-toolbar">
                <TextField
                    size="small"
                    placeholder="Search by name or code..."
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

                    <Tooltip title="Add new course">
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
                        Loading courses...
                    </Typography>
                </Box>
            ) : filteredCourses.length === 0 ? (
                <Box className="course-grid-empty">
                    <Typography variant="h6" gutterBottom>
                        No courses found
                    </Typography>
                    <Typography
                        variant="body2"
                        color="text.secondary"
                        paragraph
                    >
                        Try a different search term or create a new course.
                    </Typography>
                    <Fab
                        variant="extended"
                        color="primary"
                        size="medium"
                        onClick={() => setIsAddOpen(true)}
                    >
                        <AddIcon sx={{mr: 1}}/>
                        Add course
                    </Fab>
                </Box>
            ) : (
                <Grid container spacing={3}>
                    {filteredCourses.map((course) => (
                        <Grid item xs={12} sm={6} md={4} key={course.id}>
                            <Fade in timeout={400}>
                                <div>
                                    <CourseCard
                                        course={course}
                                        onOpenDetails={() =>
                                            handleOpenDetails(course)
                                        }
                                        onEdit={() => handleOpenEdit(course)}
                                        onDelete={() =>
                                            handleOpenDelete(course)
                                        }
                                    />
                                </div>
                            </Fade>
                        </Grid>
                    ))}
                </Grid>
            )}

            {/* Dialogs */}
            <AddCourseDialog
                open={isAddOpen}
                onClose={closeAllDialogs}
                onAdd={onAdd}
            />

            {selectedCourse && (
                <>
                    <EditCourseDialog
                        open={isEditOpen}
                        onClose={closeAllDialogs}
                        course={selectedCourse}
                        onEdit={onEdit}
                    />

                    <DeleteCourseDialog
                        open={isDeleteOpen}
                        onClose={closeAllDialogs}
                        course={selectedCourse}
                        onDelete={onDelete}
                    />

                    <CourseDetails
                        open={isDetailsOpen}
                        onClose={closeAllDialogs}
                        courseId={selectedCourse.id}
                        findById={findById}
                        exportEnrolledStudentsCsv={exportEnrolledStudentsCsv}
                        importEnrolledStudentsCsv={importEnrolledStudentsCsv}
                        getEnrolledStudents={getEnrolledStudents}
                    />
                </>
            )}
        </Box>
    );
};

export default CourseGrid;
