package com.chuka.srms.view;

import com.chuka.srms.model.ExaminationResult;
import com.chuka.srms.model.Enrollment;
import com.chuka.srms.model.Student;
import com.chuka.srms.model.Course;
import com.chuka.srms.service.ExaminationResultService;
import com.chuka.srms.service.EnrollmentService;
import com.chuka.srms.service.StudentService;
import com.chuka.srms.service.CourseService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ExaminationResultPanel extends JPanel {
    
    // Form Fields
    private JTextField txtResultId;
    private JComboBox<String> cmbEnrollment;
    private JTextField txtScore;
    private JTextField txtGrade;
    private JTextField txtSemester;
    private JTextField txtYear;
    
    // Display fields for enrollment info
    private JLabel lblStudentInfo;
    private JLabel lblCourseInfo;
    
    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnViewAll;
    private JButton btnClearForm;
    private JButton btnCalculateGrade;
    private JButton refreshButton;
    
    // Table
    private JTable resultTable;
    private DefaultTableModel tableModel;
    
    // Services
    private ExaminationResultService resultService;
    private EnrollmentService enrollmentService;
    private StudentService studentService;
    private CourseService courseService;
    
    public ExaminationResultPanel() {
        resultService = new ExaminationResultService();
        enrollmentService = new EnrollmentService();
        studentService = new StudentService();
        courseService = new CourseService();
        initializeUI();
        loadResults();
        loadEnrollments();
    }

    private void refreshResults() {
        loadResults();
    }

    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(createFormPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        add(createTablePanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Examination Result"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtResultId = new JTextField(15);
        cmbEnrollment = new JComboBox<>();
        txtScore = new JTextField(10);
        txtGrade = new JTextField(5);
        txtSemester = new JTextField(5);
        txtYear = new JTextField(5);
        
        lblStudentInfo = new JLabel("Select enrollment to see details");
        lblCourseInfo = new JLabel("");
        lblStudentInfo.setForeground(Color.BLUE);
        lblCourseInfo.setForeground(new Color(0, 100, 0));
        
        // Row 0: Result ID and Enrollment
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Result ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtResultId, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Enrollment:"), gbc);
        gbc.gridx = 3;
        panel.add(cmbEnrollment, gbc);
        
        // Row 1: Student and Course Info
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(lblStudentInfo, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(lblCourseInfo, gbc);
        gbc.gridwidth = 1;
        
        // Row 2: Score and Grade
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Score (0-100):"), gbc);
        gbc.gridx = 1;
        panel.add(txtScore, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Grade:"), gbc);
        gbc.gridx = 3;
        panel.add(txtGrade, gbc);
        
        // Row 3: Semester and Year
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSemester, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 3;
        panel.add(txtYear, gbc);

        
        
        // Add listener to update info when enrollment changes
        cmbEnrollment.addActionListener(e -> updateEnrollmentInfo());
        
        return panel;
    }
    
    private void updateEnrollmentInfo() {
        String selected = (String) cmbEnrollment.getSelectedItem();
        if (selected != null && !selected.isEmpty()) {
            try {
                String enrollmentId = selected.split(" - ")[0];
                Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
                Student student = studentService.getStudentById(enrollment.getStudentId());
                Course course = courseService.getCourseById(enrollment.getCourseId());
                
                lblStudentInfo.setText(student.getStudentId() + " - " + student.getFirstName() + " " + student.getLastName());
                lblCourseInfo.setText(course.getCourseId() + " - " + course.getCourseName() + " (" + course.getCredits() + " credits)");
                
                txtSemester.setText(String.valueOf(enrollment.getSemester()));
                txtYear.setText(String.valueOf(enrollment.getYear()));
            } catch (Exception e) {
                lblStudentInfo.setText("Error loading info");
            }
        }
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));
        
        btnAdd = new JButton("Add Result");
        btnUpdate = new JButton("Update Result");
        btnDelete = new JButton("Delete Result");
        btnSearch = new JButton("Search");
        btnViewAll = new JButton("View All");
        btnClearForm = new JButton("Clear Form");
        btnCalculateGrade = new JButton("Calculate Grade");
        refreshButton = new JButton("Refresh");
        
        styleButton(btnAdd, new Color(0, 150, 0));
        styleButton(btnUpdate, new Color(0, 100, 200));
        styleButton(btnDelete, new Color(200, 0, 0));
        btnCalculateGrade.setBackground(new Color(255, 165, 0));
        
        btnAdd.addActionListener(e -> addResult());
        btnUpdate.addActionListener(e -> updateResult());
        btnDelete.addActionListener(e -> deleteResult());
        btnSearch.addActionListener(e -> searchResult());
        btnViewAll.addActionListener(e -> loadResults());
        btnClearForm.addActionListener(e -> clearForm());
        btnCalculateGrade.addActionListener(e -> calculateGrade());
        refreshButton.addActionListener(e -> refreshResults());
        
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnViewAll);
        panel.add(btnClearForm);
        panel.add(btnCalculateGrade);
        panel.add(refreshButton);
        
        return panel;
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 12));
        button.setFocusPainted(false);
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Examination Results"));
        
        String[] columns = {"Result ID", "Student ID", "Student Name", "Course ID", "Course Name", "Score", "Grade", "Semester", "Year"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setRowHeight(25);
        
        resultTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = resultTable.getSelectedRow();
                if (row >= 0) {
                    loadSelectedResult(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadEnrollments() {
        try {
            cmbEnrollment.removeAllItems();
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            for (Enrollment e : enrollments) {
                Student student = studentService.getStudentById(e.getStudentId());
                Course course = courseService.getCourseById(e.getCourseId());
                cmbEnrollment.addItem(e.getEnrollmentId() + " - " + 
                    student.getFirstName() + " " + student.getLastName() + " - " + 
                    course.getCourseName());
            }
        } catch (Exception e) {
            showError("Error loading enrollments: " + e.getMessage());
        }
    }
    
    private void loadResults() {
        try {
            tableModel.setRowCount(0);
            List<ExaminationResult> results = resultService.getAllResults();
            
            for (ExaminationResult r : results) {
                Enrollment enrollment = enrollmentService.getEnrollmentById(r.getEnrollmentId());
                Student student = studentService.getStudentById(enrollment.getStudentId());
                Course course = courseService.getCourseById(enrollment.getCourseId());
                
                tableModel.addRow(new Object[]{
                    r.getResultId(),
                    student.getStudentId(),
                    student.getFirstName() + " " + student.getLastName(),
                    course.getCourseId(),
                    course.getCourseName(),
                    r.getScore(),
                    r.getGrade(),
                    r.getSemester(),
                    r.getYear()
                });
            }
        } catch (Exception e) {
            showError("Error loading results: " + e.getMessage());
        }
    }
    
    private void loadSelectedResult(int row) {
        txtResultId.setText(tableModel.getValueAt(row, 0).toString());
        
        String studentId = tableModel.getValueAt(row, 1).toString();
        String courseId = tableModel.getValueAt(row, 3).toString();
        
        // Find and select matching enrollment
        for (int i = 0; i < cmbEnrollment.getItemCount(); i++) {
            String item = cmbEnrollment.getItemAt(i);
            if (item.contains(studentId) && item.contains(courseId)) {
                cmbEnrollment.setSelectedIndex(i);
                break;
            }
        }
        
        txtScore.setText(tableModel.getValueAt(row, 5).toString());
        txtGrade.setText(tableModel.getValueAt(row, 6).toString());
        txtSemester.setText(tableModel.getValueAt(row, 7).toString());
        txtYear.setText(tableModel.getValueAt(row, 8).toString());
    }
    
    private ExaminationResult getResultFromForm() throws Exception {
        String resultId = txtResultId.getText().trim();
        String enrollmentId = ((String) cmbEnrollment.getSelectedItem()).split(" - ")[0];
        String scoreStr = txtScore.getText().trim();
        String grade = txtGrade.getText().trim();
        String semesterStr = txtSemester.getText().trim();
        String yearStr = txtYear.getText().trim();
        
        if (resultId.isEmpty()) throw new IllegalArgumentException("Result ID is required");
        if (scoreStr.isEmpty()) throw new IllegalArgumentException("Score is required");
        
        int score = Integer.parseInt(scoreStr);
        if (score < 0 || score > 100) throw new IllegalArgumentException("Score must be between 0 and 100");
        
        int semester = Integer.parseInt(semesterStr);
        int year = Integer.parseInt(yearStr);
        
        return new ExaminationResult(resultId, enrollmentId, score, grade, semester, year);
    }
    
    private void calculateGrade() {
        String scoreStr = txtScore.getText().trim();
        if (scoreStr.isEmpty()) {
            showError("Please enter a score first");
            return;
        }
        
        try {
            int score = Integer.parseInt(scoreStr);
            String grade = ExaminationResult.calculateGrade(score);
            txtGrade.setText(grade);
            
            String message = "Score: " + score + "\nGrade: " + grade + "\n\n";
            if (score >= 70) message += "Excellent!";
            else if (score >= 60) message += "Good work!";
            else if (score >= 50) message += "Satisfactory";
            else if (score >= 40) message += "Passing";
            else message += "Needs improvement";
            
            JOptionPane.showMessageDialog(this, message, "Grade Calculated", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            showError("Invalid score format");
        }
    }
    
    private void addResult() {
        try {
            ExaminationResult result = getResultFromForm();
            resultService.addResult(result);
            showSuccess("Result added successfully!");
            clearForm();
            loadResults();
        } catch (Exception e) {
            showError("Error adding result: " + e.getMessage());
        }
    }
    
    private void updateResult() {
        int row = resultTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a result to update");
            return;
        }
        
        try {
            ExaminationResult result = getResultFromForm();
            resultService.updateResult(result);
            showSuccess("Result updated successfully!");
            loadResults();
        } catch (Exception e) {
            showError("Error updating result: " + e.getMessage());
        }
    }
    
    private void deleteResult() {
        int row = resultTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a result to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this result?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String resultId = tableModel.getValueAt(row, 0).toString();
                resultService.deleteResult(resultId);
                showSuccess("Result deleted successfully!");
                clearForm();
                loadResults();
            } catch (Exception e) {
                showError("Error deleting result: " + e.getMessage());
            }
        }
    }
    
    private void searchResult() {
        String searchId = JOptionPane.showInputDialog(this, "Enter Result ID to search:");
        if (searchId != null && !searchId.trim().isEmpty()) {
            try {
                ExaminationResult r = resultService.getResultById(searchId.trim());
                Enrollment enrollment = enrollmentService.getEnrollmentById(r.getEnrollmentId());
                Student student = studentService.getStudentById(enrollment.getStudentId());
                Course course = courseService.getCourseById(enrollment.getCourseId());
                
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                    r.getResultId(),
                    student.getStudentId(),
                    student.getFirstName() + " " + student.getLastName(),
                    course.getCourseId(),
                    course.getCourseName(),
                    r.getScore(),
                    r.getGrade(),
                    r.getSemester(),
                    r.getYear()
                });
            } catch (Exception e) {
                showError("Result not found!");
                loadResults();
            }
        }
    }
    
    private void clearForm() {
        txtResultId.setText("");
        txtScore.setText("");
        txtGrade.setText("");
        txtSemester.setText("");
        txtYear.setText("");
        resultTable.clearSelection();
        if (cmbEnrollment.getItemCount() > 0) {
            cmbEnrollment.setSelectedIndex(0);
        }
        lblStudentInfo.setText("Select enrollment to see details");
        lblCourseInfo.setText("");
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}