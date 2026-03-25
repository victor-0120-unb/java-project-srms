package com.chuka.srms.service;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.CourseAllocationDAO;
import com.chuka.srms.dao.database.CourseAllocationDatabaseDAO;
import com.chuka.srms.dao.file.CourseAllocationFileDAO;
import com.chuka.srms.model.CourseAllocation;
import java.util.List;

public class CourseAllocationService {
    private CourseAllocationDAO allocationDAO;
    
    public CourseAllocationService() {
        if (AppConfig.isDatabaseMode()) {
            allocationDAO = new CourseAllocationDatabaseDAO();
        } else {
            allocationDAO = new CourseAllocationFileDAO();
        }
    }
    
    public void addAllocation(CourseAllocation allocation) throws Exception {
        validateAllocation(allocation);
        allocationDAO.addAllocation(allocation);
    }
    
    public void updateAllocation(CourseAllocation allocation) throws Exception {
        validateAllocation(allocation);
        allocationDAO.updateAllocation(allocation);
    }
    
    public void deleteAllocation(String allocationId) throws Exception {
        allocationDAO.deleteAllocation(allocationId);
    }
    
    public CourseAllocation getAllocationById(String allocationId) throws Exception {
        return allocationDAO.getAllocationById(allocationId)
            .orElseThrow(() -> new IllegalArgumentException("Allocation not found"));
    }
    
    public List<CourseAllocation> getAllAllocations() throws Exception {
        return allocationDAO.getAllAllocations();
    }
    
    public List<CourseAllocation> getAllocationsByTutor(String tutorId) throws Exception {
        return allocationDAO.getAllocationsByTutor(tutorId);
    }
    
    private void validateAllocation(CourseAllocation allocation) {
        if (allocation == null) throw new IllegalArgumentException("Allocation cannot be null");
        if (allocation.getAllocationId() == null || allocation.getAllocationId().trim().isEmpty())
            throw new IllegalArgumentException("Allocation ID required");
        if (allocation.getCourseId() == null || allocation.getCourseId().trim().isEmpty())
            throw new IllegalArgumentException("Course ID required");
        if (allocation.getTutorId() == null || allocation.getTutorId().trim().isEmpty())
            throw new IllegalArgumentException("Tutor ID required");
        if (allocation.getSemester() < 1 || allocation.getSemester() > 2)
            throw new IllegalArgumentException("Semester must be 1 or 2");
    }
}