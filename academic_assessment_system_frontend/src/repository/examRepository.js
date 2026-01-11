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
    registerForExam: async (id, data) => {
        return await axiosInstance.post(`/exams/${id}/register`, data);
    },
    exportRegisteredStudents: async (id) => {
        return await axiosInstance.get(`/exams/${id}/registered-students/export`);
    },
    importAttendedStudents: async (id, data) => {
        return await axiosInstance.post(`/exams/${id}/attended-students/import`, data);
    },
    exportAttendedStudents: async (id) => {
        return await axiosInstance.get(`/exams/${id}/attended-students/export`);
    },
    exportAbsentStudents: async (id) => {
        return await axiosInstance.get(`/exams/${id}/absent-students/export`);
    },
    getRegisteredStudents: async (id) => {
        return await axiosInstance.get(`/exams/${id}/registered-students`);
    },
    getAttendedStudents: async (id) => {
        return await axiosInstance.get(`/exams/${id}/attended-students`);
    },
    getAbsentStudents: async (id) => {
        return await axiosInstance.get(`/exams/${id}/absent-students`);
    },
};

export default examRepository;