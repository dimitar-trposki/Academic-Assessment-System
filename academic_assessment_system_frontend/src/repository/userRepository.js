import axiosInstance from "../axios/axios.js";

const userRepository = {
    findAll: async () => {
        return await axiosInstance.get("/users");
    },
    findById: async (id) => {
        return await axiosInstance.get(`/users/${id}`);
    },
    add: async (data) => {
        return await axiosInstance.post("/users/add", data);
    },
    edit: async (id, data) => {
        return await axiosInstance.put(`/users/${id}/edit`, data);
    },
    delete: async (id) => {
        return await axiosInstance.delete(`/users/${id}/delete`);
    },
    me: async () => {
        return await axiosInstance.get(`/users/me`);
    },
    register: async () => {
        return await axiosInstance.get(`/users/register`);
    },
    login: async () => {
        return await axiosInstance.get(`/users/login`);
    },
    importUsers: async (data) => {
        return await axiosInstance.post(`/users/import`, data);
    },
    exportUsers: async () => {
        return await axiosInstance.get(`/users/export`);
    },
    requestPasswordReset: async (data) => {
        return await axiosInstance.post(`/users/password-reset/request`, data);
    },
    confirmPasswordReset: async (data) => {
        return await axiosInstance.post(`/users/password-reset/confirm`, data);
    },
};

export default userRepository;