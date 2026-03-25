package com.chuka.srms.service;

import com.chuka.srms.model.Student;
import com.chuka.srms.dao.StudentDAO;
import com.chuka.srms.dao.database.StudentDatabaseDAO;
import com.chuka.srms.dao.file.StudentFileDAO;
import com.chuka.srms.config.AppConfig;

/**
 * Service class for academic progression business logic
 * Handles promotion and graduation clearance
 */
public class AcademicProgressionService {
    
    private StudentService studentService;
    private EnrollmentService enrollmentService;  // To be created
    private ExaminationResultService resultService;  // To be created
    private StudentDAO studentDAO;
    
    // Minimum courses required per semester
    private static final int MIN_COURSES_PER_SEMESTER = 10;
    
    // Minimum credits required for graduation (for 4-year degree)
    private static final int MIN_CREDITS_FOR_GRADUATION = 120;
    
    // Passing grade threshold (score >= 40)
    private static final int PASSING_SCORE = 40;
    
    public AcademicProgressionService() {
        studentService = new StudentService();
        
        // Initialize DAO based on configuration
        if (AppConfig.getPersistenceMode().equals("database")) {
            studentDAO = new StudentDatabaseDAO();
        } else {
            studentDAO = new StudentFileDAO();
        }
        
        // These will be initialized when we create these services
        enrollmentService = null; // Will be set later
        resultService = null; // Will be set later
    }
    
    /**
     * Promote a student to the next class/year
     * Rules: Student must pass at least 10 courses in the current semester
     * 
     * @param studentId The ID of the student to promote
     * @return true if promoted successfully, false if requirements not met
     * @throws Exception if database/file access fails
     */
    public boolean promoteStudent(String studentId) throws Exception {
        // Get the student
        Student student = studentService.getStudentById(studentId);
        
        // Check if student is already graduated
        if (student.getAcademicStatus().equals("Graduated")) {
            throw new IllegalArgumentException("Cannot promote a graduated student");
        }
        
        // Get current semester and year
        int currentSemester = student.getCurrentSemester();
        int currentYear = student.getCurrentYear();
        
        // Count passed courses in current semester
        int passedCourses = studentService.countPassedCourses(studentId, currentSemester, currentYear);
        
        // Check if student passed minimum required courses
        if (passedCourses < MIN_COURSES_PER_SEMESTER) {
            System.out.println("Student " + studentId + " passed only " + passedCourses + 
                               " courses. Minimum required: " + MIN_COURSES_PER_SEMESTER);
            return false;
        }
        
        // Update student's academic progression
        if (currentSemester == 2) {
            // End of second semester - move to next year
            student.setCurrentYear(currentYear + 1);
            student.setCurrentSemester(1);
            System.out.println("Student " + studentId + " promoted to Year " + (currentYear + 1));
        } else {
            // Move to next semester
            student.setCurrentSemester(currentSemester + 1);
            System.out.println("Student " + studentId + " promoted to Semester " + (currentSemester + 1));
        }
        
        // Update academic status if on probation
        if (student.getAcademicStatus().equals("On Probation")) {
            student.setAcademicStatus("Active");
            System.out.println("Student " + studentId + " removed from probation");
        }
        
        // Save updated student
        studentService.updateStudent(student);
        
        return true;
    }
    
    /**
     * Clear a student for graduation
     * Rules: Student must have completed required credits for their major
     * 
     * @param studentId The ID of the student to clear
     * @return true if cleared for graduation, false if requirements not met
     * @throws Exception if database/file access fails
     */
    public boolean clearForGraduation(String studentId) throws Exception {
        // Get the student
        Student student = studentService.getStudentById(studentId);
        
        // Check if student is already graduated
        if (student.getAcademicStatus().equals("Graduated")) {
            throw new IllegalArgumentException("Student is already graduated");
        }
        
        // Check if student is in final year (Year 4)
        if (student.getCurrentYear() < 4) {
            System.out.println("Student " + studentId + " is only in Year " + 
                               student.getCurrentYear() + ". Must be in Year 4 to graduate.");
            return false;
        }
        
        // Check if student has met graduation requirements
        boolean metRequirements = studentService.hasMetGraduationRequirements(studentId);
        
        if (metRequirements) {
            // Update student status
            student.setAcademicStatus("Cleared for Graduation");
            studentService.updateStudent(student);
            System.out.println("Student " + studentId + " cleared for graduation!");
            return true;
        } else {
            System.out.println("Student " + studentId + " does not meet graduation requirements");
            return false;
        }
    }
    
    /**
     * Graduate a student (mark as graduated)
     * Called after graduation ceremony
     * 
     * @param studentId The ID of the student to graduate
     * @throws Exception if database/file access fails
     */
    public void graduateStudent(String studentId) throws Exception {
        Student student = studentService.getStudentById(studentId);
        
        if (!student.getAcademicStatus().equals("Cleared for Graduation")) {
            throw new IllegalArgumentException("Student is not cleared for graduation");
        }
        
        student.setAcademicStatus("Graduated");
        studentService.updateStudent(student);
        System.out.println("Student " + studentId + " has graduated!");
    }
    
