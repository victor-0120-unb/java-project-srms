package com.chuka.srms.dao;

import com.chuka.srms.model.Enrollment;
import java.util.List;
import java.util.Optional;

public interface EnrollmentDAO {
    void addEnrollment(Enrollment enrollment) throws Exception;
    void updateEnrollment(Enrollment enrollment) throws Exception;
    void deleteEnrollment(String enrollmentId) throws Exception;
    Optional<Enrollment> getEnrollmentById(String enrollmentId) throws Exception;
    List<Enrollment> getAllEnrollments() throws Exception;
    List<Enrollment> getEnrollmentsByStudent(String studentId) throws Exception;
    List<Enrollment> getEnrollmentsByCourse(String courseId) throws Exception;
    List<Enrollment> getEnrollmentsBySemester(int semester, int year) throws Exception;
    int countEnrollmentsByStudent(String studentId, int semester, int year) throws Exception;
    boolean isStudentEnrolledInCourse(String studentId, String courseId, int semester, int year) throws Exception;
    

}