import axiosInstance from "../axios/axios.js";

const studentRepository = {
    findAll: async () => {
        return await axiosInstance.get("/students");
    },
    findById: async (id) => {
        return await axiosInstance.get(`/students/${id}`);
    },
    add: async (data) => {
        return await axiosInstance.post("/students/add", data);
    },
    edit: async (id, data) => {
        return await axiosInstance.put(`/students/${id}/edit`, data);
    },
    delete: async (id) => {
        return await axiosInstance.delete(`/students/${id}/delete`);
    },
    findStudentExamRegistrationByStudentId: async (id) => {
        return await axiosInstance.get(`/students/${id}/exam-registrations`);
    },
    findCourseEnrollmentByStudentId: async (id) => {
        return await axiosInstance.get(`/students/${id}/course-enrollments`);
    },
};

export default studentRepository;