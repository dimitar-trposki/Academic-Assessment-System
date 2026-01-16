// src/ui/components/courses/CourseGrid/CourseGrid.jsx
import React, {useEffect, useMemo, useState} from "react";
import "./CourseGrid.css";

import {
    Box,
    CircularProgress,
    Fab,
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
import useUsers from "../../../../hooks/useUsers.js";

const ROLE_ADMIN = "ADMINISTRATOR";
const ROLE_STAFF = "STAFF";
const ROLE_STUDENT = "STUDENT";
const ROLE_USER = "USER";

const filterBySearch = (list, search) => {
    const term = search.trim().toLowerCase();
    if (!term) return list;

    return list.filter((c) => {
        const name = (c.courseName || "").toLowerCase();
        const code = (c.courseCode || "").toLowerCase();
        return name.includes(term) || code.includes(term);
    });
};

const CourseGrid = () => {
    const {
        courses,
        loading: coursesLoading,
        onAdd,
        onEdit,
        onDelete,
        findById,
        findAllForStaff,
        findAllForStudent,
    } = useCourses();

    const {
        exportEnrolledStudentsCsv,
        importEnrolledStudentsCsv,
        getEnrolledStudents,
    } = useCourseEnrollments();

    const {me} = useUsers();

    const [search, setSearch] = useState("");
    const [selectedCourse, setSelectedCourse] = useState(null);

    const [isAddOpen, setIsAddOpen] = useState(false);
    const [isEditOpen, setIsEditOpen] = useState(false);
    const [isDeleteOpen, setIsDeleteOpen] = useState(false);
    const [isDetailsOpen, setIsDetailsOpen] = useState(false);

    const [role, setRole] = useState(null);

    const [staffCourses, setStaffCourses] = useState([]);
    const [studentCourses, setStudentCourses] = useState([]);

    const [meLoading, setMeLoading] = useState(true);
    const [staffLoading, setStaffLoading] = useState(false);
    const [studentLoading, setStudentLoading] = useState(false);

    useEffect(() => {
        let isMounted = true;

        const load = async () => {
            try {
                setMeLoading(true);
                const user = await me();
                if (!isMounted || !user) return;

                const userRole = user.role;
                setRole(userRole);

                if (userRole === ROLE_STAFF) {
                    setStaffLoading(true);
                    try {
                        const staff = await findAllForStaff();
                        if (isMounted) {
                            setStaffCourses(staff || []);
                        }
                    } finally {
                        if (isMounted) setStaffLoading(false);
                    }
                } else if (userRole === ROLE_STUDENT) {
                    setStudentLoading(true);
                    try {
                        const student = await findAllForStudent();
                        if (isMounted) {
                            setStudentCourses(student || []);
                        }
                    } finally {
                        if (isMounted) setStudentLoading(false);
                    }
                } else {
                    setStaffCourses([]);
                    setStudentCourses([]);
                }
            } catch (e) {
                console.log(e);
                if (isMounted) {
                    setRole(null);
                    setStaffCourses([]);
                    setStudentCourses([]);
                }
            } finally {
                if (isMounted) setMeLoading(false);
            }
        };

        load();

        return () => {
            isMounted = false;
        };
    }, [me, findAllForStaff, findAllForStudent]);

    const isAdmin = role === ROLE_ADMIN;
    const isStaff = role === ROLE_STAFF;
    const isStudent = role === ROLE_STUDENT;
    const isUser = role === ROLE_USER;

    const filteredAllCourses = useMemo(
        () => filterBySearch(courses, search),
        [courses, search]
    );

    const filteredStaffCourses = useMemo(
        () => filterBySearch(staffCourses, search),
        [staffCourses, search]
    );

    const filteredStudentCourses = useMemo(
        () => filterBySearch(studentCourses, search),
        [studentCourses, search]
    );

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

    const isAnyLoading =
        coursesLoading || meLoading || staffLoading || studentLoading;

    return (
        <Box className="course-grid-root">
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
                            sx={{
                                color: "#ffffff",
                                bgcolor: "rgba(255, 255, 255, 0.10)",
                                boxShadow: "0 6px 14px rgba(0,0,0,0.45)",
                                backdropFilter: "blur(6px)",
                                borderRadius: "8px",
                                "&:hover": {
                                    bgcolor: "rgba(255, 255, 255, 0.22)",
                                },
                            }}
                        >
                            <RefreshIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>

                    {(isAdmin || isStaff) && (
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
                    )}
                </Box>
            </Box>

            {isAnyLoading ? (
                <Box className="course-grid-loading">
                    <CircularProgress/>
                    <Typography variant="body2" sx={{mt: 1}}>
                        Loading courses...
                    </Typography>
                </Box>
            ) : filteredAllCourses.length === 0 ? (
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
                    {(isAdmin || isStaff) && (
                        <Fab
                            variant="extended"
                            color="primary"
                            size="medium"
                            onClick={() => setIsAddOpen(true)}
                        >
                            <AddIcon sx={{mr: 1}}/>
                            Add course
                        </Fab>
                    )}
                </Box>
            ) : (
                <Box className="course-grid-content">
                    {isStaff && (
                        <>
                            <Box mb={3}>
                                <Typography variant="h6" gutterBottom>
                                    My assigned courses
                                </Typography>
                                {filteredStaffCourses.length === 0 ? (
                                    <Typography
                                        variant="body2"
                                        color="text.secondary"
                                    >
                                        You are not assigned to any course.
                                    </Typography>
                                ) : (
                                    <Grid container spacing={3}>
                                        {filteredStaffCourses.map((course) => (
                                            <Grid
                                                item
                                                xs={12}
                                                sm={6}
                                                md={6}   // 2 per row on desktop
                                                key={`my-${course.id}`}
                                            >
                                                <CourseCard
                                                    course={course}
                                                    onOpenDetails={() =>
                                                        handleOpenDetails(
                                                            course
                                                        )
                                                    }
                                                    onEdit={() =>
                                                        handleOpenEdit(course)
                                                    }
                                                    onDelete={() =>
                                                        handleOpenDelete(
                                                            course
                                                        )
                                                    }
                                                    showDetails={true}
                                                    showEdit={true}
                                                    showDelete={true}
                                                />
                                            </Grid>
                                        ))}
                                    </Grid>
                                )}
                            </Box>

                            <Box>
                                <Typography variant="h6" gutterBottom>
                                    All courses
                                </Typography>
                                <Grid container spacing={3}>
                                    {filteredAllCourses.map((course) => (
                                        <Grid
                                            item
                                            xs={12}
                                            sm={6}
                                            md={6}   // 2 per row
                                            key={course.id}
                                        >
                                            <CourseCard
                                                course={course}
                                                showDetails={false}
                                                showEdit={false}
                                                showDelete={false}
                                            />
                                        </Grid>
                                    ))}
                                </Grid>
                            </Box>
                        </>
                    )}

                    {isStudent && (
                        <>
                            <Box mb={3}>
                                <Typography variant="h6" gutterBottom>
                                    My enrolled courses
                                </Typography>
                                {filteredStudentCourses.length === 0 ? (
                                    <Typography
                                        variant="body2"
                                        color="text.secondary"
                                    >
                                        You are not enrolled in any course.
                                    </Typography>
                                ) : (
                                    <Grid container spacing={3}>
                                        {filteredStudentCourses.map((course) => (
                                            <Grid
                                                item
                                                xs={12}
                                                sm={6}
                                                md={6}  // 2 per row
                                                key={`my-${course.id}`}
                                            >
                                                <CourseCard
                                                    course={course}
                                                    onOpenDetails={() =>
                                                        handleOpenDetails(
                                                            course
                                                        )
                                                    }
                                                    showDetails={true}
                                                    showEdit={false}
                                                    showDelete={false}
                                                />
                                            </Grid>
                                        ))}
                                    </Grid>
                                )}
                            </Box>

                            <Box>
                                <Typography variant="h6" gutterBottom>
                                    All courses
                                </Typography>
                                <Grid container spacing={3}>
                                    {filteredAllCourses.map((course) => (
                                        <Grid
                                            item
                                            xs={12}
                                            sm={6}
                                            md={6}
                                            key={course.id}
                                        >
                                            <CourseCard
                                                course={course}
                                                showDetails={false}
                                                showEdit={false}
                                                showDelete={false}
                                            />
                                        </Grid>
                                    ))}
                                </Grid>
                            </Box>
                        </>
                    )}

                    {isAdmin && (
                        <Box>
                            <Typography variant="h6" gutterBottom>
                                All courses (manage)
                            </Typography>
                            <Grid container spacing={3}>
                                {filteredAllCourses.map((course) => (
                                    <Grid
                                        item
                                        xs={12}
                                        sm={6}
                                        md={6}
                                        key={course.id}
                                    >
                                        <CourseCard
                                            course={course}
                                            onOpenDetails={() =>
                                                handleOpenDetails(course)
                                            }
                                            onEdit={() =>
                                                handleOpenEdit(course)
                                            }
                                            onDelete={() =>
                                                handleOpenDelete(course)
                                            }
                                            showDetails={true}
                                            showEdit={true}
                                            showDelete={true}
                                        />
                                    </Grid>
                                ))}
                            </Grid>
                        </Box>
                    )}

                    {isUser && (
                        <Box>
                            <Typography variant="h6" gutterBottom>
                                All courses
                            </Typography>
                            <Grid container spacing={3}>
                                {filteredAllCourses.map((course) => (
                                    <Grid
                                        item
                                        xs={12}
                                        sm={6}
                                        md={6}
                                        key={course.id}
                                    >
                                        <CourseCard
                                            course={course}
                                            showDetails={false}
                                            showEdit={false}
                                            showDelete={false}
                                        />
                                    </Grid>
                                ))}
                            </Grid>
                        </Box>
                    )}

                    {!isAdmin && !isStaff && !isStudent && !isUser && (
                        <Box>
                            <Typography variant="h6" gutterBottom>
                                All courses
                            </Typography>
                            <Grid container spacing={3}>
                                {filteredAllCourses.map((course) => (
                                    <Grid
                                        item
                                        xs={12}
                                        sm={6}
                                        md={6}
                                        key={course.id}
                                    >
                                        <CourseCard
                                            course={course}
                                            showDetails={false}
                                            showEdit={false}
                                            showDelete={false}
                                        />
                                    </Grid>
                                ))}
                            </Grid>
                        </Box>
                    )}
                </Box>
            )}

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
