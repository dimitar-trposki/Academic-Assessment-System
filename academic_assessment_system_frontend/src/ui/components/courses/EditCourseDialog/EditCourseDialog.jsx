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

    const handleChangeField = (e) => {
        setFormData((prev) => ({...prev, [e.target.name]: e.target.value}));
    };

    const handleSubmit = () => {
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
            className="dialog-theme course-dialog-root"
        >
            <DialogTitle>Edit course</DialogTitle>

            <DialogContent dividers>
                <Grid container spacing={2}>

                    {/* ROW 1: CODE + NAME */}
                    <Grid item xs={12} md={6}>
                        <TextField
                            fullWidth
                            label="Course code"
                            name="courseCode"
                            value={formData.courseCode}
                            onChange={handleChangeField}
                        />
                    </Grid>

                    <Grid item xs={12} md={6}>
                        <TextField
                            fullWidth
                            label="Course name"
                            name="courseName"
                            value={formData.courseName}
                            onChange={handleChangeField}
                        />
                    </Grid>

                    {/* ROW 2: YEAR + SEMESTER */}
                    <Grid item xs={12} md={6}>
                        <TextField
                            fullWidth
                            label="Academic year"
                            name="academicYear"
                            value={formData.academicYear}
                            onChange={handleChangeField}
                        />
                    </Grid>

                    <Grid item xs={12} md={6}>
                        <TextField
                            fullWidth
                            label="Semester"
                            name="semester"
                            type="number"
                            value={formData.semester}
                            onChange={handleChangeField}
                        />
                    </Grid>

                    {/* ROW 3: PROFESSORS */}
                    <Grid item xs={12}>
                        <Autocomplete
                            multiple
                            options={professorOptions}
                            value={formData.professors}
                            onChange={(_, value) =>
                                setFormData((prev) => ({...prev, professors: value}))
                            }
                            getOptionLabel={fullName}
                            loading={loading}
                            isOptionEqualToValue={(o, v) => o.id === v.id}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Professors"
                                    placeholder="Select professors"
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

                    {/* ROW 4: ASSISTANTS */}
                    <Grid item xs={12}>
                        <Autocomplete
                            multiple
                            options={assistantOptions}
                            value={formData.assistants}
                            onChange={(_, value) =>
                                setFormData((prev) => ({...prev, assistants: value}))
                            }
                            getOptionLabel={fullName}
                            loading={loading}
                            isOptionEqualToValue={(o, v) => o.id === v.id}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Assistants"
                                    placeholder="Select assistants"
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
                <Button variant="contained" onClick={handleSubmit}>
                    Save changes
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default EditCourseDialog;
