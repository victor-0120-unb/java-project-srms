package com.chuka.srms.dao.database;

import com.chuka.srms.dao.EnrollmentDAO;
import com.chuka.srms.model.Enrollment;
import com.chuka.srms.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class EnrollmentDatabaseDAO implements EnrollmentDAO {
    
    @Override
    public void addEnrollment(Enrollment enrollment) throws SQLException, ClassNotFoundException{
        String sql = "INSERT INTO enrollments (enrollment_id, student_id, course_id, " +
                    "semester, year, enrollment_date) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enrollment.getEnrollmentId());
            pstmt.setString(2, enrollment.getStudentId());
            pstmt.setString(3, enrollment.getCourseId());
            pstmt.setInt(4, enrollment.getSemester());
            pstmt.setInt(5, enrollment.getYear());
            pstmt.setDate(6, java.sql.Date.valueOf(enrollment.getEnrollmentDate()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Enrollment ID already exists: " + enrollment.getEnrollmentId(), e);
            }
            if (e.getErrorCode() == 1452) {
                throw new SQLException("Invalid student ID or course ID - foreign key constraint failed", e);
            }
            throw e;
        }
    }
    
    @Override
    public void updateEnrollment(Enrollment enrollment) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE enrollments SET student_id=?, course_id=?, semester=?, " +
                    "year=?, enrollment_date=? WHERE enrollment_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enrollment.getStudentId());
            pstmt.setString(2, enrollment.getCourseId());
            pstmt.setInt(3, enrollment.getSemester());
            pstmt.setInt(4, enrollment.getYear());
            pstmt.setDate(5, java.sql.Date.valueOf(enrollment.getEnrollmentDate()));
            pstmt.setString(6, enrollment.getEnrollmentId());
            
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Enrollment not found: " + enrollment.getEnrollmentId());
            }
        }
    }
    
    @Override
    public void deleteEnrollment(String enrollmentId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM enrollments WHERE enrollment_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enrollmentId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Enrollment not found: " + enrollmentId);
            }
        }
    }
    
    @Override
    public Optional<Enrollment> getEnrollmentById(String enrollmentId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM enrollments WHERE enrollment_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enrollmentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        }
        return Optional.empty();
    }
    
    

    @Override
    public List<Enrollment> getAllEnrollments() throws SQLException, ClassNotFoundException{
    String sql = "SELECT * FROM enrollments";
    List<Enrollment> enrollments = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Enrollment enrollment = new Enrollment(
                rs.getString("enrollment_id"),
                rs.getString("student_id"),
                rs.getString("course_id"),
                rs.getInt("semester"),
                rs.getInt("year"),
                rs.getDate("enrollment_date").toLocalDate()
            );
            enrollments.add(enrollment);
        }
    }
    return enrollments;
}

    
    @Override
    public List<Enrollment> getEnrollmentsByStudent(String studentId) throws SQLException, ClassNotFoundException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id=? ORDER BY year DESC, semester DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                enrollments.add(mapResultSet(rs));
            }
        }
        return enrollments;
    }
    
    @Override
    public List<Enrollment> getEnrollmentsByCourse(String courseId) throws SQLException, ClassNotFoundException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE course_id=? ORDER BY year DESC, semester DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                enrollments.add(mapResultSet(rs));
            }
        }
        return enrollments;
    }
    
    @Override
    public List<Enrollment> getEnrollmentsBySemester(int semester, int year) throws SQLException, ClassNotFoundException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE semester=? AND year=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, semester);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                enrollments.add(mapResultSet(rs));
            }
        }
        return enrollments;
    }
    
    @Override
    public int countEnrollmentsByStudent(String studentId, int semester, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id=? AND semester=? AND year=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, semester);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    @Override
    public boolean isStudentEnrolledInCourse(String studentId, String courseId, int semester, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id=? AND course_id=? AND semester=? AND year=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);
            pstmt.setInt(3, semester);
            pstmt.setInt(4, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    private Enrollment mapResultSet(ResultSet rs) throws SQLException {
        return new Enrollment(
            rs.getString("enrollment_id"),
            rs.getString("student_id"),
            rs.getString("course_id"),
            rs.getInt("semester"),
            rs.getInt("year"),
            rs.getDate("enrollment_date").toLocalDate()
        );
    }

}