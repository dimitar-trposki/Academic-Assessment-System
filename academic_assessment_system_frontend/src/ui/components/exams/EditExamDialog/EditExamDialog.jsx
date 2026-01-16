import React, {useEffect, useState} from "react";
import "./EditExamDialog.css";

import {
    Autocomplete,
    Button,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Grid,
    TextField,
} from "@mui/material";

import useCourses from "../../../../hooks/useCourses.js";

const normalizeTime = (t) => {
    if (!t) return "";
    return t.substring(0, 5);
};

const EditExamDialog = ({open, onClose, exam, onEdit}) => {
    const [formData, setFormData] = useState({
        session: "",
        dateOfExam: "",
        capacityOfStudents: "",
        course: null,
        reservedLaboratories: "",
        startTime: "",
        endTime: "",
    });

    const {courses, loading} = useCourses();

    useEffect(() => {
        if (!exam) return;

        const labs =
            exam.reservedLaboratories && exam.reservedLaboratories.length > 0
                ? exam.reservedLaboratories.join(", ")
                : "";

        setFormData({
            session: exam.session ?? "",
            dateOfExam: exam.dateOfExam ?? "",
            capacityOfStudents: exam.capacityOfStudents ?? "",
            course: exam.course ?? null,
            reservedLaboratories: labs,
            startTime: normalizeTime(exam.startTime),
            endTime: normalizeTime(exam.endTime),
        });
    }, [exam]);

    const handleChangeField = (event) => {
        const {name, value} = event.target;
        setFormData((prev) => ({...prev, [name]: value}));
    };

    const handleChangeCourse = (_, value) => {
        setFormData((prev) => ({...prev, course: value}));
    };

    const buildTimeForApi = (t) => {
        if (!t) return null;
        return t.length === 5 ? `${t}:00` : t;
    };

    const handleSubmit = () => {
        if (!exam) return;

        const labs = formData.reservedLaboratories
            .split(",")
            .map((s) => s.trim())
            .filter(Boolean);

        const payload = {
            session: formData.session,
            dateOfExam: formData.dateOfExam || null,
            capacityOfStudents: formData.capacityOfStudents
                ? Number(formData.capacityOfStudents)
                : null,
            course: formData.course ? {id: formData.course.id} : null,
            reservedLaboratories: labs,
            startTime: buildTimeForApi(formData.startTime),
            endTime: buildTimeForApi(formData.endTime),
        };

        onEdit(exam.id, payload);
        onClose();
    };

    const courseOptions = Array.isArray(courses) ? courses : [];
    const getCourseLabel = (c) =>
        c ? `${c.courseCode} - ${c.courseName}` : "";

    return (
        <Dialog
            open={open}
            onClose={onClose}
            fullWidth
            maxWidth="md"
            className="dialog-theme course-dialog-root"
        >
            <DialogTitle>Edit exam</DialogTitle>
            <DialogContent dividers>
                <Grid container spacing={2} sx={{mt: 0.2}}>
                    <Grid item xs={12} sm={6}>
                        <TextField
                            label="Session"
                            name="session"
                            fullWidth
                            value={formData.session}
                            onChange={handleChangeField}
                            size="small"
                        />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <TextField
                            label="Capacity of students"
                            name="capacityOfStudents"
                            type="number"
                            fullWidth
                            value={formData.capacityOfStudents}
                            onChange={handleChangeField}
                            size="small"
                        />
                    </Grid>

                    <Grid item xs={12} sm={6}>
                        <TextField
                            label="Date of exam"
                            name="dateOfExam"
                            type="date"
                            fullWidth
                            value={formData.dateOfExam}
                            onChange={handleChangeField}
                            InputLabelProps={{shrink: true}}
                            size="small"
                        />
                    </Grid>
                    <Grid item xs={12} sm={3}>
                        <TextField
                            label="Start time"
                            name="startTime"
                            type="time"
                            fullWidth
                            value={formData.startTime}
                            onChange={handleChangeField}
                            InputLabelProps={{shrink: true}}
                            size="small"
                        />
                    </Grid>
                    <Grid item xs={12} sm={3}>
                        <TextField
                            label="End time"
                            name="endTime"
                            type="time"
                            fullWidth
                            value={formData.endTime}
                            onChange={handleChangeField}
                            InputLabelProps={{shrink: true}}
                            size="small"
                        />
                    </Grid>

                    {/* Course */}
                    <Grid item xs={12}>
                        <Autocomplete
                            options={courseOptions}
                            getOptionLabel={getCourseLabel}
                            value={formData.course}
                            onChange={handleChangeCourse}
                            loading={loading}
                            isOptionEqualToValue={(o, v) => o.id === v.id}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Course"
                                    placeholder={
                                        loading ? "Loading..." : "Select course"
                                    }
                                    size="small"
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
                                                {
                                                    params.InputProps
                                                        .endAdornment
                                                }
                                            </>
                                        ),
                                    }}
                                />
                            )}
                        />
                    </Grid>

                    {/* Labs */}
                    <Grid item xs={12}>
                        <TextField
                            label="Reserved laboratories"
                            name="reservedLaboratories"
                            fullWidth
                            value={formData.reservedLaboratories}
                            onChange={handleChangeField}
                            helperText="Comma-separated list, e.g. Lab 1, Lab 2"
                            size="small"
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

export default EditExamDialog;
