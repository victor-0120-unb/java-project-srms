package com.chuka.srms.service;

import com.chuka.srms.model.*;
import java.util.List;

public class ReportService {
    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private ExaminationResultService resultService;
    private TutorService tutorService;
    private CourseAllocationService allocationService;
    
    public ReportService() {
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
        this.resultService = new ExaminationResultService();
        this.tutorService = new TutorService();
        this.allocationService = new CourseAllocationService();
    }
    
    public String generateStudentTranscript(String studentId) throws Exception {
        Student student = studentService.getStudentById(studentId);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        
        StringBuilder transcript = new StringBuilder();
        transcript.append("=".repeat(60)).append("\n");
        transcript.append("CHUKA UNIVERSITY - STUDENT ACADEMIC TRANSCRIPT\n");
        transcript.append("=".repeat(60)).append("\n\n");
        transcript.append("Student ID: ").append(student.getStudentId()).append("\n");
        transcript.append("Name: ").append(student.getFirstName()).append(" ").append(student.getLastName()).append("\n");
        transcript.append("Major: ").append(student.getMajor()).append("\n");
        transcript.append("Academic Status: ").append(student.getAcademicStatus()).append("\n\n");
        transcript.append("-".repeat(60)).append("\n");
        transcript.append(String.format("%-15s %-30s %-8s %-5s\n", "Course ID", "Course Name", "Score", "Grade"));
        transcript.append("-".repeat(60)).append("\n");
        
        for (Enrollment e : enrollments) {
            Course course = courseService.getCourseById(e.getCourseId());
            List<ExaminationResult> results = resultService.getResultsByEnrollment(e.getEnrollmentId());
            if (!results.isEmpty()) {
                ExaminationResult r = results.get(0);
                transcript.append(String.format("%-15s %-30s %-8d %-5s\n", 
                    course.getCourseId(), 
                    course.getCourseName().length() > 28 ? course.getCourseName().substring(0, 28) : course.getCourseName(),
                    r.getScore(), r.getGrade()));
            }
        }
        
        transcript.append("-".repeat(60)).append("\n");
        return transcript.toString();
    }
    
    public String generateCourseEnrollmentReport(String courseId, int semester, int year) throws Exception {
        Course course = courseService.getCourseById(courseId);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append("COURSE ENROLLMENT REPORT\n");
        report.append("=".repeat(60)).append("\n\n");
        report.append("Course: ").append(course.getCourseId()).append(" - ").append(course.getCourseName()).append("\n");
        report.append("Semester: ").append(semester).append(", Year: ").append(year).append("\n\n");
        report.append("-".repeat(60)).append("\n");
        report.append(String.format("%-20s %-20s\n", "Student ID", "Student Name"));
        report.append("-".repeat(60)).append("\n");
        
        for (Enrollment e : enrollments) {
            if (e.getSemester() == semester && e.getYear() == year) {
                Student s = studentService.getStudentById(e.getStudentId());
                report.append(String.format("%-20s %-20s\n", s.getStudentId(), s.getFirstName() + " " + s.getLastName()));
            }
        }
        
        report.append("-".repeat(60)).append("\n");
        report.append("Total Enrolled: ").append(enrollments.stream()
            .filter(e -> e.getSemester() == semester && e.getYear() == year).count()).append("\n");
        return report.toString();
    }
    
    public String generateTutorCourseLoadReport(String tutorId, int semester, int year) throws Exception {
        Tutor tutor = tutorService.getTutorById(tutorId);
        List<CourseAllocation> allocations = allocationService.getAllocationsByTutor(tutorId);
        
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append("TUTOR COURSE LOAD REPORT\n");
        report.append("=".repeat(60)).append("\n\n");
        report.append("Tutor: ").append(tutor.getTutorId()).append(" - ").append(tutor.getFirstName()).append(" ").append(tutor.getLastName()).append("\n");
        report.append("Department: ").append(tutor.getDepartment()).append("\n");
        report.append("Semester: ").append(semester).append(", Year: ").append(year).append("\n\n");
        report.append("-".repeat(60)).append("\n");
        report.append(String.format("%-15s %-40s %-8s\n", "Course ID", "Course Name", "Credits"));
        report.append("-".repeat(60)).append("\n");
        
        int totalCredits = 0;
        for (CourseAllocation a : allocations) {
            if (a.getSemester() == semester && a.getYear() == year) {
                Course c = courseService.getCourseById(a.getCourseId());
                report.append(String.format("%-15s %-40s %-8d\n", 
                    c.getCourseId(), 
                    c.getCourseName().length() > 38 ? c.getCourseName().substring(0, 38) : c.getCourseName(),
                    c.getCredits()));
                totalCredits += c.getCredits();
            }
        }
        
        report.append("-".repeat(60)).append("\n");
        report.append("Total Teaching Load: ").append(totalCredits).append(" credits\n");
        return report.toString();
    }
    
    public String generateGraduationEligibilityReport() throws Exception {
        List<Student> students = studentService.getAllStudents();
        
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append("GRADUATION ELIGIBILITY REPORT\n");
        report.append("=".repeat(60)).append("\n\n");
        report.append(String.format("%-20s %-20s %-15s\n", "Student ID", "Student Name", "Status"));
        report.append("-".repeat(60)).append("\n");
        
        for (Student s : students) {
            boolean eligible = studentService.hasMetGraduationRequirements(s.getStudentId());
            if (eligible && s.getCurrentYear() >= 4) {
                report.append(String.format("%-20s %-20s %-15s\n", 
                    s.getStudentId(), 
                    s.getFirstName() + " " + s.getLastName(),
                    "ELIGIBLE"));
            }
        }
        
        report.append("-".repeat(60)).append("\n");
        return report.toString();
    }
    
    public String generateStudentsOnProbationReport() throws Exception {
        List<Student> students = studentService.getAllStudents();
        
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append("STUDENTS ON PROBATION REPORT\n");
        report.append("=".repeat(60)).append("\n\n");
        report.append(String.format("%-20s %-20s %-15s\n", "Student ID", "Student Name", "Status"));
        report.append("-".repeat(60)).append("\n");
        
        for (Student s : students) {
            if (s.getAcademicStatus().equals("On Probation")) {
                report.append(String.format("%-20s %-20s %-15s\n", 
                    s.getStudentId(), 
                    s.getFirstName() + " " + s.getLastName(),
                    s.getAcademicStatus()));
            }
        }
        
        report.append("-".repeat(60)).append("\n");
        return report.toString();
    }
}
