package com.chuka.srms.service;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.CourseDAO;
import com.chuka.srms.dao.database.CourseDatabaseDAO;
import com.chuka.srms.dao.file.CourseFileDAO;
import com.chuka.srms.model.Course;
import java.util.List;

public class CourseService {
    private CourseDAO courseDAO;
    
    public CourseService() {
        if (AppConfig.isDatabaseMode()) {
            courseDAO = new CourseDatabaseDAO();
        } else {
            courseDAO = new CourseFileDAO();
        }
    }
    
    public void addCourse(Course course) throws Exception {
        validateCourse(course);
        courseDAO.addCourse(course);
    }
    
    public void updateCourse(Course course) throws Exception {
        validateCourse(course);
        courseDAO.updateCourse(course);
    }
    
    public void deleteCourse(String courseId) throws Exception {
        courseDAO.deleteCourse(courseId);
    }
    
    public Course getCourseById(String courseId) throws Exception {
        return courseDAO.getCourseById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }
    
    public List<Course> getAllCourses() throws Exception {
        return courseDAO.getAllCourses();
    }
    
    private void validateCourse(Course course) {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty())
            throw new IllegalArgumentException("Course ID required");
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty())
            throw new IllegalArgumentException("Course name required");
        if (course.getCredits() <= 0)
            throw new IllegalArgumentException("Credits must be positive");
    }
}