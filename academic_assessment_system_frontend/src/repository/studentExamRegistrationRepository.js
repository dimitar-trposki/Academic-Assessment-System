import axiosInstance from "../axios/axios.js";

const studentExamRegistrationRepository = {
    registerForExam: async (examId) => {
        return await axiosInstance.post(`/exams/${examId}/register`);
    },
    exportRegisteredStudents: async (examId) => {
        return await axiosInstance.get(
            `/exams/${examId}/registered-students/export`,
            {responseType: "blob"}
        );
    },
    importAttendedStudents: async (examId, formData) => {
        return await axiosInstance.post(
            `/exams/${examId}/attended-students/import`,
            formData,
            {
                headers: {"Content-Type": "multipart/form-data"},
            }
        );
    },
    exportAttendedStudents: async (examId) => {
        return await axiosInstance.get(
            `/exams/${examId}/attended-students/export`,
            {responseType: "blob"}
        );
    },
    exportAbsentStudents: async (examId) => {
        return await axiosInstance.get(
            `/exams/${examId}/absent-students/export`,
            {responseType: "blob"}
        );
    },
    getRegisteredStudents: async (examId) => {
        return await axiosInstance.get(`/exams/${examId}/registered-students`);
    },
    getAttendedStudents: async (examId) => {
        return await axiosInstance.get(`/exams/${examId}/attended-students`);
    },
    getAbsentStudents: async (examId) => {
        return await axiosInstance.get(`/exams/${examId}/absent-students`);
    },
};

export default studentExamRegistrationRepository;