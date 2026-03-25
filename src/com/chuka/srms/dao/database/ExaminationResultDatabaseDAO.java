package com.chuka.srms.dao.database;

import com.chuka.srms.dao.ExaminationResultDAO;
import com.chuka.srms.model.ExaminationResult;
import com.chuka.srms.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class ExaminationResultDatabaseDAO implements ExaminationResultDAO {
    
    @Override
    public void addResult(ExaminationResult result) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO examination_results (result_id, enrollment_id, score, " +
                    "grade, semester, year) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, result.getResultId());
            pstmt.setString(2, result.getEnrollmentId());
            pstmt.setInt(3, result.getScore());
            pstmt.setString(4, result.getGrade());
            pstmt.setInt(5, result.getSemester());
            pstmt.setInt(6, result.getYear());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Result ID already exists: " + result.getResultId(), e);
            }
            if (e.getErrorCode() == 1452) {
                throw new SQLException("Invalid enrollment ID - foreign key constraint failed", e);
            }
            throw e;
        }
    }
    
    @Override
    public void updateResult(ExaminationResult result) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE examination_results SET enrollment_id=?, score=?, grade=?, " +
                    "semester=?, year=? WHERE result_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, result.getEnrollmentId());
            pstmt.setInt(2, result.getScore());
            pstmt.setString(3, result.getGrade());
            pstmt.setInt(4, result.getSemester());
            pstmt.setInt(5, result.getYear());
            pstmt.setString(6, result.getResultId());
            
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Result not found: " + result.getResultId());
            }
        }
    }
    
    @Override
    public void deleteResult(String resultId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM examination_results WHERE result_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resultId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Result not found: " + resultId);
            }
        }
    }
    
    @Override
    public Optional<ExaminationResult> getResultById(String resultId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM examination_results WHERE result_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resultId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<ExaminationResult> getAllResults() throws SQLException, ClassNotFoundException {
        List<ExaminationResult> results = new ArrayList<>();
        String sql = "SELECT * FROM examination_results ORDER BY result_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
        }
        return results;
    }
    
    @Override
    public List<ExaminationResult> getResultsByEnrollment(String enrollmentId) throws SQLException, ClassNotFoundException {
        List<ExaminationResult> results = new ArrayList<>();
        String sql = "SELECT * FROM examination_results WHERE enrollment_id=? ORDER BY year DESC, semester DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enrollmentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
        }
        return results;
    }
    
    @Override
    public List<ExaminationResult> getResultsByStudent(String studentId) throws SQLException, ClassNotFoundException {
        List<ExaminationResult> results = new ArrayList<>();
        String sql = "SELECT er.* FROM examination_results er " +
                    "JOIN enrollments e ON er.enrollment_id = e.enrollment_id " +
                    "WHERE e.student_id = ? ORDER BY er.year DESC, er.semester DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
        }
        return results;
    }
    
    @Override
    public Optional<ExaminationResult> getResultByEnrollmentAndSemester(String enrollmentId, int semester, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM examination_results WHERE enrollment_id=? AND semester=? AND year=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enrollmentId);
            pstmt.setInt(2, semester);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        }
        return Optional.empty();
    }
    
    private ExaminationResult mapResultSet(ResultSet rs) throws SQLException {
        return new ExaminationResult(
            rs.getString("result_id"),
            rs.getString("enrollment_id"),
            rs.getInt("score"),
            rs.getString("grade"),
            rs.getInt("semester"),
            rs.getInt("year")
        );
    }
}