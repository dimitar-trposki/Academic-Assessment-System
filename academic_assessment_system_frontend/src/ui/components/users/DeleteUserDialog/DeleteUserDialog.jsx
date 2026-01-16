import React from "react";
import "./DeleteUserDialog.css";
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Typography,
} from "@mui/material";

const DeleteUserDialog = ({open, onClose, onConfirm, context}) => {
    if (!context) return null;

    const isUser = context.type === "USER";
    const name = isUser
        ? `${context.entity.firstName} ${context.entity.lastName}`
        : `${context.entity.studentFirstName} ${context.entity.studentLastName}`;

    return (
        <Dialog
            open={open}
            onClose={onClose}
            className="dialog-theme course-dialog-root user-dialog-root"
            maxWidth="xs"
            fullWidth
        >
            <DialogTitle>
                Delete {isUser ? "user" : "student"}?
            </DialogTitle>
            <DialogContent dividers>
                <Typography variant="body2">
                    Are you sure you want to delete{" "}
                    <strong>{name}</strong>?
                </Typography>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button onClick={onConfirm} color="error" variant="contained">
                    Delete
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default DeleteUserDialog;
