package com.chuka.srms.dao;

import com.chuka.srms.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    void addCourse(Course course) throws Exception;
    void updateCourse(Course course) throws Exception;
    void deleteCourse(String courseId) throws Exception;
    Optional<Course> getCourseById(String courseId) throws Exception;
    List<Course> getAllCourses() throws Exception;
    List<Course> getCoursesByDepartment(String department) throws Exception;
}