    /**
     * Check if a student can register for a course
     * Rules: 
     * - Student must not have already passed the course
     * - Student must not have reached maximum course load (optional)
     * 
     * @param studentId The student ID
     * @param courseId The course ID to check
     * @return true if eligible to register
     * @throws Exception if database/file access fails
     */
    public boolean canRegisterForCourse(String studentId, String courseId) throws Exception {
        // This will need EnrollmentService and ExaminationResultService
        // For now, return true (will be implemented later)
        
        // TODO: Check if student already passed this course
        // TODO: Check if student is already enrolled
        // TODO: Check course prerequisites
        
        return true;
    }
    
    /**
     * Check if student meets minimum course load requirements
     * Students must register for at least 10 courses per semester
     * 
     * @param studentId The student ID
     * @param semester The semester
     * @param year The academic year
     * @param enrolledCourses Number of courses enrolled
     * @return true if meets minimum requirement
     */
    public boolean meetsMinimumCourseLoad(String studentId, int semester, int year, int enrolledCourses) {
        if (enrolledCourses < MIN_COURSES_PER_SEMESTER) {
            System.out.println("Student " + studentId + " enrolled in only " + enrolledCourses +
                               " courses. Minimum required: " + MIN_COURSES_PER_SEMESTER);
            return false;
        }
        return true;
    }
    
    /**
     * Calculate GPA for a student in a given semester
     * 
     * @param studentId The student ID
     * @param semester The semester
     * @param year The academic year
     * @return The GPA (0.0 - 4.0)
     * @throws Exception if database/file access fails
     */
    public double calculateGPA(String studentId, int semester, int year) throws Exception {
        // This will need ExaminationResultService
        // For now, return a placeholder
        // TODO: Implement GPA calculation based on grades
        
        return 0.0;
    }
    
    /**
     * Get grade points for a letter grade
     * 
     * @param grade The letter grade (A, A-, B+, etc.)
     * @return Grade points (0-4)
     */
    public double getGradePoints(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "E": return 0.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
    
    /**
     * Get letter grade based on score
     * 
     * @param score The numerical score (0-100)
     * @return The letter grade
     */
    public String calculateGrade(int score) {
        if (score >= 70) return "A";
        else if (score >= 60) return "B";
        else if (score >= 50) return "C";
        else if (score >= 40) return "D";
        else return "F";
    }
    
    /**
     * Check if a grade is passing
     * 
     * @param grade The letter grade
     * @return true if passing
     */
    public boolean isPassingGrade(String grade) {
        return !grade.equals("F") && !grade.equals("E");
    }
    
    /**
     * Get academic standing based on GPA
     * 
     * @param gpa The GPA
     * @return Academic standing (Good Standing, Probation, etc.)
     */
    public String getAcademicStanding(double gpa) {
        if (gpa >= 3.5) return "Honors";
        else if (gpa >= 2.0) return "Good Standing";
        else if (gpa >= 1.5) return "Academic Warning";
        else return "Academic Probation";
    }
    
    // ========== Helper Methods ==========
    
    /**
     * Check if student is eligible for promotion
     * 
     * @param studentId The student ID
     * @return true if eligible
     * @throws Exception if database/file access fails
     */
    public boolean isEligibleForPromotion(String studentId) throws Exception {
        Student student = studentService.getStudentById(studentId);
        int currentSemester = student.getCurrentSemester();
        int currentYear = student.getCurrentYear();
        
        int passedCourses = studentService.countPassedCourses(studentId, currentSemester, currentYear);
        
        return passedCourses >= MIN_COURSES_PER_SEMESTER;
    }
    
    /**
     * Get promotion message explaining why a student can't be promoted
     * 
     * @param studentId The student ID
     * @return Explanation message
     * @throws Exception if database/file access fails
     */
    public String getPromotionMessage(String studentId) throws Exception {
        Student student = studentService.getStudentById(studentId);
        int currentSemester = student.getCurrentSemester();
        int currentYear = student.getCurrentYear();
        
        int passedCourses = studentService.countPassedCourses(studentId, currentSemester, currentYear);
        
        if (passedCourses >= MIN_COURSES_PER_SEMESTER) {
            return "Student is eligible for promotion!";
        } else {
            return "Student passed " + passedCourses + " out of " + MIN_COURSES_PER_SEMESTER +
                   " required courses. Need " + (MIN_COURSES_PER_SEMESTER - passedCourses) + 
                   " more passed courses to be promoted.";
        }
    }
    
    /**
     * Get graduation eligibility message
     * 
     * @param studentId The student ID
     * @return Explanation message
     * @throws Exception if database/file access fails
     */
    public String getGraduationMessage(String studentId) throws Exception {
        Student student = studentService.getStudentById(studentId);
        
        if (student.getCurrentYear() < 4) {
            return "Student is in Year " + student.getCurrentYear() + 
                   ". Must be in Year 4 to graduate.";
        }
        
        boolean metRequirements = studentService.hasMetGraduationRequirements(studentId);
        
        if (metRequirements) {
            return "Student has met all graduation requirements! Ready for clearance.";
        } else {
            return "Student has not met all graduation requirements. Complete all required courses and credits.";
        }
    }

    public boolean meetsCourseLoadRequirement(String studentId, int semester, int year, int enrolledCount) {
    int requiredCourses = (year < 4) ? 10 : 8; // example rule
    return enrolledCount >= requiredCourses;
}

}