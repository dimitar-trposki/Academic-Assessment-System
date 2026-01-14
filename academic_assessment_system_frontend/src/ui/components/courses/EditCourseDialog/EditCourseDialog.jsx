// src/ui/components/courses/EditCourseDialog/EditCourseDialog.jsx
import React, {useEffect, useState} from "react";
import "./EditCourseDialog.css";

import {
    Autocomplete,
    Button,
    Chip,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Grid,
    TextField,
} from "@mui/material";

import useUsers from "../../../../hooks/useUsers.js";

const EditCourseDialog = ({open, onClose, course, onEdit}) => {
    const [formData, setFormData] = useState({
        courseCode: "",
        courseName: "",
        semester: "",
        academicYear: "",
        professors: [],
        assistants: [],
    });

    const {users, loading} = useUsers();

    const allUsers = Array.isArray(users)
        ? users
        : Array.isArray(users?.users)
            ? users.users
            : Array.isArray(users?.data)
                ? users.data
                : [];

    const fullName = (u) =>
        u.fullName ||
        [u.firstName, u.lastName].filter(Boolean).join(" ") ||
        u.email ||
        `User #${u.id}`;

    const staffUsers = allUsers.filter((u) => u.userRole === "STAFF");

    const professorOptions = staffUsers;
    const assistantOptions = staffUsers;

    useEffect(() => {
        if (!course) return;

        setFormData({
            courseCode: course.courseCode ?? "",
            courseName: course.courseName ?? "",
            semester: course.semester ?? "",
            academicYear: course.academicYear ?? "",
            professors: course.professors ?? [],
            assistants: course.assistants ?? [],
        });
    }, [course]);

    const handleChangeField = (event) => {
        const {name, value} = event.target;
        setFormData((prev) => ({...prev, [name]: value}));
    };

    const handleChangeProfessors = (_, value) => {
        setFormData((prev) => ({...prev, professors: value}));
    };

    const handleChangeAssistants = (_, value) => {
        setFormData((prev) => ({...prev, assistants: value}));
    };

    const handleSubmit = () => {
        if (!course) return;

        const payload = {
            courseCode: formData.courseCode,
            courseName: formData.courseName,
            semester: Number(formData.semester),
            academicYear: Number(formData.academicYear),
            professorIds: formData.professors.map((p) => p.id),
            assistantIds: formData.assistants.map((a) => a.id),
        };

        onEdit(course.id, payload);
        onClose();
    };

    return (
        <Dialog
            open={open}
            onClose={onClose}
            fullWidth
            maxWidth="md"
            className="course-dialog-root"
        >
            <DialogTitle>Edit course</DialogTitle>
            <DialogContent dividers>
                <Grid container spacing={2} sx={{mt: 0.2}}>
                    <Grid item xs={12} sm={6}>
                        <TextField
                            label="Course code"
                            name="courseCode"
                            fullWidth
                            value={formData.courseCode}
                            onChange={handleChangeField}
                        />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <TextField
                            label="Academic year"
                            name="academicYear"
                            fullWidth
                            value={formData.academicYear}
                            onChange={handleChangeField}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            label="Course name"
                            name="courseName"
                            fullWidth
                            value={formData.courseName}
                            onChange={handleChangeField}
                        />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <TextField
                            label="Semester"
                            name="semester"
                            type="number"
                            fullWidth
                            value={formData.semester}
                            onChange={handleChangeField}
                        />
                    </Grid>

                    {/* Professors */}
                    <Grid item xs={12} sm={6}>
                        <Autocomplete
                            multiple
                            options={professorOptions}
                            getOptionLabel={fullName}
                            value={formData.professors}
                            onChange={handleChangeProfessors}
                            loading={loading}
                            isOptionEqualToValue={(opt, val) => opt.id === val.id}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Professors"
                                    placeholder={
                                        loading ? "Loading..." : "Select professors"
                                    }
                                    InputProps={{
                                        ...params.InputProps,
                                        endAdornment: (
                                            <>
                                                {loading ? (
                                                    <CircularProgress
                                                        color="inherit"
                                                        size={18}
                                                    />
                                                ) : null}
                                                {params.InputProps.endAdornment}
                                            </>
                                        ),
                                    }}
                                />
                            )}
                            renderTags={(value, getTagProps) =>
                                value.map((option, index) => (
                                    <Chip
                                        variant="filled"
                                        label={fullName(option)}
                                        {...getTagProps({index})}
                                        key={option.id}
                                    />
                                ))
                            }
                        />
                    </Grid>

                    {/* Assistants */}
                    <Grid item xs={12} sm={6}>
                        <Autocomplete
                            multiple
                            options={assistantOptions}
                            getOptionLabel={fullName}
                            value={formData.assistants}
                            onChange={handleChangeAssistants}
                            loading={loading}
                            isOptionEqualToValue={(opt, val) => opt.id === val.id}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Assistants"
                                    placeholder={
                                        loading ? "Loading..." : "Select assistants"
                                    }
                                    InputProps={{
                                        ...params.InputProps,
                                        endAdornment: (
                                            <>
                                                {loading ? (
                                                    <CircularProgress
                                                        color="inherit"
                                                        size={18}
                                                    />
                                                ) : null}
                                                {params.InputProps.endAdornment}
                                            </>
                                        ),
                                    }}
                                />
                            )}
                            renderTags={(value, getTagProps) =>
                                value.map((option, index) => (
                                    <Chip
                                        variant="outlined"
                                        label={fullName(option)}
                                        {...getTagProps({index})}
                                        key={option.id}
                                    />
                                ))
                            }
                        />
                    </Grid>
                </Grid>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button variant="contained" color="primary" onClick={handleSubmit}>
                    Save changes
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default EditCourseDialog;
