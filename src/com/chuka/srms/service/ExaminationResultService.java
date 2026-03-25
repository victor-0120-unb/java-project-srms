package com.chuka.srms.service;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.ExaminationResultDAO;
import com.chuka.srms.dao.database.ExaminationResultDatabaseDAO;
import com.chuka.srms.dao.file.ExaminationResultFileDAO;
import com.chuka.srms.model.ExaminationResult;
import java.util.List;

public class ExaminationResultService {
    private ExaminationResultDAO resultDAO;
    
    public ExaminationResultService() {
        if (AppConfig.isDatabaseMode()) {
            resultDAO = new ExaminationResultDatabaseDAO();
        } else {
            resultDAO = new ExaminationResultFileDAO();
        }
    }
    
    public void addResult(ExaminationResult result) throws Exception {
        validateResult(result);
        // Auto-calculate grade if not provided
        if (result.getGrade() == null || result.getGrade().isEmpty()) {
            result.setGrade(ExaminationResult.calculateGrade(result.getScore()));
        }
        resultDAO.addResult(result);
    }
    
    public void updateResult(ExaminationResult result) throws Exception {
        validateResult(result);
        if (result.getGrade() == null || result.getGrade().isEmpty()) {
            result.setGrade(ExaminationResult.calculateGrade(result.getScore()));
        }
        resultDAO.updateResult(result);
    }
    
    public void deleteResult(String resultId) throws Exception {
        resultDAO.deleteResult(resultId);
    }
    
    public ExaminationResult getResultById(String resultId) throws Exception {
        return resultDAO.getResultById(resultId)
            .orElseThrow(() -> new IllegalArgumentException("Result not found"));
    }
    
    public List<ExaminationResult> getAllResults() throws Exception {
        return resultDAO.getAllResults();
    }
    
    public List<ExaminationResult> getResultsByEnrollment(String enrollmentId) throws Exception {
        return resultDAO.getResultsByEnrollment(enrollmentId);
    }
    
    private void validateResult(ExaminationResult result) {
        if (result == null) throw new IllegalArgumentException("Result cannot be null");
        if (result.getResultId() == null || result.getResultId().trim().isEmpty())
            throw new IllegalArgumentException("Result ID required");
        if (result.getEnrollmentId() == null || result.getEnrollmentId().trim().isEmpty())
            throw new IllegalArgumentException("Enrollment ID required");
        if (result.getScore() < 0 || result.getScore() > 100)
            throw new IllegalArgumentException("Score must be between 0 and 100");
    }
}