package com.chuka.srms.dao.file;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.CourseDAO;
import com.chuka.srms.model.Course;
import com.chuka.srms.util.FileUtil;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CourseFileDAO implements CourseDAO {
    private static final String FILE_PATH;
    
    static {
        try {
            AppConfig.ensureDataDirectory();
            FILE_PATH = AppConfig.getDataFilePath("courses.txt");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize course data file", e);
        }
    }
    
    @Override
    public void addCourse(Course course) throws IOException {
        if (getCourseById(course.getCourseId()).isPresent()) {
            throw new IllegalArgumentException("Course ID already exists: " + course.getCourseId());
        }
        FileUtil.writeLine(FILE_PATH, course.toString(), true);
    }
    
    @Override
    public void updateCourse(Course course) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            Course existing = Course.fromString(line);
            if (existing.getCourseId().equals(course.getCourseId())) {
                updatedLines.add(course.toString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("Course not found: " + course.getCourseId());
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public void deleteCourse(String courseId) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = lines.stream()
            .filter(line -> !Course.fromString(line).getCourseId().equals(courseId))
            .collect(Collectors.toList());
        
        if (lines.size() == updatedLines.size()) {
            throw new IllegalArgumentException("Course not found: " + courseId);
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public Optional<Course> getCourseById(String courseId) throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(Course::fromString)
            .filter(c -> c.getCourseId().equals(courseId))
            .findFirst();
    }
    
    @Override
    public List<Course> getAllCourses() throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(Course::fromString)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> getCoursesByDepartment(String department) throws IOException {
        return getAllCourses().stream()
            .filter(c -> c.getDepartment().equals(department))
            .collect(Collectors.toList());
    }
}