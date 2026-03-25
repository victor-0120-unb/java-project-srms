package com.chuka.srms.model;

import java.time.LocalDate;
import java.util.Objects;

public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String courseId;
    private int semester;
    private int year;
    private LocalDate enrollmentDate;
    
    public Enrollment(String enrollmentId, String studentId, String courseId, 
                      int semester, int year, LocalDate enrollmentDate) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.semester = semester;
        this.year = year;
        this.enrollmentDate = enrollmentDate;
    }
    
    // Getters and Setters
    public String getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = enrollmentId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    @Override
    public String toString() {
        return String.join(",", enrollmentId, studentId, courseId, 
                          String.valueOf(semester), String.valueOf(year), 
                          enrollmentDate.toString());
    }
    
    public static Enrollment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 6) {
            return new Enrollment(
                parts[0], parts[1], parts[2], 
                Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), 
                LocalDate.parse(parts[5])
            );
        }
        throw new IllegalArgumentException("Invalid Enrollment data format: " + line);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(enrollmentId, that.enrollmentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }
}