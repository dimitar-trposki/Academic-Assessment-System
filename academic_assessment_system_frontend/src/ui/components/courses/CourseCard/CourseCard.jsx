import React from "react";
import "./CourseCard.css";

import {
    Box,
    Card,
    CardContent,
    Chip,
    IconButton,
    Stack,
    Tooltip,
    Typography,
} from "@mui/material";

import MenuBookIcon from "@mui/icons-material/MenuBook";
import EditIcon from "@mui/icons-material/Edit";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import PeopleAltOutlinedIcon from "@mui/icons-material/PeopleAltOutlined";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";

const CourseCard = ({
                        course,
                        onOpenDetails,
                        onEdit,
                        onDelete,
                        showDetails = true,
                        showEdit = true,
                        showDelete = true,
                    }) => {
    const name = course.courseName;
    const code = course.courseCode;
    const semester = course.semester;
    const year = course.academicYear;

    const profCount = course.professors?.length ?? 0;
    const asstCount = course.assistants?.length ?? 0;

    const showAnyActions = showDetails || showEdit || showDelete;

    return (
        <Card className="course-card-root" elevation={2}>
            <CardContent className="course-card-content">
                <Stack
                    direction="row"
                    alignItems="flex-start"
                    justifyContent="space-between"
                    spacing={1}
                >
                    <Stack spacing={1}>
                        <Box className="course-card-icon">
                            <MenuBookIcon fontSize="small"/>
                        </Box>

                        <Typography
                            variant="subtitle2"
                            className="course-card-code"
                        >
                            {code}
                        </Typography>
                        <Typography
                            variant="h6"
                            className="course-card-name"
                            title={name}
                        >
                            {name}
                        </Typography>
                    </Stack>

                    <Stack spacing={0.5} alignItems="flex-end">
                        {showAnyActions && (
                            <Stack direction="row" spacing={0.5}>
                                {showDetails && onOpenDetails && (
                                    <Tooltip title="Details">
                                        <IconButton
                                            size="small"
                                            onClick={onOpenDetails}
                                        >
                                            <InfoOutlinedIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                )}

                                {showEdit && onEdit && (
                                    <Tooltip title="Edit course">
                                        <IconButton
                                            size="small"
                                            onClick={onEdit}
                                        >
                                            <EditIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                )}

                                {showDelete && onDelete && (
                                    <Tooltip title="Delete course">
                                        <IconButton
                                            size="small"
                                            color="error"
                                            onClick={onDelete}
                                        >
                                            <DeleteOutlineIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                )}
                            </Stack>
                        )}

                        <Chip
                            size="small"
                            label={`Semester ${semester}`}
                            className="course-card-semester-chip"
                        />
                    </Stack>
                </Stack>

                <Box className="course-card-footer">
                    <Stack
                        direction="row"
                        spacing={1.25}
                        alignItems="center"
                        flexWrap="wrap"
                    >
                        <Stack
                            direction="row"
                            spacing={0.5}
                            alignItems="center"
                            className="course-card-meta-item"
                        >
                            <CalendarMonthIcon fontSize="small"/>
                            <Typography variant="caption">
                                {year} / {year + 1}
                            </Typography>
                        </Stack>

                        <Stack
                            direction="row"
                            spacing={0.5}
                            alignItems="center"
                            className="course-card-meta-item"
                        >
                            <PeopleAltOutlinedIcon fontSize="small"/>
                            <Typography variant="caption">
                                {profCount} prof · {asstCount} asst
                            </Typography>
                        </Stack>
                    </Stack>
                </Box>
            </CardContent>
        </Card>
    );
};

export default CourseCard;
