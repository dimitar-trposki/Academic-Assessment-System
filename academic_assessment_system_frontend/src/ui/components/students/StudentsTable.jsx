import React from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TableContainer,
    Paper,
} from "@mui/material";

const mockStudents = [
    {id: 1, index: "201234", name: "Ana Petrovska", program: "KNI"},
    {id: 2, index: "201567", name: "Marko Stojanov", program: "IKI"},
    {id: 3, index: "201890", name: "Elena Trajceva", program: "PET"},
];

const StudentsTable = () => {
    return (
        <TableContainer component={Paper} elevation={0}>
            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell>Index</TableCell>
                        <TableCell>Name</TableCell>
                        <TableCell>Program</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {mockStudents.map((student) => (
                        <TableRow key={student.id} hover>
                            <TableCell>{student.index}</TableCell>
                            <TableCell>{student.name}</TableCell>
                            <TableCell>{student.program}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default StudentsTable;
