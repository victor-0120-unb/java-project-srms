package com.chuka.srms.model;

import java.util.Objects;

public class ExaminationResult {
    private String resultId;
    private String enrollmentId;
    private int score;
    private String grade;
    private int semester;
    private int year;
    
    public ExaminationResult(String resultId, String enrollmentId, int score, 
                             String grade, int semester, int year) {
        this.resultId = resultId;
        this.enrollmentId = enrollmentId;
        this.score = score;
        this.grade = grade;
        this.semester = semester;
        this.year = year;
    }
    
    // Getters and Setters
    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }
    
    public String getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = enrollmentId; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public static String calculateGrade(int score) {
        if (score >= 70) return "A";
        else if (score >= 60) return "B";
        else if (score >= 50) return "C";
        else if (score >= 40) return "D";
        else return "F";
    }
    
    @Override
    public String toString() {
        return String.join(",", resultId, enrollmentId, String.valueOf(score), 
                          grade, String.valueOf(semester), String.valueOf(year));
    }
    
    public static ExaminationResult fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 6) {
            return new ExaminationResult(
                parts[0], parts[1], Integer.parseInt(parts[2]), 
                parts[3], Integer.parseInt(parts[4]), Integer.parseInt(parts[5])
            );
        }
        throw new IllegalArgumentException("Invalid ExaminationResult data format: " + line);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExaminationResult that = (ExaminationResult) o;
        return Objects.equals(resultId, that.resultId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(resultId);
    }
}