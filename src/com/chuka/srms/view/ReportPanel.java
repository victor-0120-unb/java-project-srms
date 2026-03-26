package com.chuka.srms.view;

import com.chuka.srms.service.ReportService;
import com.chuka.srms.service.StudentService;
import com.chuka.srms.service.CourseService;
import com.chuka.srms.service.TutorService;
import javax.swing.*;
import java.awt.*;


public class ReportPanel extends JPanel {
    
    private ReportService reportService;
    private StudentService studentService;
    private CourseService courseService;
    private TutorService tutorService;
    
    private JTextArea reportArea;
    private JComboBox<String> cmbStudentId;
    private JComboBox<String> cmbCourseId;
    private JComboBox<String> cmbTutorId;
    private JComboBox<String> cmbSemester;
    private JComboBox<String> cmbYear;
    private JButton btnGenerateTranscript;
    private JButton btnGenerateCourseReport;
    private JButton btnGenerateTutorReport;
    private JButton btnGenerateGraduationReport;
    private JButton btnGenerateProbationReport;
    private JButton btnClear;
    private JButton btnCopy;
    private JButton refreshButton;
    
    public ReportPanel() {
        reportService = new ReportService();
        studentService = new StudentService();
        courseService = new CourseService();
        tutorService = new TutorService();
        initializeUI();
        loadComboBoxes();
    }

    private void refreshReports() {
        loadComboBoxes();
    }

    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(createControlPanel(), BorderLayout.NORTH);
        add(createReportPanel(), BorderLayout.CENTER);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generate Reports"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create components
        cmbStudentId = new JComboBox<>();
        cmbCourseId = new JComboBox<>();
        cmbTutorId = new JComboBox<>();
        cmbSemester = new JComboBox<>(new String[]{"1", "2"});
        cmbYear = new JComboBox<>();
        
        // Populate years (2020-2030)
        for (int i = 2020; i <= 2030; i++) {
            cmbYear.addItem(String.valueOf(i));
        }
        
        btnGenerateTranscript = new JButton("Generate Transcript");
        btnGenerateCourseReport = new JButton("Course Enrollment Report");
        btnGenerateTutorReport = new JButton("Tutor Course Load Report");
        btnGenerateGraduationReport = new JButton("Graduation Eligibility Report");
        btnGenerateProbationReport = new JButton("Students on Probation Report");
        btnClear = new JButton("Clear");
        btnCopy = new JButton("Copy to Clipboard");
        refreshButton = new JButton("Refresh");

        // Style buttons
        styleButton(btnGenerateTranscript, new Color(0, 100, 200));
        styleButton(btnGenerateCourseReport, new Color(0, 100, 200));
        styleButton(btnGenerateTutorReport, new Color(0, 100, 200));
        styleButton(btnGenerateGraduationReport, new Color(0, 150, 0));
        styleButton(btnGenerateProbationReport, new Color(255, 165, 0));
        styleButton(btnClear, new Color(100, 100, 100));
        styleButton(btnCopy, new Color(100, 100, 100));
        
        // Add action listeners
        btnGenerateTranscript.addActionListener(e -> generateTranscript());
        btnGenerateCourseReport.addActionListener(e -> generateCourseReport());
        btnGenerateTutorReport.addActionListener(e -> generateTutorReport());
        btnGenerateGraduationReport.addActionListener(e -> generateGraduationReport());
        btnGenerateProbationReport.addActionListener(e -> generateProbationReport());
        btnClear.addActionListener(e -> reportArea.setText(""));
        btnCopy.addActionListener(e -> copyToClipboard());
        refreshButton.addActionListener(e -> refreshReports());
        
