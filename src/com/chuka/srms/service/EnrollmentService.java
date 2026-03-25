package com.chuka.srms.service;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.EnrollmentDAO;
import com.chuka.srms.dao.database.EnrollmentDatabaseDAO;
import com.chuka.srms.dao.file.EnrollmentFileDAO;
import com.chuka.srms.model.Enrollment;
import java.time.LocalDate;
import java.util.List;

public class EnrollmentService {
    private EnrollmentDAO enrollmentDAO;
    private static final int MIN_COURSES_PER_SEMESTER = 10;
    
    public EnrollmentService() {
        if (AppConfig.isDatabaseMode()) {
            enrollmentDAO = new EnrollmentDatabaseDAO();
        } else {
            enrollmentDAO = new EnrollmentFileDAO();
        }
    }
    
    public void addEnrollment(Enrollment enrollment) throws Exception {
        validateEnrollment(enrollment);
        
        // Check minimum course load
        int currentEnrollments = enrollmentDAO.countEnrollmentsByStudent(
            enrollment.getStudentId(), enrollment.getSemester(), enrollment.getYear());
        
        if (currentEnrollments >= MIN_COURSES_PER_SEMESTER) {
            throw new IllegalArgumentException("Student already registered for minimum " + 
                MIN_COURSES_PER_SEMESTER + " courses this semester");
        }
        
        // Check if already enrolled
        if (enrollmentDAO.isStudentEnrolledInCourse(enrollment.getStudentId(), 
                enrollment.getCourseId(), enrollment.getSemester(), enrollment.getYear())) {
            throw new IllegalArgumentException("Student already enrolled in this course");
        }
        
        enrollmentDAO.addEnrollment(enrollment);
    }
    
    public void updateEnrollment(Enrollment enrollment) throws Exception {
        validateEnrollment(enrollment);
        enrollmentDAO.updateEnrollment(enrollment);
    }
    
    public void deleteEnrollment(String enrollmentId) throws Exception {
        enrollmentDAO.deleteEnrollment(enrollmentId);
    }
    
    public Enrollment getEnrollmentById(String enrollmentId) throws Exception {
        return enrollmentDAO.getEnrollmentById(enrollmentId)
            .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
    }
    
    public List<Enrollment> getAllEnrollments() throws Exception {
        return enrollmentDAO.getAllEnrollments();
    }
    
    public List<Enrollment> getEnrollmentsByStudent(String studentId) throws Exception {
        return enrollmentDAO.getEnrollmentsByStudent(studentId);
    }
    
    private void validateEnrollment(Enrollment enrollment) {
        if (enrollment == null) throw new IllegalArgumentException("Enrollment cannot be null");
        if (enrollment.getEnrollmentId() == null || enrollment.getEnrollmentId().trim().isEmpty())
            throw new IllegalArgumentException("Enrollment ID required");
        if (enrollment.getStudentId() == null || enrollment.getStudentId().trim().isEmpty())
            throw new IllegalArgumentException("Student ID required");
        if (enrollment.getCourseId() == null || enrollment.getCourseId().trim().isEmpty())
            throw new IllegalArgumentException("Course ID required");
        if (enrollment.getSemester() < 1 || enrollment.getSemester() > 2)
            throw new IllegalArgumentException("Semester must be 1 or 2");
        if (enrollment.getYear() < 2020 || enrollment.getYear() > 2030)
            throw new IllegalArgumentException("Invalid year");
        if (enrollment.getEnrollmentDate() == null)
            enrollment.setEnrollmentDate(LocalDate.now());
    }

    public List<Enrollment> getEnrollmentsByCourse(String courseId) throws Exception {
    return enrollmentDAO.getEnrollmentsByCourse(courseId);
}

public int countEnrollmentsByStudent(String studentId, int semester, int year) throws Exception {
    return enrollmentDAO.countEnrollmentsByStudent(studentId, semester, year);
}

}