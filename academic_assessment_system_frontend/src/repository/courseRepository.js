import axiosInstance from "../axios/axios.js";

const courseRepository = {
    findAll: async () => {
        return await axiosInstance.get("/courses");
    },
    findById: async (id) => {
        return await axiosInstance.get(`/courses/${id}`);
    },
    add: async (data) => {
        return await axiosInstance.post("/courses/add", data);
    },
    edit: async (id, data) => {
        return await axiosInstance.put(`/courses/${id}/edit`, data);
    },
    delete: async (id) => {
        return await axiosInstance.delete(`/courses/${id}/delete`);
    },
};

export default courseRepository;