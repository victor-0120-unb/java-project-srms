package com.chuka.srms.dao.database;

import com.chuka.srms.dao.StudentDAO;
import com.chuka.srms.model.Student;
import com.chuka.srms.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class StudentDatabaseDAO implements StudentDAO {
    
    @Override
    public void addStudent(Student student) throws SQLException, ClassNotFoundException{
        String sql = "INSERT INTO students (student_id, first_name, last_name, date_of_birth, " +
                    "major, current_semester, current_year, academic_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setDate(4, java.sql.Date.valueOf(student.getDateOfBirth()));
            pstmt.setString(5, student.getMajor());
            pstmt.setInt(6, student.getCurrentSemester());
            pstmt.setInt(7, student.getCurrentYear());
            pstmt.setString(8, student.getAcademicStatus());
            pstmt.executeUpdate();
        }
    }
    
    @Override
    public void updateStudent(Student student) throws SQLException, ClassNotFoundException{
        String sql = "UPDATE students SET first_name=?, last_name=?, date_of_birth=?, " +
                    "major=?, current_semester=?, current_year=?, academic_status=? WHERE student_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setDate(3, java.sql.Date.valueOf(student.getDateOfBirth()));
            pstmt.setString(4, student.getMajor());
            pstmt.setInt(5, student.getCurrentSemester());
            pstmt.setInt(6, student.getCurrentYear());
            pstmt.setString(7, student.getAcademicStatus());
            pstmt.setString(8, student.getStudentId());
            int rows = pstmt.executeUpdate();
            if (rows == 0) throw new SQLException("Student not found: " + student.getStudentId());
        }
    }
    
    @Override
    public void deleteStudent(String studentId) throws SQLException, ClassNotFoundException  {
        String sql = "DELETE FROM students WHERE student_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) throw new SQLException("Student not found: " + studentId);
        }
    }
    
    @Override
    public Optional<Student> getStudentById(String studentId) throws SQLException, ClassNotFoundException  {
        String sql = "SELECT * FROM students WHERE student_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Student> getAllStudents() throws SQLException, ClassNotFoundException  {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(mapResultSet(rs));
            }
        }
        return students;
    }
    
    @Override
    public int countPassedCourses(String studentId, int semester, int year) throws SQLException, ClassNotFoundException  {
        String sql = "SELECT COUNT(*) FROM examination_results er " +
                    "JOIN enrollments e ON er.enrollment_id = e.enrollment_id " +
                    "WHERE e.student_id = ? AND e.semester = ? AND e.year = ? " +
                    "AND er.grade IN ('A', 'B', 'C', 'D')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, semester);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    
    @Override
    public boolean hasMetGraduationRequirements(String studentId) throws SQLException, ClassNotFoundException  {
        String sql = "SELECT SUM(c.credits) FROM examination_results er " +
                    "JOIN enrollments e ON er.enrollment_id = e.enrollment_id " +
                    "JOIN courses c ON e.course_id = c.course_id " +
                    "WHERE e.student_id = ? AND er.grade IN ('A', 'B', 'C', 'D')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int totalCredits = rs.getInt(1);
                return totalCredits >= 120;
            }
        }
        return false;
    }
    
    private Student mapResultSet(ResultSet rs) throws SQLException {
        return new Student(
            rs.getString("student_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getDate("date_of_birth").toLocalDate(),
            rs.getString("major"),
            rs.getInt("current_semester"),
            rs.getInt("current_year"),
            rs.getString("academic_status")
        );
    }
}