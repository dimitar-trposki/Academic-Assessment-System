import {useCallback, useEffect, useState} from "react";
import examRepository from "../repository/examRepository.js";
import studentExamRegistrationRepository from "../repository/studentExamRegistrationRepository.js";

const initialState = {
    "exams": [],
    "loading": true,
};

const useExams = () => {
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

    const registerForExam = useCallback((id) => {
        return studentExamRegistrationRepository
            .registerForExam(id)
            .then(() => {
                console.log(`Registered student for exam ${id}`);
                fetchExams();
            })
            .catch((error) => console.log(error));
    }, [fetchExams]);

    const exportRegisteredStudents = useCallback((id) => {
        return studentExamRegistrationRepository
            .exportRegisteredStudents(id)
            .then((response) => {
                console.log(`Exported registered students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const importAttendedStudents = useCallback((id, data) => {
        return studentExamRegistrationRepository
            .importAttendedStudents(id, data)
            .then((response) => {
                console.log(`Imported attended students for exam ${id}`);
                fetchExams();
                return response;
            })
            .catch((error) => console.log(error));
    }, [fetchExams]);

    const exportAttendedStudents = useCallback((id) => {
        return studentExamRegistrationRepository
            .exportAttendedStudents(id)
            .then((response) => {
                console.log(`Exported attended students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const exportAbsentStudents = useCallback((id) => {
        return studentExamRegistrationRepository
            .exportAbsentStudents(id)
            .then((response) => {
                console.log(`Exported absent students for exam ${id}`);
                return response;
            })
            .catch((error) => console.log(error));
    }, []);

    const getRegisteredStudents = useCallback((id) => {
        return studentExamRegistrationRepository
            .getRegisteredStudents(id)
            .then((response) => {
                console.log(`Registered students for exam ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const getAttendedStudents = useCallback((id) => {
        return studentExamRegistrationRepository
            .getAttendedStudents(id)
            .then((response) => {
                console.log(`Attended students for exam ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    const getAbsentStudents = useCallback((id) => {
        return studentExamRegistrationRepository
            .getAbsentStudents(id)
            .then((response) => {
                console.log(`Absent students for exam ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    useEffect(() => {
        fetchExams();
    }, [fetchExams]);

    return {
        ...state,
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

export default useExams;