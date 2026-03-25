package com.chuka.srms.service;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.StudentDAO;
import com.chuka.srms.dao.database.StudentDatabaseDAO;
import com.chuka.srms.dao.file.StudentFileDAO;
import com.chuka.srms.model.Student;
import java.util.List;

public class StudentService {
    private StudentDAO studentDAO;
    
    public StudentService() {
        if (AppConfig.isDatabaseMode()) {
            studentDAO = new StudentDatabaseDAO();
        } else {
            studentDAO = new StudentFileDAO();
        }
    }
    
    public void addStudent(Student student) throws Exception {
        validateStudent(student);
        studentDAO.addStudent(student);
    }
    
    public void updateStudent(Student student) throws Exception {
        validateStudent(student);
        studentDAO.updateStudent(student);
    }
    
    public void deleteStudent(String studentId) throws Exception {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty");
        }
        studentDAO.deleteStudent(studentId);
    }
    
    public Student getStudentById(String studentId) throws Exception {
        return studentDAO.getStudentById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
    }
    
    public List<Student> getAllStudents() throws Exception {
        return studentDAO.getAllStudents();
    }
    
    public int countPassedCourses(String studentId, int semester, int year) throws Exception {
        return studentDAO.countPassedCourses(studentId, semester, year);
    }
    
    public boolean hasMetGraduationRequirements(String studentId) throws Exception {
        return studentDAO.hasMetGraduationRequirements(studentId);
    }
    
    private void validateStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty())
            throw new IllegalArgumentException("Student ID is required");
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty())
            throw new IllegalArgumentException("First name is required");
        if (student.getLastName() == null || student.getLastName().trim().isEmpty())
            throw new IllegalArgumentException("Last name is required");
        if (student.getDateOfBirth() == null)
            throw new IllegalArgumentException("Date of birth is required");
        if (student.getMajor() == null || student.getMajor().trim().isEmpty())
            throw new IllegalArgumentException("Major is required");
        if (student.getCurrentSemester() < 1 || student.getCurrentSemester() > 2)
            throw new IllegalArgumentException("Semester must be 1 or 2");
        if (student.getCurrentYear() < 1 || student.getCurrentYear() > 4)
            throw new IllegalArgumentException("Year must be between 1 and 4");
    }
}