package com.chuka.srms.dao.database;

import com.chuka.srms.dao.TutorDAO;
import com.chuka.srms.model.Tutor;
import com.chuka.srms.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class TutorDatabaseDAO implements TutorDAO {
    
    @Override
    public void addTutor(Tutor tutor) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tutors (tutor_id, first_name, last_name, department) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tutor.getTutorId());
            pstmt.setString(2, tutor.getFirstName());
            pstmt.setString(3, tutor.getLastName());
            pstmt.setString(4, tutor.getDepartment());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Tutor ID already exists: " + tutor.getTutorId(), e);
            }
            throw e;
        }
    }
    
    @Override
    public void updateTutor(Tutor tutor) throws SQLException, ClassNotFoundException{
        String sql = "UPDATE tutors SET first_name=?, last_name=?, department=? WHERE tutor_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tutor.getFirstName());
            pstmt.setString(2, tutor.getLastName());
            pstmt.setString(3, tutor.getDepartment());
            pstmt.setString(4, tutor.getTutorId());
            
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Tutor not found: " + tutor.getTutorId());
            }
        }
    }
    
    @Override
    public void deleteTutor(String tutorId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tutors WHERE tutor_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tutorId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Tutor not found: " + tutorId);
            }
        }
    }
    
    @Override
    public Optional<Tutor> getTutorById(String tutorId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tutors WHERE tutor_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tutorId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Tutor> getAllTutors() throws SQLException, ClassNotFoundException {
        List<Tutor> tutors = new ArrayList<>();
        String sql = "SELECT * FROM tutors ORDER BY tutor_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tutors.add(mapResultSet(rs));
            }
        }
        return tutors;
    }
    
    @Override
    public List<Tutor> getTutorsByDepartment(String department) throws SQLException, ClassNotFoundException {
        List<Tutor> tutors = new ArrayList<>();
        String sql = "SELECT * FROM tutors WHERE department=? ORDER BY tutor_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tutors.add(mapResultSet(rs));
            }
        }
        return tutors;
    }
    
    private Tutor mapResultSet(ResultSet rs) throws SQLException {
        return new Tutor(
            rs.getString("tutor_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("department")
        );
    }
}