import {useCallback, useEffect, useState} from "react";
import examRepository from "../repository/examRepository.js";

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

    useEffect(() => {
        fetchExams();
    }, [fetchExams]);

    return {
        ...state,
        onAdd: onAdd,
        onEdit: onEdit,
        onDelete: onDelete,
        findById: findById,
    };
};

export default useExams;