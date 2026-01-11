import {useCallback, useEffect, useState} from "react";
import examRepository from "../repository/examRepository.js";

const initialState = {
    "exams": [],
    "loading": true,
};

const useCourses = () => {
    const [state, setState] = useState(initialState);

    const fetchExams = useCallback(() => {
        examRepository
            .findAll()
            .then((response) => {
                setState({
                    "exams": response.data,
                    "loading": false,
                });
            })
            .catch((error) => console.log(error));
    }, []);

    const findById = useCallback((id) => {
        return examRepository
            .findById(id)
            .then((response) => {
                console.log(`Fetched exam with ID: ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const onAdd = useCallback((data) => {
        examRepository
            .add(data)
            .then(() => {
                console.log("Successfully added a new exam.");
                fetchExams();
            })
            .catch((error) => console.log(error));

    }, [fetchExams]);

    const onEdit = useCallback((id, data) => {
        examRepository
            .edit(id, data)
            .then(() => {
                console.log(`Successfully edited the exam with ID: ${id}`);
                fetchExams();
            })
            .catch((error) => console.log(error));
    }, [fetchExams]);

    const onDelete = useCallback((id) => {
        examRepository
            .delete(id)
            .then(() => {
                console.log(`Successfully deleted the exam with ID ${id}`);
                fetchExams();
            })
            .catch((error) => console.log(error));
    }, [fetchExams]);

    const registerForExam = useCallback((id, data) => {
        return examRepository
            .registerForExam(id, data)
            .then(() => {
                console.log(`Registered student for exam ${id}`);
                fetchExams();
            })
            .catch((error) => console.log(error));
    }, [fetchExams]);

    const exportRegisteredStudents = useCallback((id) => {
        return examRepository
            .exportRegisteredStudents(id)
            .then((response) => {
                console.log(`Exported registered students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const importAttendedStudents = useCallback((id, data) => {
        return examRepository
            .importAttendedStudents(id, data)
            .then(() => {
                console.log(`Imported attended students for exam ${id}`);
                fetchExams();
            })
            .catch((error) => console.log(error));
    }, [fetchExams]);

    const exportAttendedStudents = useCallback((id) => {
        return examRepository
            .exportAttendedStudents(id)
            .then((response) => {
                console.log(`Exported attended students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const exportAbsentStudents = useCallback((id) => {
        return examRepository
            .exportAbsentStudents(id)
            .then((response) => {
                console.log(`Exported absent students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const getRegisteredStudents = useCallback((id) => {
        return examRepository
            .getRegisteredStudents(id)
            .then((response) => {
                console.log(`Registered students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const getAttendedStudents = useCallback((id) => {
        return examRepository
            .getAttendedStudents(id)
            .then((response) => {
                console.log(`Attended students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const getAbsentStudents = useCallback((id) => {
        return examRepository
            .getAbsentStudents(id)
            .then((response) => {
                console.log(`Absent students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    useEffect(() => {
        fetchExams();
    }, [fetchExams]);

    return {
        ...state,
        onAdd: onAdd,
        onEdit: onEdit,
        onDelete: onDelete,
        findById: findById,
        registerForExam: registerForExam,
        exportRegisteredStudents: exportRegisteredStudents,
        importAttendedStudents: importAttendedStudents,
        exportAttendedStudents: exportAttendedStudents,
        exportAbsentStudents: exportAbsentStudents,
        getRegisteredStudents: getRegisteredStudents,
        getAttendedStudents: getAttendedStudents,
        getAbsentStudents: getAbsentStudents,
    };
};

export default useCourses;