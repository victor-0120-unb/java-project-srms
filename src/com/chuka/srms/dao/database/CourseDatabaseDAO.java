package com.chuka.srms.dao.database;

import com.chuka.srms.dao.CourseDAO;
import com.chuka.srms.model.Course;
import com.chuka.srms.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class CourseDatabaseDAO implements CourseDAO {
    
    @Override
    public void addCourse(Course course) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO courses (course_id, course_name, credits, department) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getDepartment());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Course ID already exists: " + course.getCourseId(), e);
            }
            throw e;
        }
    }
    
    @Override
    public void updateCourse(Course course) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE courses SET course_name=?, credits=?, department=? WHERE course_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setInt(2, course.getCredits());
            pstmt.setString(3, course.getDepartment());
            pstmt.setString(4, course.getCourseId());
            
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Course not found: " + course.getCourseId());
            }
        }
    }
    
    @Override
    public void deleteCourse(String courseId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM courses WHERE course_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Course not found: " + courseId);
            }
        }
    }
    
    @Override
    public Optional<Course> getCourseById(String courseId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM courses WHERE course_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Course> getAllCourses() throws SQLException, ClassNotFoundException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(mapResultSet(rs));
            }
        }
        return courses;
    }
    
    @Override
    public List<Course> getCoursesByDepartment(String department) throws SQLException, ClassNotFoundException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE department=? ORDER BY course_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(mapResultSet(rs));
            }
        }
        return courses;
    }
    
    private Course mapResultSet(ResultSet rs) throws SQLException {
        return new Course(
            rs.getString("course_id"),
            rs.getString("course_name"),
            rs.getInt("credits"),
            rs.getString("department")
        );
    }
}