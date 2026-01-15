import {useCallback, useEffect, useState} from "react";
import courseRepository from "../repository/courseRepository.js";

const initialState = {
    "courses": [],
    "loading": true,
};

const useCourses = () => {
    const [state, setState] = useState(initialState);

    const fetchCourses = useCallback(() => {
        courseRepository
            .findAll()
            .then((response) => {
                setState({
                    "courses": response.data,
                    "loading": false,
                });
            })
            .catch((error) => console.log(error));
    }, []);

    const findById = useCallback((id) => {
        return courseRepository
            .findById(id)
            .then((response) => {
                console.log(`Fetched course with ID: ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const onAdd = useCallback((data) => {
        courseRepository
            .add(data)
            .then(() => {
                console.log("Successfully added a new course.");
                fetchCourses();
            })
            .catch((error) => console.log(error));

    }, [fetchCourses]);

    const onEdit = useCallback((id, data) => {
        courseRepository
            .edit(id, data)
            .then(() => {
                console.log(`Successfully edited the course with ID: ${id}`);
                fetchCourses();
            })
            .catch((error) => console.log(error));
    }, [fetchCourses]);

    const onDelete = useCallback((id) => {
        courseRepository
            .delete(id)
            .then(() => {
                console.log(`Successfully deleted the course with ID ${id}`);
                fetchCourses();
            })
            .catch((error) => console.log(error));
    }, [fetchCourses]);

    const findAllForStaff = useCallback(() => {
        return courseRepository
            .findAllForStaff()
            .then((response) => {
                console.log("Fetched courses for current staff user.");
                return response.data;
            })
            .catch((error) => {
                console.log(error);
                throw error;
            });
    }, []);

    const findAllForStudent = useCallback(() => {
        return courseRepository
            .findAllForStudent()
            .then((response) => {
                console.log("Fetched courses for current student user.");
                return response.data;
            })
            .catch((error) => {
                console.log(error);
                throw error;
            });
    }, []);

    useEffect(() => {
        fetchCourses();
    }, [fetchCourses]);

    return {
        ...state,
        onAdd: onAdd,
        onEdit: onEdit,
        onDelete: onDelete,
        findById: findById,
        findAllForStaff: findAllForStaff,
        findAllForStudent: findAllForStudent,
    };
};

export default useCourses;