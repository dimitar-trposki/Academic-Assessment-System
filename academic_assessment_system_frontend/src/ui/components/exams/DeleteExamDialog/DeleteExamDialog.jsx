import React from "react";
import "./DeleteExamDialog.css";

import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Typography,
} from "@mui/material";

const DeleteExamDialog = ({open, onClose, exam, onDelete}) => {
    const handleDelete = () => {
        if (!exam) return;
        onDelete(exam.id);
        onClose();
    };

    const courseName = exam?.course?.courseName || `exam #${exam?.id ?? ""}`;

    return (
        <Dialog
            open={open}
            onClose={onClose}
            maxWidth="xs"
            fullWidth
            className="dialog-theme course-dialog-root"
        >
            <DialogTitle>Delete exam</DialogTitle>
            <DialogContent dividers>
                <Typography variant="body2">
                    Are you sure you want to delete{" "}
                    <strong>{courseName}</strong>? This action cannot be undone.
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

export default DeleteExamDialog;
