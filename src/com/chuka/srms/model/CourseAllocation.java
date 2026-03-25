package com.chuka.srms.model;

import java.util.Objects;

public class CourseAllocation {
    private String allocationId;
    private String courseId;
    private String tutorId;
    private int semester;
    private int year;
    
    public CourseAllocation(String allocationId, String courseId, String tutorId, 
                            int semester, int year) {
        this.allocationId = allocationId;
        this.courseId = courseId;
        this.tutorId = tutorId;
        this.semester = semester;
        this.year = year;
    }
    
    // Getters and Setters
    public String getAllocationId() { return allocationId; }
    public void setAllocationId(String allocationId) { this.allocationId = allocationId; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getTutorId() { return tutorId; }
    public void setTutorId(String tutorId) { this.tutorId = tutorId; }
    
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    @Override
    public String toString() {
        return String.join(",", allocationId, courseId, tutorId, 
                          String.valueOf(semester), String.valueOf(year));
    }
    
    public static CourseAllocation fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            return new CourseAllocation(
                parts[0], parts[1], parts[2], 
                Integer.parseInt(parts[3]), Integer.parseInt(parts[4])
            );
        }
        throw new IllegalArgumentException("Invalid CourseAllocation data format: " + line);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseAllocation that = (CourseAllocation) o;
        return Objects.equals(allocationId, that.allocationId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(allocationId);
    }
}