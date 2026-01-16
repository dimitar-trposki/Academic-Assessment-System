import React, {useEffect, useState} from "react";
import "./EditStudentDialog.css";
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    Stack,
    Typography,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    InputAdornment,
    IconButton,
} from "@mui/material";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";

const EditStudentDialog = ({open, onClose, student, onSave}) => {
    const [form, setForm] = useState({
        studentIndex: "",
        major: "",
        firstName: "",
        lastName: "",
        userRole: "STUDENT",
        password: "",
    });

    const [showPassword, setShowPassword] = useState(false);

    useEffect(() => {
        if (student) {
            setForm({
                studentIndex: student.studentIndex ?? "",
                major: student.studentMajor ?? student.major ?? "",
                firstName: student.studentFirstName ?? "",
                lastName: student.studentLastName ?? "",
                userRole: student.userRole ?? "STUDENT",
                password: "",
            });
        }
    }, [student]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setForm((prev) => ({...prev, [name]: value}));
    };

    const handleCancel = () => {
        onClose();
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const studentDto = {
            studentIndex: form.studentIndex,
            major: form.major,
            userId: student.userId,
        };

        const userDto = {
            firstName: form.firstName,
            lastName: form.lastName,
            email: student.studentEmail,
            userRole: form.userRole,
            password:
                form.password && form.password.trim().length > 0
                    ? form.password
                    : null,
        };

        onSave(student, studentDto, userDto);
    };

    if (!student) return null;

    return (
        <Dialog
            open={open}
            onClose={handleCancel}
            fullWidth
            maxWidth="sm"
            className="dialog-theme course-dialog-root user-dialog-root"
        >
            <DialogTitle>Edit student profile</DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent dividers>
                    <Stack spacing={2} mt={1}>
                        <Typography variant="subtitle2">
                            {student.studentEmail}
                        </Typography>

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

                        <FormControl fullWidth>
                            <InputLabel id="student-role-label">
                                User role
                            </InputLabel>
                            <Select
                                labelId="student-role-label"
                                label="User role"
                                name="userRole"
                                value={form.userRole}
                                onChange={handleChange}
                            >
                                <MenuItem value="STUDENT">STUDENT</MenuItem>
                                <MenuItem value="STAFF">STAFF</MenuItem>
                                <MenuItem value="USER">USER</MenuItem>
                            </Select>
                        </FormControl>

                        <TextField
                            label="New password (optional)"
                            type={showPassword ? "text" : "password"}
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            fullWidth
                            InputProps={{
                                endAdornment: (
                                    <InputAdornment position="end">
                                        <IconButton
                                            onClick={() =>
                                                setShowPassword((prev) => !prev)
                                            }
                                            edge="end"
                                        >
                                            {showPassword ? (
                                                <VisibilityOff/>
                                            ) : (
                                                <Visibility/>
                                            )}
                                        </IconButton>
                                    </InputAdornment>
                                ),
                            }}
                        />

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

export default EditStudentDialog;
