package com.chuka.srms.dao.database;

import com.chuka.srms.dao.CourseAllocationDAO;
import com.chuka.srms.model.CourseAllocation;
import com.chuka.srms.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class CourseAllocationDatabaseDAO implements CourseAllocationDAO {
    
    @Override
    public void addAllocation(CourseAllocation allocation) throws SQLException, ClassNotFoundException  {
        String sql = "INSERT INTO course_allocations (allocation_id, course_id, tutor_id, " +
                    "semester, year) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, allocation.getAllocationId());
            pstmt.setString(2, allocation.getCourseId());
            pstmt.setString(3, allocation.getTutorId());
            pstmt.setInt(4, allocation.getSemester());
            pstmt.setInt(5, allocation.getYear());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Allocation ID already exists: " + allocation.getAllocationId(), e);
            }
            if (e.getErrorCode() == 1452) {
                throw new SQLException("Invalid course ID or tutor ID - foreign key constraint failed", e);
            }
            throw e;
        }
    }
    
    @Override
    public void updateAllocation(CourseAllocation allocation) throws SQLException,  ClassNotFoundException  {
        String sql = "UPDATE course_allocations SET course_id=?, tutor_id=?, semester=?, year=? " +
                    "WHERE allocation_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, allocation.getCourseId());
            pstmt.setString(2, allocation.getTutorId());
            pstmt.setInt(3, allocation.getSemester());
            pstmt.setInt(4, allocation.getYear());
            pstmt.setString(5, allocation.getAllocationId());
            
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Allocation not found: " + allocation.getAllocationId());
            }
        }
    }
    
    @Override
    public void deleteAllocation(String allocationId) throws SQLException,  ClassNotFoundException  {
        String sql = "DELETE FROM course_allocations WHERE allocation_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, allocationId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Allocation not found: " + allocationId);
            }
        }
    }
    
    @Override
    public Optional<CourseAllocation> getAllocationById(String allocationId) throws SQLException,  ClassNotFoundException {
        String sql = "SELECT * FROM course_allocations WHERE allocation_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, allocationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<CourseAllocation> getAllAllocations() throws SQLException,  ClassNotFoundException  {
        List<CourseAllocation> allocations = new ArrayList<>();
        String sql = "SELECT * FROM course_allocations ORDER BY allocation_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allocations.add(mapResultSet(rs));
            }
        }
        return allocations;
    }
    
    @Override
    public List<CourseAllocation> getAllocationsByCourse(String courseId) throws SQLException,  ClassNotFoundException  {
        List<CourseAllocation> allocations = new ArrayList<>();
        String sql = "SELECT * FROM course_allocations WHERE course_id=? ORDER BY year DESC, semester DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                allocations.add(mapResultSet(rs));
            }
        }
        return allocations;
    }
    
    @Override
    public List<CourseAllocation> getAllocationsByTutor(String tutorId) throws SQLException, ClassNotFoundException  {
        List<CourseAllocation> allocations = new ArrayList<>();
        String sql = "SELECT * FROM course_allocations WHERE tutor_id=? ORDER BY year DESC, semester DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tutorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                allocations.add(mapResultSet(rs));
            }
        }
        return allocations;
    }
    
    @Override
    public List<CourseAllocation> getAllocationsBySemester(int semester, int year) throws SQLException, ClassNotFoundException  {
        List<CourseAllocation> allocations = new ArrayList<>();
        String sql = "SELECT * FROM course_allocations WHERE semester=? AND year=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, semester);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                allocations.add(mapResultSet(rs));
            }
        }
        return allocations;
    }
    
    private CourseAllocation mapResultSet(ResultSet rs) throws SQLException {
        return new CourseAllocation(
            rs.getString("allocation_id"),
            rs.getString("course_id"),
            rs.getString("tutor_id"),
            rs.getInt("semester"),
            rs.getInt("year")
        );
    }
}