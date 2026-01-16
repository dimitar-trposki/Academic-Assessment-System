// src/ui/components/courses/DeleteCourseDialog/DeleteCourseDialog.jsx
import React from "react";
import "./DeleteCourseDialog.css";

import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Typography,
} from "@mui/material";

const DeleteCourseDialog = ({open, onClose, course, onDelete}) => {
    const handleDelete = () => {
        if (!course) return;
        onDelete(course.id);
        onClose();
    };

    return (
        <Dialog
            open={open}
            onClose={onClose}
            maxWidth="xs"
            fullWidth
            className="dialog-theme course-dialog-root"
        >
            <DialogTitle>Delete course</DialogTitle>
            <DialogContent dividers>
                <Typography variant="body2">
                    Are you sure you want to delete{" "}
                    <strong>{course?.courseName || "this course"}</strong>? This
                    action cannot be undone.
                </Typography>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button variant="contained" color="error" onClick={handleDelete}>
                    Delete
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default DeleteCourseDialog;
