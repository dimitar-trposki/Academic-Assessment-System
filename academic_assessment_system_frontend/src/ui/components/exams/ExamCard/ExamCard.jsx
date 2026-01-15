import React from "react";
import "./ExamCard.css";

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

const normalizeTime = (t) => (t ? t.substring(0, 5) : "");

const ExamCard = ({
                      exam,
                      onOpenDetails,
                      onEdit,
                      onDelete,
                      showDetails = true,
                      showEdit = true,
                      showDelete = true,
                  }) => {
    const courseCode = exam.course?.courseCode || "COURSE";
    const courseName = exam.course?.courseName || "Exam";

    const session = exam.session || "";
    const date = exam.dateOfExam || "";
    const startTime = normalizeTime(exam.startTime);
    const endTime = normalizeTime(exam.endTime);
    const capacity = exam.capacityOfStudents ?? "-";

    const labs =
        exam.reservedLaboratories && exam.reservedLaboratories.length > 0
            ? exam.reservedLaboratories.join(", ")
            : "No labs";

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
                            {courseCode} - {session || "Session"}
                        </Typography>
                        <Typography
                            variant="h6"
                            className="course-card-name"
                            title={courseName}
                        >
                            {courseName}
                        </Typography>
                    </Stack>

                    {showAnyActions && (
                        <Stack spacing={0.5} alignItems="flex-end">
                            <Stack direction="row" spacing={0.5}>
                                {showDetails && (
                                    <Tooltip title="Details">
                                        <IconButton
                                            size="small"
                                            onClick={onOpenDetails}
                                        >
                                            <InfoOutlinedIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                )}
                                {showEdit && (
                                    <Tooltip title="Edit exam">
                                        <IconButton
                                            size="small"
                                            onClick={onEdit}
                                        >
                                            <EditIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                )}
                                {showDelete && (
                                    <Tooltip title="Delete exam">
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

                            <Chip
                                size="small"
                                label={`Capacity ${capacity}`}
                                className="course-card-semester-chip"
                            />
                        </Stack>
                    )}
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
                                {date || "No date"}{" "}
                                {startTime &&
                                    `· ${startTime} - ${endTime || "?"}`}
                            </Typography>
                        </Stack>

                        <Stack
                            direction="row"
                            spacing={0.5}
                            alignItems="center"
                            className="course-card-meta-item"
                        >
                            <PeopleAltOutlinedIcon fontSize="small"/>
                            <Typography variant="caption">{labs}</Typography>
                        </Stack>
                    </Stack>
                </Box>
            </CardContent>
        </Card>
    );
};

export default ExamCard;
