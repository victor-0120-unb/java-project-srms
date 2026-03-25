package com.chuka.srms.dao.file;

import com.chuka.srms.config.AppConfig;
import com.chuka.srms.dao.CourseAllocationDAO;
import com.chuka.srms.model.CourseAllocation;
import com.chuka.srms.util.FileUtil;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CourseAllocationFileDAO implements CourseAllocationDAO {
    private static final String FILE_PATH;
    
    static {
        try {
            AppConfig.ensureDataDirectory();
            FILE_PATH = AppConfig.getDataFilePath("course_allocations.txt");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize course allocation data file", e);
        }
    }
    
    @Override
    public void addAllocation(CourseAllocation allocation) throws IOException {
        if (getAllocationById(allocation.getAllocationId()).isPresent()) {
            throw new IllegalArgumentException("Allocation ID already exists: " + allocation.getAllocationId());
        }
        FileUtil.writeLine(FILE_PATH, allocation.toString(), true);
    }
    
    @Override
    public void updateAllocation(CourseAllocation allocation) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        for (String line : lines) {
            CourseAllocation existing = CourseAllocation.fromString(line);
            if (existing.getAllocationId().equals(allocation.getAllocationId())) {
                updatedLines.add(allocation.toString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("Allocation not found: " + allocation.getAllocationId());
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public void deleteAllocation(String allocationId) throws IOException {
        List<String> lines = FileUtil.readAllLines(FILE_PATH);
        List<String> updatedLines = lines.stream()
            .filter(line -> !CourseAllocation.fromString(line).getAllocationId().equals(allocationId))
            .collect(Collectors.toList());
        
        if (lines.size() == updatedLines.size()) {
            throw new IllegalArgumentException("Allocation not found: " + allocationId);
        }
        FileUtil.rewriteFile(FILE_PATH, updatedLines);
    }
    
    @Override
    public Optional<CourseAllocation> getAllocationById(String allocationId) throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(CourseAllocation::fromString)
            .filter(a -> a.getAllocationId().equals(allocationId))
            .findFirst();
    }
    
    @Override
    public List<CourseAllocation> getAllAllocations() throws IOException {
        return FileUtil.readAllLines(FILE_PATH).stream()
            .map(CourseAllocation::fromString)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseAllocation> getAllocationsByCourse(String courseId) throws IOException {
        return getAllAllocations().stream()
            .filter(a -> a.getCourseId().equals(courseId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseAllocation> getAllocationsByTutor(String tutorId) throws IOException {
        return getAllAllocations().stream()
            .filter(a -> a.getTutorId().equals(tutorId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseAllocation> getAllocationsBySemester(int semester, int year) throws IOException {
        return getAllAllocations().stream()
            .filter(a -> a.getSemester() == semester && a.getYear() == year)
            .collect(Collectors.toList());
    }
}
