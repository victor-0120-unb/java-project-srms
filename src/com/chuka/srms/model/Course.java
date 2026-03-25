package com.chuka.srms.model;

import java.util.Objects;

public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private String department;
    
    public Course(String courseId, String courseName, int credits, String department) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.department = department;
    }
    
    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    @Override
    public String toString() {
        return String.join(",", courseId, courseName, String.valueOf(credits), department);
    }
    
    public static Course fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            return new Course(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
        }
        throw new IllegalArgumentException("Invalid Course data format: " + line);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}