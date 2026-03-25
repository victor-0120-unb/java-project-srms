package com.chuka.srms.dao;

import com.chuka.srms.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    void addStudent(Student student) throws Exception;
    void updateStudent(Student student) throws Exception;
    void deleteStudent(String studentId) throws Exception;
    Optional<Student> getStudentById(String studentId) throws Exception;
    List<Student> getAllStudents() throws Exception;
    int countPassedCourses(String studentId, int semester, int year) throws Exception;
    boolean hasMetGraduationRequirements(String studentId) throws Exception;
}