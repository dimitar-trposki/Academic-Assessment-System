import React, {useEffect, useState} from "react";
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
    InputAdornment,
    IconButton,
    Typography,
} from "@mui/material";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";

const EditUserDialog = ({open, onClose, user, onSave}) => {
    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",      // new password (optional)
        userRole: "",
        studentIndex: "",
        major: "",
    });

    const [showPassword, setShowPassword] = useState(false);

    useEffect(() => {
        if (user) {
            setForm((prev) => ({
                ...prev,
                firstName: user.firstName ?? "",
                lastName: user.lastName ?? "",
                email: user.email ?? "",
                password: "",
                userRole: user.userRole ?? "",
                // ако некогаш пуштиш student info во DTO, можеш да го пополниш и тука
                studentIndex: "",
                major: "",
            }));
        }
    }, [user]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setForm((prev) => ({...prev, [name]: value}));
    };

    const handleCancel = () => {
        onClose();
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const dto = {
            firstName: form.firstName,
            lastName: form.lastName,
            email: form.email,
            userRole: form.userRole,
            password:
                form.password && form.password.trim().length > 0
                    ? form.password
                    : null,
        };

        const isStudent = form.userRole === "STUDENT";

        const studentData = isStudent
            ? {
                studentIndex: form.studentIndex,
                major: form.major,
            }
            : null;

        // му праќаме и стар role, за да знае дали има transition во STUDENT
        onSave(
            user.id,
            dto,
            {
                previousRole: user.userRole,
                studentData,
            }
        );
    };

    const isStudent = form.userRole === "STUDENT";

    return (
        <Dialog open={open} onClose={handleCancel} fullWidth maxWidth="sm">
            <DialogTitle>Edit user</DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent>
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

                        <FormControl fullWidth>
                            <InputLabel id="edit-role-label">
                                User role
                            </InputLabel>
                            <Select
                                labelId="edit-role-label"
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

export default EditUserDialog;
