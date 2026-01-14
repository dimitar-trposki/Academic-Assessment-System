import React from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    Typography,
} from "@mui/material";
import StudentsTable from "../../components/students/StudentsTable.jsx";

const StudentsPage = () => {
    return (
        <>
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 2,
                }}
            >
                <Box>
                    <Typography variant="h4" className="page-title">
                        Students
                    </Typography>
                    <Typography variant="body1" className="page-subtitle">
                        Enrolled students and their index numbers.
                    </Typography>
                </Box>

                <Button variant="contained" color="primary" size="small">
                    + Add Student
                </Button>
            </Box>

            <Card elevation={1}>
                <CardContent>
                    <StudentsTable/>
                </CardContent>
            </Card>
        </>
    );
};

export default StudentsPage;
