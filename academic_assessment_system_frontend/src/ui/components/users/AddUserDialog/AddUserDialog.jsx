import React, {useState, useEffect} from "react";
import "./AddUserDialog.css";
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    MenuItem,
    FormControl,
    InputLabel,
    Select,
    Stack,
    Typography,
} from "@mui/material";

const initialState = {
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    userRole: "STAFF",
    studentIndex: "",
    major: "",
};

const AddUserDialog = ({open, onClose, onSave}) => {
    const [form, setForm] = useState(initialState);

    useEffect(() => {
        if (!open) {
            setForm(initialState);
        }
    }, [open]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setForm((prev) => ({...prev, [name]: value}));
    };

    const handleCancel = () => {
        setForm(initialState);
        onClose();
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(form);
    };

    const isStudent = form.userRole === "STUDENT";

    return (
        <Dialog
            open={open}
            onClose={handleCancel}
            fullWidth
            maxWidth="sm"
            className="dialog-theme course-dialog-root user-dialog-root"
        >
            <DialogTitle>Add new user</DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent dividers>
                    <Stack spacing={2} mt={1}>
                        <TextField
                            label="First name"
                            name="firstName"
                            value={form.firstName}
                            onChange={handleChange}
                            required
                            fullWidth
                        />
                        <TextField
                            label="Last name"
                            name="lastName"
                            value={form.lastName}
                            onChange={handleChange}
                            required
                            fullWidth
                        />
                        <TextField
                            label="Email"
                            type="email"
                            name="email"
                            value={form.email}
                            onChange={handleChange}
                            required
                            fullWidth
                        />
                        <TextField
                            label="Password"
                            type="password"
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            required
                            fullWidth
                        />

                        <FormControl fullWidth>
                            <InputLabel id="role-label">
                                User role
                            </InputLabel>
                            <Select
                                labelId="role-label"
                                label="User role"
                                name="userRole"
                                value={form.userRole}
                                onChange={handleChange}
                            >
                                <MenuItem value="STAFF">STAFF</MenuItem>
                                <MenuItem value="STUDENT">STUDENT</MenuItem>
                                <MenuItem value="USER">USER</MenuItem>
                            </Select>
                        </FormControl>

                        {isStudent && (
                            <>
                                <Typography variant="subtitle2">
                                    Student profile
                                </Typography>
                                <TextField
                                    label="Student index"
                                    name="studentIndex"
                                    value={form.studentIndex}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                                <TextField
                                    label="Major"
                                    name="major"
                                    value={form.major}
                                    onChange={handleChange}
                                    required
                                    fullWidth
                                />
                            </>
                        )}
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCancel}>Cancel</Button>
                    <Button type="submit" variant="contained">
                        Save
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
};

export default AddUserDialog;
