package com.chuka.srms.dao.file;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.StudentDAO;
import com.chuka.srms.model.Student;
import com.chuka.srms.util.FileUtil;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StudentFileDAO implements StudentDAO {
    private static final String FILE_PATH;
    
    static {
        try {
            AppConfig.ensureDataDirectory();
            FILE_PATH = AppConfig.getDataFilePath("students.txt");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize student data file", e);
        }
    }
    
    @Override
    public void addStudent(Student student) throws IOException {
        FileUtil.writeLine(FILE_PATH, student.toString(), true);
    }
    
    @Override
    public void updateStudent(Student student) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            Student existing = Student.fromString(line);
            if (existing.getStudentId().equals(student.getStudentId())) {
                updatedLines.add(student.toString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("Student not found: " + student.getStudentId());
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public void deleteStudent(String studentId) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = lines.stream()
            .filter(line -> !Student.fromString(line).getStudentId().equals(studentId))
            .collect(Collectors.toList());
        
        if (lines.size() == updatedLines.size()) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public Optional<Student> getStudentById(String studentId) throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(Student::fromString)
            .filter(s -> s.getStudentId().equals(studentId))
            .findFirst();
    }
    
    @Override
    public List<Student> getAllStudents() throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(Student::fromString)
            .collect(Collectors.toList());
    }
    
    @Override
    public int countPassedCourses(String studentId, int semester, int year) throws Exception {
        // This requires reading from enrollments and results files
        // For now, return a placeholder - implement fully later
        return 0;
    }
    
    @Override
    public boolean hasMetGraduationRequirements(String studentId) throws Exception {
        // This requires reading from multiple files - implement later
        return false;
    }
}