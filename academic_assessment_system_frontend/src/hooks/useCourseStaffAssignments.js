import {useCallback} from "react";
import courseStaffAssignmentRepository from "../repository/courseStaffAssignmentRepository.js";

const useCourses = () => {
    const getCourseAssignedStaff = useCallback((id) => {
        return courseStaffAssignmentRepository
            .getCourseAssignedStaff(id)
            .then((response) => {
                console.log(`Assigned staff for course ${id}`);
                return response.data;
            })
            .catch((error) => console.log(error));
    }, []);

    return {
        getCourseAssignedStaff: getCourseAssignedStaff,
    };
};

export default useCourses;