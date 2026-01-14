import axiosInstance from "../axios/axios.js";

const courseEnrollmentRepository = {
    exportEnrolledStudentsCsv: async (courseId) => {
        return await axiosInstance.get(`/courses/${courseId}/export`, {
            responseType: "blob",
        });
    },
    importEnrolledStudentsCsv: async (courseId, formData) => {
        return await axiosInstance.post(`/courses/${courseId}/import`, formData, {
            headers: {"Content-Type": "multipart/form-data"},
        });
    },
    getEnrolledStudents: async (courseId) => {
        return await axiosInstance.get(`/courses/${courseId}/enrolled-students`);
    },
};

export default courseEnrollmentRepository;