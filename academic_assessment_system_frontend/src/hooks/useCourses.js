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

    const exportEnrolledStudentsCsv = useCallback((id) => {
        return courseRepository
            .exportEnrolledStudentsCsv(id)
            .then((response) => {
                console.log(`Exported enrolled students CSV for course ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const importEnrolledStudentsCsv = useCallback(
        (id, data) => {
            return courseRepository
                .importEnrolledStudentsCsv(id, data)
                .then(() => {
                    console.log(
                        `Imported enrolled students CSV for course ${id}`
                    );
                    fetchCourses();
                })
                .catch((error) => console.log(error));
        },
        [fetchCourses]
    );

    const getEnrolledStudents = useCallback((id) => {
        return courseRepository
            .getEnrolledStudents(id)
            .then((response) => {
                console.log(`Enrolled students for course ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const getCourseAssignedStaff = useCallback((id) => {
        return courseRepository
            .getCourseAssignedStaff(id)
            .then((response) => {
                console.log(`Assigned staff for course ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
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
        exportEnrolledStudentsCsv: exportEnrolledStudentsCsv,
        importEnrolledStudentsCsv: importEnrolledStudentsCsv,
        getEnrolledStudents: getEnrolledStudents,
        getCourseAssignedStaff: getCourseAssignedStaff,
    };
};

export default useCourses;