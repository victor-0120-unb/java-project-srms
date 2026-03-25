package com.chuka.srms.dao.file;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.ExaminationResultDAO;
import com.chuka.srms.model.ExaminationResult;
import com.chuka.srms.util.FileUtil;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ExaminationResultFileDAO implements ExaminationResultDAO {
    private static final String FILE_PATH;
    
    static {
        try {
            AppConfig.ensureDataDirectory();
            FILE_PATH = AppConfig.getDataFilePath("examination_results.txt");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize examination results data file", e);
        }
    }
    
    @Override
    public void addResult(ExaminationResult result) throws IOException {
        if (getResultById(result.getResultId()).isPresent()) {
            throw new IllegalArgumentException("Result ID already exists: " + result.getResultId());
        }
        FileUtil.writeLine(FILE_PATH, result.toString(), true);
    }
    
    @Override
    public void updateResult(ExaminationResult result) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            ExaminationResult existing = ExaminationResult.fromString(line);
            if (existing.getResultId().equals(result.getResultId())) {
                updatedLines.add(result.toString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("Result not found: " + result.getResultId());
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public void deleteResult(String resultId) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = lines.stream()
            .filter(line -> !ExaminationResult.fromString(line).getResultId().equals(resultId))
            .collect(Collectors.toList());
        
        if (lines.size() == updatedLines.size()) {
            throw new IllegalArgumentException("Result not found: " + resultId);
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public Optional<ExaminationResult> getResultById(String resultId) throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(ExaminationResult::fromString)
            .filter(r -> r.getResultId().equals(resultId))
            .findFirst();
    }
    
    @Override
    public List<ExaminationResult> getAllResults() throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(ExaminationResult::fromString)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ExaminationResult> getResultsByEnrollment(String enrollmentId) throws IOException {
        return getAllResults().stream()
            .filter(r -> r.getEnrollmentId().equals(enrollmentId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ExaminationResult> getResultsByStudent(String studentId) throws IOException {
        // This requires reading enrollments file to map studentId to enrollmentId
        String enrollmentPath = AppConfig.getDataFilePath("enrollments.txt");
        List<String> enrollmentLines = FileUtil.readAllLines(enrollmentPath);
        
        // Get all enrollment IDs for this student
        Set<String> enrollmentIds = enrollmentLines.stream()
            .map(line -> line.split(","))
            .filter(parts -> parts.length >= 2 && parts[1].equals(studentId))
            .map(parts -> parts[0])
            .collect(Collectors.toSet());
        
        // Get results for those enrollment IDs
        return getAllResults().stream()
            .filter(r -> enrollmentIds.contains(r.getEnrollmentId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<ExaminationResult> getResultByEnrollmentAndSemester(String enrollmentId, int semester, int year) throws IOException {
        return getAllResults().stream()
            .filter(r -> r.getEnrollmentId().equals(enrollmentId) && 
                        r.getSemester() == semester && 
                        r.getYear() == year)
            .findFirst();
    }
}