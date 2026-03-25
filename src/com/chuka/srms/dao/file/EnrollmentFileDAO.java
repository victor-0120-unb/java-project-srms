package com.chuka.srms.dao.file;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.EnrollmentDAO;
import com.chuka.srms.model.Enrollment;
import com.chuka.srms.util.FileUtil;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentFileDAO implements EnrollmentDAO {
    private static final String FILE_PATH;
    
    static {
        try {
            AppConfig.ensureDataDirectory();
            FILE_PATH = AppConfig.getDataFilePath("enrollments.txt");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize enrollment data file", e);
        }
    }
    
    @Override
    public void addEnrollment(Enrollment enrollment) throws IOException {
        // Check if enrollment already exists
        if (getEnrollmentById(enrollment.getEnrollmentId()).isPresent()) {
            throw new IllegalArgumentException("Enrollment ID already exists: " + enrollment.getEnrollmentId());
        }
        FileUtil.writeLine(FILE_PATH, enrollment.toString(), true);
    }
    
    @Override
    public void updateEnrollment(Enrollment enrollment) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            Enrollment existing = Enrollment.fromString(line);
            if (existing.getEnrollmentId().equals(enrollment.getEnrollmentId())) {
                updatedLines.add(enrollment.toString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("Enrollment not found: " + enrollment.getEnrollmentId());
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public void deleteEnrollment(String enrollmentId) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = lines.stream()
            .filter(line -> !Enrollment.fromString(line).getEnrollmentId().equals(enrollmentId))
            .collect(Collectors.toList());
        
        if (lines.size() == updatedLines.size()) {
            throw new IllegalArgumentException("Enrollment not found: " + enrollmentId);
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public Optional<Enrollment> getEnrollmentById(String enrollmentId) throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(Enrollment::fromString)
            .filter(e -> e.getEnrollmentId().equals(enrollmentId))
            .findFirst();
    }
    
    @Override
    public List<Enrollment> getAllEnrollments() throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(Enrollment::fromString)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Enrollment> getEnrollmentsByStudent(String studentId) throws IOException {
        return getAllEnrollments().stream()
            .filter(e -> e.getStudentId().equals(studentId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Enrollment> getEnrollmentsByCourse(String courseId) throws IOException {
        return getAllEnrollments().stream()
            .filter(e -> e.getCourseId().equals(courseId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Enrollment> getEnrollmentsBySemester(int semester, int year) throws IOException {
        return getAllEnrollments().stream()
            .filter(e -> e.getSemester() == semester && e.getYear() == year)
            .collect(Collectors.toList());
    }
    
    @Override
    public int countEnrollmentsByStudent(String studentId, int semester, int year) throws IOException {
        return (int) getAllEnrollments().stream()
            .filter(e -> e.getStudentId().equals(studentId) && 
                        e.getSemester() == semester && 
                        e.getYear() == year)
            .count();
    }
    
    @Override
    public boolean isStudentEnrolledInCourse(String studentId, String courseId, int semester, int year) throws IOException {
        return getAllEnrollments().stream()
            .anyMatch(e -> e.getStudentId().equals(studentId) && 
                          e.getCourseId().equals(courseId) && 
                          e.getSemester() == semester && 
                          e.getYear() == year);
    }



    
}
