import {useCallback, useEffect, useState} from "react";
import courseRepository from "../repository/courseRepository.js";
import courseEnrollmentRepository from "../repository/courseEnrollmentRepository.js";

const initialState = {
    "courses": [],
    "loading": true,
};

const useCourseEnrollments = () => {
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

    const exportEnrolledStudentsCsv = useCallback((id) => {
        return courseEnrollmentRepository
            .exportEnrolledStudentsCsv(id)
            .then((response) => {
                console.log(`Exported enrolled students CSV for course ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const importEnrolledStudentsCsv = useCallback(
        (id, data) => {
            return courseEnrollmentRepository
                .importEnrolledStudentsCsv(id, data)
                .then((response) => {
                    console.log(
                        `Imported enrolled students CSV for course ${id}`
                    );
                    fetchCourses();
                    return response;
                })
                .catch((error) => console.log(error));
        },
        [fetchCourses]
    );

    const getEnrolledStudents = useCallback((id) => {
        return courseEnrollmentRepository
            .getEnrolledStudents(id)
            .then((response) => {
                console.log(`Enrolled students for course ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    useEffect(() => {
        fetchCourses();
    }, [fetchCourses]);

    return {
        ...state,
        exportEnrolledStudentsCsv: exportEnrolledStudentsCsv,
        importEnrolledStudentsCsv: importEnrolledStudentsCsv,
        getEnrolledStudents: getEnrolledStudents,
    };
};

export default useCourseEnrollments;