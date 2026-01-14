import {useCallback, useEffect, useState} from "react";
import userRepository from "../repository/userRepository.js";
import useStudents from "./useStudents.js";

const initialState = {
    "users": [],
    "loading": true,
};

const useUsers = () => {
    const [state, setState] = useState(initialState);

    const {
        students,
        loading: studentsLoading,
        onAdd: addStudent,
        onDelete: deleteStudent,
    } = useStudents();

    const fetchUsers = useCallback(() => {
        userRepository
            .findAll()
            .then((response) => {
                setState({
                    "users": response.data,
                    "loading": false,
                });
            })
            .catch((error) => console.log(error));
    }, []);

    const findById = useCallback((id) => {
        return userRepository
            .findById(id)
            .then((response) => {
                console.log(`Fetched user with ID: ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    // const onAdd = useCallback((data) => {
    //     userRepository
    //         .add(data)
    //         .then((response) => {
    //             console.log("Successfully added a new user.");
    //             fetchUsers();
    //             return response.data;
    //         })
    //         .catch((error) => {
    //             console.log(error)
    //             throw error;
    //         });
    //
    // }, [fetchUsers]);

    const onAdd = useCallback(async (data) => {
        try {
            const response = await userRepository.add(data);
            console.log("Successfully added a new user.");
            await fetchUsers();
            return response.data;
        } catch (error) {
            console.log(error);
            throw error;
        }
    }, [fetchUsers]);

    const onEdit = useCallback((id, data) => {
        userRepository
            .edit(id, data)
            .then(() => {
                console.log(`Successfully edited the user with ID: ${id}`);
                fetchUsers();
            })
            .catch((error) => console.log(error));
    }, [fetchUsers]);

    const onDelete = useCallback((id) => {
        userRepository
            .delete(id)
            .then(() => {
                console.log(`Successfully deleted the user with ID ${id}`);
                fetchUsers();
            })
            .catch((error) => console.log(error));
    }, [fetchUsers]);

    const me = useCallback(() => {
        return userRepository
            .me()
            .then((response) => {
                console.log("Fetched current logged-in user.");
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const register = useCallback((data) => {
        return userRepository
            .register(data)
            .then((response) => {
                console.log("User registration request sent.");
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const login = useCallback((data) => {
        return userRepository
            .login(data)
            .then((response) => {
                console.log("User login request sent.");
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const importUsers = useCallback((data) => {
        return userRepository
            .importUsers(data)
            .then((response) => {
                console.log("Successfully imported users.");
                fetchUsers();
                return response;
            })
            .catch((error) => console.log(error));
    }, [fetchUsers]);

    // const importUsers = useCallback(async (data) => {
    //     try {
    //         const response = await userRepository.importUsers(data);
    //         console.log("Successfully imported users.");
    //         await fetchUsers();
    //         return response.data;
    //     } catch (error) {
    //         console.log(error);
    //         throw error;
    //     }
    // }, [fetchUsers]);

    const exportUsers = useCallback(() => {
        return userRepository
            .exportUsers()
            .then((response) => {
                console.log("Exported users.");
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const requestPasswordReset = useCallback((data) => {
        return userRepository
            .requestPasswordReset(data)
            .then((response) => {
                console.log("Password reset request sent.");
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const confirmPasswordReset = useCallback((data) => {
        return userRepository
            .confirmPasswordReset(data)
            .then((response) => {
                console.log("Password reset confirmed.");
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    return {
        ...state,
        onAdd: onAdd,
        onEdit: onEdit,
        onDelete: onDelete,
        findById: findById,
        me: me,
        register: register,
        login: login,
        importUsers: importUsers,
        exportUsers: exportUsers,
        requestPasswordReset: requestPasswordReset,
        confirmPasswordReset: confirmPasswordReset,
        fetchUsers: fetchUsers,
    };
};

export default useUsers;