        // Layout
        // Row 0: Student Transcript
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student Transcript:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbStudentId, gbc);
        gbc.gridx = 2;
        panel.add(btnGenerateTranscript, gbc);
        
        // Row 1: Course Enrollment Report
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Course Enrollment Report:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbCourseId, gbc);
        gbc.gridx = 2;
        JPanel semesterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        semesterPanel.add(new JLabel("Semester:"));
        semesterPanel.add(cmbSemester);
        semesterPanel.add(new JLabel("Year:"));
        semesterPanel.add(cmbYear);
        gbc.gridx = 3;
        panel.add(semesterPanel, gbc);
        gbc.gridx = 4;
        panel.add(btnGenerateCourseReport, gbc);
        
        // Row 2: Tutor Course Load Report
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Tutor Course Load Report:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbTutorId, gbc);
        gbc.gridx = 2;
        JPanel semesterPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        semesterPanel2.add(new JLabel("Semester:"));
        semesterPanel2.add(cmbSemester);
        semesterPanel2.add(new JLabel("Year:"));
        semesterPanel2.add(cmbYear);
        gbc.gridx = 3;
        panel.add(semesterPanel2, gbc);
        gbc.gridx = 4;
        panel.add(btnGenerateTutorReport, gbc);
        
        // Row 3: Graduation Report
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Graduation Eligibility:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(btnGenerateGraduationReport, gbc);
        gbc.gridwidth = 1;
        
        // Row 4: Probation Report
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Students on Probation:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(btnGenerateProbationReport, gbc);
        gbc.gridwidth = 1;
        
        // Row 5: Utility buttons
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(btnClear, gbc);
        gbc.gridx = 1;
        panel.add(btnCopy, gbc);

        return panel;
    }
    
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Report Output"));
        
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 12));
        button.setFocusPainted(false);
    }
    
    private void loadComboBoxes() {
        try {
            // Load students
            cmbStudentId.removeAllItems();
            var students = studentService.getAllStudents();
            for (var s : students) {
                cmbStudentId.addItem(s.getStudentId() + " - " + s.getFirstName() + " " + s.getLastName());
            }
            
            // Load courses
            cmbCourseId.removeAllItems();
            var courses = courseService.getAllCourses();
            for (var c : courses) {
                cmbCourseId.addItem(c.getCourseId() + " - " + c.getCourseName());
            }
            
            // Load tutors
            cmbTutorId.removeAllItems();
            var tutors = tutorService.getAllTutors();
            for (var t : tutors) {
                cmbTutorId.addItem(t.getTutorId() + " - " + t.getFirstName() + " " + t.getLastName());
            }
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }
    
    private void generateTranscript() {
        String selected = (String) cmbStudentId.getSelectedItem();
        if (selected == null) {
            showError("Please select a student");
            return;
        }
        
        String studentId = selected.split(" - ")[0];
        
        try {
            String transcript = reportService.generateStudentTranscript(studentId);
            reportArea.setText(transcript);
        } catch (Exception e) {
            showError("Error generating transcript: " + e.getMessage());
        }
    }
    
    private void generateCourseReport() {
        String selected = (String) cmbCourseId.getSelectedItem();
        if (selected == null) {
            showError("Please select a course");
            return;
        }
        
        String courseId = selected.split(" - ")[0];
        int semester = Integer.parseInt((String) cmbSemester.getSelectedItem());
        int year = Integer.parseInt((String) cmbYear.getSelectedItem());
        
        try {
            String report = reportService.generateCourseEnrollmentReport(courseId, semester, year);
            reportArea.setText(report);
        } catch (Exception e) {
            showError("Error generating report: " + e.getMessage());
        }
    }
    
    private void generateTutorReport() {
        String selected = (String) cmbTutorId.getSelectedItem();
        if (selected == null) {
            showError("Please select a tutor");
            return;
        }
        
        String tutorId = selected.split(" - ")[0];
        int semester = Integer.parseInt((String) cmbSemester.getSelectedItem());
        int year = Integer.parseInt((String) cmbYear.getSelectedItem());
        
        try {
            String report = reportService.generateTutorCourseLoadReport(tutorId, semester, year);
            reportArea.setText(report);
        } catch (Exception e) {
            showError("Error generating report: " + e.getMessage());
        }
    }
    
    private void generateGraduationReport() {
        try {
            String report = reportService.generateGraduationEligibilityReport();
            reportArea.setText(report);
        } catch (Exception e) {
            showError("Error generating graduation report: " + e.getMessage());
        }
    }
    
    private void generateProbationReport() {
        try {
            String report = reportService.generateStudentsOnProbationReport();
            reportArea.setText(report);
        } catch (Exception e) {
            showError("Error generating probation report: " + e.getMessage());
        }
    }
    
    private void copyToClipboard() {
        String text = reportArea.getText();
        if (text.isEmpty()) {
            showError("Nothing to copy");
            return;
        }
        
        java.awt.datatransfer.StringSelection selection = 
            new java.awt.datatransfer.StringSelection(text);
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        
        showSuccess("Report copied to clipboard!");
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}