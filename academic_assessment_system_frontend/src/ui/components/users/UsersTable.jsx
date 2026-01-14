import React from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TableContainer,
    Paper,
    Chip,
} from "@mui/material";

const mockUsers = [
    {id: 1, email: "admin", role: "ADMIN", fullName: "System Administrator"},
    {id: 2, email: "prof_ai", role: "PROFESSOR", fullName: "Prof. AI"},
    {id: 3, email: "s201234", role: "STUDENT", fullName: "Ana Petrovska"},
];

const roleColor = (role) => {
    switch (role) {
        case "ADMIN":
            return "error";
        case "PROFESSOR":
            return "primary";
        default:
            return "default";
    }
};

const UsersTable = () => {
    return (
        <TableContainer component={Paper} elevation={0}>
            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell>Username</TableCell>
                        <TableCell>Full Name</TableCell>
                        <TableCell>Role</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {mockUsers.map((user) => (
                        <TableRow key={user.id} hover>
                            <TableCell>{user.username}</TableCell>
                            <TableCell>{user.fullName}</TableCell>
                            <TableCell>
                                <Chip
                                    label={user.role}
                                    size="small"
                                    color={roleColor(user.role)}
                                />
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default UsersTable;
