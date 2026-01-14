import React from "react";
import {Box, Typography} from "@mui/material";
import CourseGrid from "../../components/courses/CourseGrid/CourseGrid.jsx";
import "./CoursesPage.css";

const CoursesPage = () => {
    return (
        <div className="courses-page-root">
            <Box className="courses-header">
                <Box>
                    <Typography variant="h4" className="page-title">
                        Courses
                    </Typography>
                    <Typography variant="body1" className="page-subtitle">
                        Manage courses, assigned staff and enrolled students.
                    </Typography>
                </Box>
                {/* Add button is inside CourseGrid */}
            </Box>

            <Box className="courses-content">
                <CourseGrid/>
            </Box>
        </div>
    );
};

export default CoursesPage;
