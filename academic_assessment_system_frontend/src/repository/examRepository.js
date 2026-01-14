import axiosInstance from "../axios/axios.js";

const examRepository = {
    findAll: async () => {
        return await axiosInstance.get("/exams");
    },
    findById: async (id) => {
        return await axiosInstance.get(`/exams/${id}`);
    },
    add: async (data) => {
        return await axiosInstance.post("/exams/add", data);
    },
    edit: async (id, data) => {
        return await axiosInstance.put(`/exams/${id}/edit`, data);
    },
    delete: async (id) => {
        return await axiosInstance.delete(`/exams/${id}/delete`);
    },
};

export default examRepository;