import React, {useState, useEffect} from "react";
import "./UserGrid.css";
import {
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Grid,
    Typography,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    IconButton,
    Stack,
    Tooltip,
} from "@mui/material";
import {Add, Upload, Download, Delete, Edit, Info} from "@mui/icons-material";

import useUsers from "../../../../hooks/useUsers.js";
import useStudents from "../../../../hooks/useStudents.js";
import AddUserDialog from "../AddUserDialog/AddUserDialog.jsx";
import EditUserDialog from "../EditUserDialog/EditUserDialog.jsx";
import DeleteUserDialog from "../DeleteUserDialog/DeleteUserDialog.jsx";
import UserDetails from "../UserDetails/UserDetails.jsx";
import EditStudentDialog from "../EditStudentDialog/EditStudentDialog.jsx";

const ROLE_ADMIN = "ADMINISTRATOR";

const UserGrid = () => {
    const {
        users,
        loading: usersLoading,
        onAdd: addUser,
        onEdit: editUser,
        onDelete: deleteUser,
        importUsers,
        exportUsers,
        fetchUsers,
        me,
    } = useUsers();

    const {
        students,
        loading: studentsLoading,
        onAdd: addStudent,
        onEdit: editStudent,
        onDeleteWithUser: deleteStudentWithUser,
        onDeleteWithoutUser: deleteStudentWithoutUser,
        fetchStudents,
    } = useStudents();

    const [role, setRole] = useState(null);

    const [addDialogOpen, setAddDialogOpen] = useState(false);
    const [editDialogOpen, setEditDialogOpen] = useState(false);
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [detailsOpen, setDetailsOpen] = useState(false);

    const [selectedUser, setSelectedUser] = useState(null);

    const [editStudentDialogOpen, setEditStudentDialogOpen] = useState(false);
    const [selectedStudent, setSelectedStudent] = useState(null);

    const [deleteContext, setDeleteContext] = useState(null);

    useEffect(() => {
        let mounted = true;
        if (!me) return;

        me()
            .then((u) => {
                if (!mounted) return;
                setRole(u?.role || null);
            })
            .catch(() => {
                if (!mounted) return;
                setRole(null);
            });

        return () => {
            mounted = false;
        };
    }, [me]);

    const isAdmin = role === ROLE_ADMIN;

    const staffUsers = users.filter((u) => u.userRole === "STAFF");
    const regularUsers = users.filter((u) => u.userRole === "USER");

    const handleOpenAdd = () => setAddDialogOpen(true);
    const handleCloseAdd = () => setAddDialogOpen(false);

    const handleOpenEditUser = (user) => {
        setSelectedUser(user);
        setEditDialogOpen(true);
    };
    const handleCloseEdit = () => {
        setSelectedUser(null);
        setEditDialogOpen(false);
    };

    const handleOpenDetailsUser = (user) => {
        setSelectedUser(user);
        setSelectedStudent(null);
        setDetailsOpen(true);
    };

    const handleOpenDetailsStudent = (student) => {
        setSelectedStudent(student);
        setSelectedUser(null);
        setDetailsOpen(true);
    };

    const handleCloseDetails = () => {
        setSelectedUser(null);
        setSelectedStudent(null);
        setDetailsOpen(false);
    };

    const handleOpenDeleteUser = (user) => {
        setDeleteContext({type: "USER", entity: user});
        setDeleteDialogOpen(true);
    };

    const handleOpenDeleteStudent = (student) => {
        setDeleteContext({type: "STUDENT", entity: student});
        setDeleteDialogOpen(true);
    };

    const handleCloseDelete = () => {
        setDeleteContext(null);
        setDeleteDialogOpen(false);
    };

    const handleOpenEditStudent = (student) => {
        setSelectedStudent(student);
        setEditStudentDialogOpen(true);
    };

    const handleCloseEditStudent = () => {
        setEditStudentDialogOpen(false);
        setSelectedStudent(null);
    };

    const handleSaveEditStudent = async (student, studentDto, userDto) => {
        try {
            await editUser(student.userId, userDto);

            if (userDto.userRole === "STUDENT") {
                await editStudent(student.id, studentDto);
            } else {
                await deleteStudentWithoutUser(student.id);
            }

            await fetchStudents();
            handleCloseEditStudent();
        } catch (e) {
            console.error("Failed to edit student/user", e);
        }
    };

    const handleCreateUser = async (formData) => {
        const {studentIndex, major, userRole, ...userData} = formData;

        let createdUser = null;

        try {
            createdUser = await addUser({
                ...userData,
                userRole,
            });
        } catch (e) {
            console.error("Failed to create USER", e);
            return;
        }

        if (studentIndex?.trim() && major?.trim()) {
            const userId = createdUser?.id;
            if (!userId) {
                console.warn(
                    "User created but response has no id – cannot create Student profile."
                );
            } else {
                try {
                    await addStudent({
                        studentIndex,
                        major,
                        userId,
                    });
                } catch (e) {
                    console.error("Failed to create STUDENT profile", e);
                }
            }
        }

        handleCloseAdd();
    };

    const handleUpdateUser = async (id, data, extras) => {
        try {
            await editUser(id, data);

            if (extras) {
                const {previousRole, studentData} = extras;

                const becameStudent =
                    previousRole !== "STUDENT" && data.userRole === "STUDENT";

                if (
                    becameStudent &&
                    studentData &&
                    studentData.studentIndex?.trim() &&
                    studentData.major?.trim()
                ) {
                    await addStudent({
                        studentIndex: studentData.studentIndex,
                        major: studentData.major,
                        userId: id,
                    });

                    await fetchStudents();
                }
            }

            handleCloseEdit();
        } catch (e) {
            console.error("Failed to update user", e);
        }
    };

    const handleConfirmDelete = async () => {
        if (!deleteContext) return;

        try {
            if (deleteContext.type === "USER") {
                await deleteUser(deleteContext.entity.id);
            } else {
                await deleteStudentWithUser(deleteContext.entity.id);
            }
        } catch (e) {
            console.error("Failed to delete", e);
        } finally {
            handleCloseDelete();
        }
    };

    const handleImportClick = () => {
        const input = document.getElementById("users-import-input");
        if (input) {
            input.click();
        }
    };

    const handleImportChange = async (event) => {
        const file = event.target.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        try {
            await importUsers(formData);
            await fetchStudents();
        } catch (e) {
            console.error("Import failed", e);
        } finally {
            event.target.value = "";
        }
    };

    const handleExport = async () => {
        try {
            const response = await exportUsers();
            const blob = new Blob([response.data], {type: "text/csv"});
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "users.csv";
            a.click();
            window.URL.revokeObjectURL(url);
        } catch (e) {
            console.error("Export failed", e);
        }
    };

    const isLoading = usersLoading || studentsLoading;

    if (isLoading) {
        return (
            <Box className="user-grid-root user-grid-loading">
                <CircularProgress/>
                <Typography variant="body2" sx={{mt: 1}}>
                    Loading users...
                </Typography>
            </Box>
        );
    }

    const importTooltipText =
        "Required header:\n" +
        "firstName,lastName,email,password,userRole,studentIndex,major\n\n" +
        "Example for staff: Ivan,Ivanov,ii@gmail.com,Ii@12345,STAFF,,\n" +
        "Example for user: Ivan,Ivanov,ii@gmail.com,Ii@12345,USER,,\n" +
        "Example for student: Ivan,Ivanov,ii@gmail.com,Ii@12345,STUDENT,221133,SEIS";

    return (
        <Box className="user-grid-root">
            <Box className="user-grid-toolbar">
                <Typography variant="h6" className="user-grid-title">
                    Users overview
                </Typography>

                {isAdmin && (
                    <Box className="user-grid-toolbar-actions">
                        <input
                            id="users-import-input"
                            type="file"
                            accept=".csv"
                            style={{display: "none"}}
                            onChange={handleImportChange}
                        />

                        <Tooltip
                            arrow
                            title={importTooltipText}
                            slotProps={{
                                tooltip: {
                                    sx: {
                                        whiteSpace: "pre-line",
                                        fontSize: "0.8rem",
                                    },
                                },
                            }}
                        >
                            <Button
                                variant="outlined"
                                startIcon={<Upload/>}
                                size="small"
                                onClick={handleImportClick}
                            >
                                Import users
                            </Button>
                        </Tooltip>

                        <Button
                            variant="outlined"
                            startIcon={<Download/>}
                            size="small"
                            onClick={handleExport}
                        >
                            Export users
                        </Button>
                        <Button
                            variant="contained"
                            startIcon={<Add/>}
                            size="small"
                            onClick={handleOpenAdd}
                        >
                            Add user
                        </Button>
                    </Box>
                )}
            </Box>

            <Grid container spacing={3} className="user-grid-sections">
                {/* STAFF */}
                <Grid item xs={12}>
                    <Card className="user-card">
                        <CardContent>
                            <Typography
                                variant="subtitle1"
                                className="user-card-title"
                            >
                                Staff
                            </Typography>
                            <TableContainer
                                component={Paper}
                                className="user-table-container"
                            >
                                <Table size="small" className="user-table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>First name</TableCell>
                                            <TableCell>Last name</TableCell>
                                            <TableCell>Email</TableCell>
                                            <TableCell>Role</TableCell>
                                            <TableCell align="right">
                                                Actions
                                            </TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody className="user-table-body">
                                        {staffUsers.map((u) => (
                                            <TableRow key={u.id}>
                                                <TableCell>
                                                    {u.firstName}
                                                </TableCell>
                                                <TableCell>
                                                    {u.lastName}
                                                </TableCell>
                                                <TableCell>{u.email}</TableCell>
                                                <TableCell>
                                                    {u.userRole}
                                                </TableCell>
                                                <TableCell align="right">
                                                    {isAdmin && (
                                                        <>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenDetailsUser(
                                                                        u
                                                                    )
                                                                }
                                                            >
                                                                <Info/>
                                                            </IconButton>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenEditUser(
                                                                        u
                                                                    )
                                                                }
                                                            >
                                                                <Edit/>
                                                            </IconButton>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenDeleteUser(
                                                                        u
                                                                    )
                                                                }
                                                            >
                                                                <Delete/>
                                                            </IconButton>
                                                        </>
                                                    )}
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                        {staffUsers.length === 0 && (
                                            <TableRow>
                                                <TableCell
                                                    colSpan={5}
                                                    align="center"
                                                >
                                                    No staff users.
                                                </TableCell>
                                            </TableRow>
                                        )}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </CardContent>
                    </Card>
                </Grid>

                {/* STUDENTS */}
                <Grid item xs={12}>
                    <Card className="user-card">
                        <CardContent>
                            <Typography
                                variant="subtitle1"
                                className="user-card-title"
                            >
                                Students
                            </Typography>
                            <TableContainer
                                component={Paper}
                                className="user-table-container"
                            >
                                <Table size="small" className="user-table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Index</TableCell>
                                            <TableCell>Name</TableCell>
                                            <TableCell>Email</TableCell>
                                            <TableCell>Major</TableCell>
                                            <TableCell align="right">
                                                Actions
                                            </TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody className="user-table-body">
                                        {students.map((s) => (
                                            <TableRow key={s.id}>
                                                <TableCell>
                                                    {s.studentIndex}
                                                </TableCell>
                                                <TableCell>
                                                    {s.studentFirstName}{" "}
                                                    {s.studentLastName}
                                                </TableCell>
                                                <TableCell>
                                                    {s.studentEmail}
                                                </TableCell>
                                                <TableCell>{s.major}</TableCell>
                                                <TableCell align="right">
                                                    {isAdmin && (
                                                        <>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenDetailsStudent(
                                                                        s
                                                                    )
                                                                }
                                                            >
                                                                <Info/>
                                                            </IconButton>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenEditStudent(
                                                                        s
                                                                    )
                                                                }
                                                            >
                                                                <Edit/>
                                                            </IconButton>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenDeleteStudent(
                                                                        s
                                                                    )
                                                                }
                                                            >
                                                                <Delete/>
                                                            </IconButton>
                                                        </>
                                                    )}
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                        {students.length === 0 && (
                                            <TableRow>
                                                <TableCell
                                                    colSpan={5}
                                                    align="center"
                                                >
                                                    No students.
                                                </TableCell>
                                            </TableRow>
                                        )}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </CardContent>
                    </Card>
                </Grid>

                {/* USERS */}
                <Grid item xs={12}>
                    <Card className="user-card">
                        <CardContent>
                            <Typography
                                variant="subtitle1"
                                className="user-card-title"
                            >
                                Users
                            </Typography>
                            <TableContainer
                                component={Paper}
                                className="user-table-container"
                            >
                                <Table size="small" className="user-table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>First name</TableCell>
                                            <TableCell>Last name</TableCell>
                                            <TableCell>Email</TableCell>
                                            <TableCell>Role</TableCell>
                                            <TableCell align="right">
                                                Actions
                                            </TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody className="user-table-body">
                                        {regularUsers.map((u) => (
                                            <TableRow key={u.id}>
                                                <TableCell>
                                                    {u.firstName}
                                                </TableCell>
                                                <TableCell>
                                                    {u.lastName}
                                                </TableCell>
                                                <TableCell>{u.email}</TableCell>
                                                <TableCell>
                                                    {u.userRole}
                                                </TableCell>
                                                <TableCell align="right">
                                                    {isAdmin && (
                                                        <>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenDetailsUser(
                                                                        u
                                                                    )
                                                                }
                                                            >
                                                                <Info/>
                                                            </IconButton>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenEditUser(
                                                                        u
                                                                    )
                                                                }
                                                            >
                                                                <Edit/>
                                                            </IconButton>
                                                            <IconButton
                                                                onClick={() =>
                                                                    handleOpenDeleteUser(
                                                                        u
                                                                    )
                                                                }
                                                            >
                                                                <Delete/>
                                                            </IconButton>
                                                        </>
                                                    )}
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                        {regularUsers.length === 0 && (
                                            <TableRow>
                                                <TableCell
                                                    colSpan={5}
                                                    align="center"
                                                >
                                                    No regular users.
                                                </TableCell>
                                            </TableRow>
                                        )}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            <AddUserDialog
                open={addDialogOpen}
                onClose={handleCloseAdd}
                onSave={handleCreateUser}
            />

            {selectedUser && (
                <EditUserDialog
                    open={editDialogOpen}
                    onClose={handleCloseEdit}
                    user={selectedUser}
                    onSave={handleUpdateUser}
                />
            )}

            {selectedStudent && (
                <EditStudentDialog
                    open={editStudentDialogOpen}
                    onClose={handleCloseEditStudent}
                    student={selectedStudent}
                    onSave={handleSaveEditStudent}
                />
            )}

            <DeleteUserDialog
                open={deleteDialogOpen}
                onClose={handleCloseDelete}
                onConfirm={handleConfirmDelete}
                context={deleteContext}
            />

            <UserDetails
                open={detailsOpen}
                onClose={handleCloseDetails}
                user={selectedUser}
                student={selectedStudent}
            />
        </Box>
    );
};

export default UserGrid;
