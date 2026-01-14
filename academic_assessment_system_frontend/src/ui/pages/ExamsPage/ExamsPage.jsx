import React from "react";
import {Box, Typography} from "@mui/material";
import ExamGrid from "../../components/exams/ExamGrid/ExamGrid.jsx";
import "./ExamsPage.css";

const ExamsPage = () => {
    return (
        <div className="exams-page-root">
            <Box className="exams-header">
                <Box>
                    <Typography variant="h4" className="page-title">
                        Exams
                    </Typography>
                    <Typography variant="body1" className="page-subtitle">
                        Manage exam sessions, registrations and attendance.
                    </Typography>
                </Box>
                {/* Add button is inside ExamGrid */}
            </Box>

            <Box className="exams-content">
                <ExamGrid/>
            </Box>
        </div>
    );
};

export default ExamsPage;
