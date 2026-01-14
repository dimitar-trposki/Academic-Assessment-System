import React from "react";
import {Box, Typography} from "@mui/material";
import UserGrid from "../../components/users/UserGrid/UserGrid.jsx";
import "./UsersPage.css";

const UsersPage = () => {
    return (
        <div className="users-page-root">
            <Box className="users-header">
                <Box>
                    <Typography variant="h4" className="page-title">
                        Users
                    </Typography>
                    <Typography variant="body1" className="page-subtitle">
                        Manage staff, students and user accounts.
                    </Typography>
                </Box>
            </Box>

            <Box className="users-content">
                <UserGrid/>
            </Box>
        </div>
    );
};

export default UsersPage;
