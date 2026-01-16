import React, {useEffect, useMemo, useState} from "react";
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
import useUsers from "../../../../hooks/useUsers.js";

const ROLE_ADMIN = "ADMINISTRATOR";
const ROLE_STAFF = "STAFF";
const ROLE_STUDENT = "STUDENT";
const ROLE_USER = "USER";

const filterExamsBySearch = (list, search) => {
    const term = search.trim().toLowerCase();
    if (!term) return list;

    return list.filter((e) => {
        const courseName = (e.course?.courseName || "").toLowerCase();
        const courseCode = (e.course?.courseCode || "").toLowerCase();
        const session = (e.session || "").toLowerCase();
        return (
            courseName.includes(term) ||
            courseCode.includes(term) ||
            session.includes(term)
        );
    });
};

const ExamGrid = () => {
    const {
        exams,
        loading: examsLoading,
        onAdd,
        onEdit,
        onDelete,
        findById,
        findAllExamsForStaff,
        findAllExamsForStudent,
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

    const {me} = useUsers();

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

    const [role, setRole] = useState(null);
    const [staffExams, setStaffExams] = useState([]);
    const [studentExams, setStudentExams] = useState([]);

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
                        const staff = await findAllExamsForStaff();
                        if (isMounted) {
                            setStaffExams(staff || []);
                        }
                    } finally {
                        if (isMounted) setStaffLoading(false);
                    }
                } else if (userRole === ROLE_STUDENT) {
                    setStudentLoading(true);
                    try {
                        const student = await findAllExamsForStudent();
                        if (isMounted) {
                            setStudentExams(student || []);
                        }
                    } finally {
                        if (isMounted) setStudentLoading(false);
                    }
                } else {
                    setStaffExams([]);
                    setStudentExams([]);
                }
            } catch (e) {
                console.log(e);
                if (isMounted) {
                    setRole(null);
                    setStaffExams([]);
                    setStudentExams([]);
                }
            } finally {
                if (isMounted) setMeLoading(false);
            }
        };

        load();

        return () => {
            isMounted = false;
        };
    }, [me, findAllExamsForStaff, findAllExamsForStudent]);

    const isAdmin = role === ROLE_ADMIN;
    const isStaff = role === ROLE_STAFF;
    const isStudent = role === ROLE_STUDENT;
    const isUser = role === ROLE_USER;

    const filteredAllExams = useMemo(
        () => filterExamsBySearch(allExams, search),
        [allExams, search]
    );

    const filteredStaffExams = useMemo(
        () => filterExamsBySearch(staffExams, search),
        [staffExams, search]
    );

    const filteredStudentExams = useMemo(
        () => filterExamsBySearch(studentExams, search),
        [studentExams, search]
    );

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

    const isAnyLoading =
        examsLoading || meLoading || staffLoading || studentLoading;

    return (
        <Box className="course-grid-root">
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
                    )}
                </Box>
            </Box>

            {isAnyLoading ? (
                <Box className="course-grid-loading">
                    <CircularProgress/>
                    <Typography variant="body2" sx={{mt: 1}}>
                        Loading exams...
                    </Typography>
                </Box>
            ) : filteredAllExams.length === 0 ? (
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
                    {(isAdmin || isStaff) && (
                        <Fab
                            variant="extended"
                            color="primary"
                            size="medium"
                            onClick={() => setIsAddOpen(true)}
                        >
                            <AddIcon sx={{mr: 1}}/>
                            Add exam
                        </Fab>
                    )}
                </Box>
            ) : (
                <Box>
                    {isStaff && (
                        <>
                            <Box mb={3}>
                                <Typography variant="h6" gutterBottom>
                                    My exams
                                </Typography>
                                {filteredStaffExams.length === 0 ? (
                                    <Typography
                                        variant="body2"
                                        color="text.secondary"
                                    >
                                        You have no exams scheduled for your courses.
                                    </Typography>
                                ) : (
                                    <Grid container spacing={3}>
                                        {filteredStaffExams.map((exam) => (
                                            <Grid
                                                item
                                                xs={12}
                                                sm={6}
                                                md={4}
                                                key={`my-${exam.id}`}
                                            >
                                                <Fade in timeout={400}>
                                                    <div>
                                                        <ExamCard
                                                            exam={exam}
                                                            onOpenDetails={() =>
                                                                handleOpenDetails(exam)
                                                            }
                                                            onEdit={() =>
                                                                handleOpenEdit(exam)
                                                            }
                                                            onDelete={() =>
                                                                handleOpenDelete(exam)
                                                            }
                                                            showDetails={true}
                                                            showEdit={true}
                                                            showDelete={true}
                                                        />
                                                    </div>
                                                </Fade>
                                            </Grid>
                                        ))}
                                    </Grid>
                                )}
                            </Box>

                            <Box>
                                <Typography variant="h6" gutterBottom>
                                    All exams
                                </Typography>
                                <Grid container spacing={3}>
                                    {filteredAllExams.map((exam) => (
                                        <Grid
                                            item
                                            xs={12}
                                            sm={6}
                                            md={4}
                                            key={exam.id}
                                        >
                                            <Fade in timeout={400}>
                                                <div>
                                                    <ExamCard
                                                        exam={exam}
                                                        showDetails={false}
                                                        showEdit={false}
                                                        showDelete={false}
                                                    />
                                                </div>
                                            </Fade>
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
                                    My exams
                                </Typography>
                                {filteredStudentExams.length === 0 ? (
                                    <Typography
                                        variant="body2"
                                        color="text.secondary"
                                    >
                                        You are not registered for any exams.
                                    </Typography>
                                ) : (
                                    <Grid container spacing={3}>
                                        {filteredStudentExams.map((exam) => (
                                            <Grid
                                                item
                                                xs={12}
                                                sm={6}
                                                md={4}
                                                key={`my-${exam.id}`}
                                            >
                                                <Fade in timeout={400}>
                                                    <div>
                                                        <ExamCard
                                                            exam={exam}
                                                            onOpenDetails={() =>
                                                                handleOpenDetails(exam)
                                                            }
                                                            showDetails={true}
                                                            showEdit={false}
                                                            showDelete={false}
                                                        />
                                                    </div>
                                                </Fade>
                                            </Grid>
                                        ))}
                                    </Grid>
                                )}
                            </Box>

                            <Box>
                                <Typography variant="h6" gutterBottom>
                                    All exams
                                </Typography>
                                <Grid container spacing={3}>
                                    {filteredAllExams.map((exam) => (
                                        <Grid
                                            item
                                            xs={12}
                                            sm={6}
                                            md={4}
                                            key={exam.id}
                                        >
                                            <Fade in timeout={400}>
                                                <div>
                                                    <ExamCard
                                                        exam={exam}
                                                        showDetails={false}
                                                        showEdit={false}
                                                        showDelete={false}
                                                    />
                                                </div>
                                            </Fade>
                                        </Grid>
                                    ))}
                                </Grid>
                            </Box>
                        </>
                    )}

                    {isAdmin && (
                        <Box>
                            <Typography variant="h6" gutterBottom>
                                All exams (manage)
                            </Typography>
                            <Grid container spacing={3}>
                                {filteredAllExams.map((exam) => (
                                    <Grid
                                        item
                                        xs={12}
                                        sm={6}
                                        md={4}
                                        key={exam.id}
                                    >
                                        <Fade in timeout={400}>
                                            <div>
                                                <ExamCard
                                                    exam={exam}
                                                    onOpenDetails={() =>
                                                        handleOpenDetails(exam)
                                                    }
                                                    onEdit={() =>
                                                        handleOpenEdit(exam)
                                                    }
                                                    onDelete={() =>
                                                        handleOpenDelete(exam)
                                                    }
                                                    showDetails={true}
                                                    showEdit={true}
                                                    showDelete={true}
                                                />
                                            </div>
                                        </Fade>
                                    </Grid>
                                ))}
                            </Grid>
                        </Box>
                    )}

                    {isUser && (
                        <Box>
                            <Typography variant="h6" gutterBottom>
                                All exams
                            </Typography>
                            <Grid container spacing={3}>
                                {filteredAllExams.map((exam) => (
                                    <Grid
                                        item
                                        xs={12}
                                        sm={6}
                                        md={4}
                                        key={exam.id}
                                    >
                                        <Fade in timeout={400}>
                                            <div>
                                                <ExamCard
                                                    exam={exam}
                                                    showDetails={false}
                                                    showEdit={false}
                                                    showDelete={false}
                                                />
                                            </div>
                                        </Fade>
                                    </Grid>
                                ))}
                            </Grid>
                        </Box>
                    )}

                    {!isAdmin && !isStaff && !isStudent && !isUser && (
                        <Box>
                            <Typography variant="h6" gutterBottom>
                                All exams
                            </Typography>
                            <Grid container spacing={3}>
                                {filteredAllExams.map((exam) => (
                                    <Grid
                                        item
                                        xs={12}
                                        sm={6}
                                        md={4}
                                        key={exam.id}
                                    >
                                        <Fade in timeout={400}>
                                            <div>
                                                <ExamCard
                                                    exam={exam}
                                                    showDetails={false}
                                                    showEdit={false}
                                                    showDelete={false}
                                                />
                                            </div>
                                        </Fade>
                                    </Grid>
                                ))}
                            </Grid>
                        </Box>
                    )}
                </Box>
            )}

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
