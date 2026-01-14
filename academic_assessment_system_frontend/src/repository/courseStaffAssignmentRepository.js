import axiosInstance from "../axios/axios.js";

const courseStaffAssignmentRepository = {
    getCourseAssignedStaff: async (courseId) => {
        return await axiosInstance.get(`/courses/${courseId}/assigned-staff`);
    },
};

export default courseStaffAssignmentRepository;