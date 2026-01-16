// src/ui/components/courses/AddCourseDialog/AddCourseDialog.jsx
import React, {useState} from "react";
import "./AddCourseDialog.css";

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

const initialFormData = {
    courseCode: "",
    courseName: "",
    semester: "",
    academicYear: "",
    professors: [],
    assistants: [],
};

const AddCourseDialog = ({open, onClose, onAdd}) => {
    const [formData, setFormData] = useState(initialFormData);

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

    const handleChangeField = (e) => {
        const {name, value} = e.target;
        setFormData((prev) => ({...prev, [name]: value}));
    };

    const handleChangeProfessors = (_, value) => {
        setFormData((prev) => ({...prev, professors: value}));
    };

    const handleChangeAssistants = (_, value) => {
        setFormData((prev) => ({...prev, assistants: value}));
    };

    const resetAndClose = () => {
        setFormData(initialFormData);
        onClose();
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

        onAdd(payload);
        resetAndClose();
    };

    return (
        <Dialog
            open={open}
            onClose={resetAndClose}
            fullWidth
            maxWidth="md"
            className="dialog-theme course-dialog-root"
        >
            <DialogTitle>Create new course</DialogTitle>

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
                            helperText="Example: EMC, AI, WPIS"
                            autoFocus
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
                            placeholder="2025"
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

                    {/* ROW 3: PROFESSORS (FULL WIDTH) */}
                    <Grid item xs={12}>
                        <Autocomplete
                            multiple
                            options={professorOptions}
                            value={formData.professors}
                            onChange={handleChangeProfessors}
                            getOptionLabel={fullName}
                            loading={loading}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Professors"
                                    placeholder={
                                        loading ? "Loading..." : "Select professors"
                                    }
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

                    {/* ROW 4: ASSISTANTS (FULL WIDTH) */}
                    <Grid item xs={12}>
                        <Autocomplete
                            multiple
                            options={assistantOptions}
                            value={formData.assistants}
                            onChange={handleChangeAssistants}
                            getOptionLabel={fullName}
                            loading={loading}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Assistants"
                                    placeholder={
                                        loading ? "Loading..." : "Select assistants"
                                    }
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
                <Button onClick={resetAndClose}>Cancel</Button>
                <Button variant="contained" color="primary" onClick={handleSubmit}>
                    Create course
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default AddCourseDialog;
