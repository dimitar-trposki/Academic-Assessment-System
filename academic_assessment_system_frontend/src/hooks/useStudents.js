import {useCallback, useEffect, useState} from "react";
import studentRepository from "../repository/studentRepository.js";

const initialState = {
    "students": [],
    "loading": true,
};

const useStudents = () => {
    const [state, setState] = useState(initialState);

    const fetchStudents = useCallback(() => {
        studentRepository
            .findAll()
            .then((response) => {
                setState({
                    "students": response.data,
                    "loading": false,
                });
            })
            .catch((error) => console.log(error));
    }, []);

    const findById = useCallback((id) => {
        return studentRepository
            .findById(id)
            .then((response) => {
                console.log(`Fetched student with ID: ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const onAdd = useCallback((data) => {
        studentRepository
            .add(data)
            .then((response) => {
                console.log("Successfully added a new student.");
                fetchStudents();
                return response.data;
            })
            .catch((error) => {
                console.log(error);
                throw error;
            });

    }, [fetchStudents]);

    const onEdit = useCallback((id, data) => {
        studentRepository
            .edit(id, data)
            .then(() => {
                console.log(`Successfully edited the student with ID: ${id}`);
                fetchStudents();
            })
            .catch((error) => console.log(error));
    }, [fetchStudents]);

    const onDeleteWithUser = useCallback((id) => {
        studentRepository
            .deleteWithUser(id)
            .then(() => {
                console.log(`Successfully deleted the student with ID ${id} with the associated user.`);
                fetchStudents();
            })
            .catch((error) => console.log(error));
    }, [fetchStudents]);

    const onDeleteWithoutUser = useCallback((id) => {
        studentRepository
            .deleteWithoutUser(id)
            .then(() => {
                console.log(`Successfully deleted the student with ID ${id} without the associated user.`);
                fetchStudents();
            })
            .catch((error) => console.log(error));
    }, [fetchStudents]);

    const findStudentExamRegistrationByStudentId = useCallback((id) => {
        return studentRepository
            .findStudentExamRegistrationByStudentId(id)
            .then((response) => {
                console.log(`Fetched exam registrations for student ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const findCourseEnrollmentByStudentId = useCallback((id) => {
        return studentRepository
            .findCourseEnrollmentByStudentId(id)
            .then((response) => {
                console.log(`Fetched course enrollments for student ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    useEffect(() => {
        fetchStudents();
    }, [fetchStudents]);

    return {
        ...state,
        fetchStudents: fetchStudents,
        onAdd: onAdd,
        onEdit: onEdit,
        onDeleteWithUser: onDeleteWithUser,
        onDeleteWithoutUser: onDeleteWithoutUser,
        findById: findById,
        findStudentExamRegistrationByStudentId: findStudentExamRegistrationByStudentId,
        findCourseEnrollmentByStudentId: findCourseEnrollmentByStudentId,
    };
};

export default useStudents;