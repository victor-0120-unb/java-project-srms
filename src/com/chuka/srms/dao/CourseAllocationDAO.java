package com.chuka.srms.dao;

import com.chuka.srms.model.CourseAllocation;
import java.util.List;
import java.util.Optional;

public interface CourseAllocationDAO {
    void addAllocation(CourseAllocation allocation) throws Exception;
    void updateAllocation(CourseAllocation allocation) throws Exception;
    void deleteAllocation(String allocationId) throws Exception;
    Optional<CourseAllocation> getAllocationById(String allocationId) throws Exception;
    List<CourseAllocation> getAllAllocations() throws Exception;
    List<CourseAllocation> getAllocationsByCourse(String courseId) throws Exception;
    List<CourseAllocation> getAllocationsByTutor(String tutorId) throws Exception;
    List<CourseAllocation> getAllocationsBySemester(int semester, int year) throws Exception;
}