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
    register: async (data) => {
        return await axiosInstance.post(`/users/register`, data);
    },
    login: async (data) => {
        return await axiosInstance.post(`/users/login`, data);
    },
    importUsers: async (formData) => {
        return await axiosInstance.post("/users/import", formData, {
            headers: {"Content-Type": "multipart/form-data"},
        });
    },
    exportUsers: async () => {
        return await axiosInstance.get("/users/export", {
            responseType: "blob",
        });
    },
    requestPasswordReset: async (data) => {
        return await axiosInstance.post(`/users/password-reset/request`, data);
    },
    confirmPasswordReset: async (data) => {
        return await axiosInstance.post(`/users/password-reset/confirm`, data);
    },
};

export default userRepository;