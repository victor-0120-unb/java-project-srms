package com.chuka.srms.view;

import com.chuka.srms.model.Enrollment;
import com.chuka.srms.model.Student;
import com.chuka.srms.model.Course;
import com.chuka.srms.service.EnrollmentService;
import com.chuka.srms.service.StudentService;
import com.chuka.srms.service.CourseService;
import com.chuka.srms.service.AcademicProgressionService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EnrollmentPanel extends JPanel {
    
    // Form Fields
    private JTextField txtEnrollmentId;
    private JComboBox<String> cmbStudentId;
    private JComboBox<String> cmbCourseId;
    private JTextField txtSemester;
    private JTextField txtYear;
    private JTextField txtEnrollmentDate;
    
    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnViewAll;
    private JButton btnClearForm;
    private JButton btnCheckLoad;
    private JButton refreshButton;
    
    // Table
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;
    
    // Services
    private EnrollmentService enrollmentService;
    private StudentService studentService;
    private CourseService courseService;
    private AcademicProgressionService progressionService;
    
    private static final int MIN_COURSES = 10;
    
    public EnrollmentPanel() {
        enrollmentService = new EnrollmentService();
        studentService = new StudentService();
        courseService = new CourseService();
        progressionService = new AcademicProgressionService();
        initializeUI();
        loadEnrollments();
        loadComboBoxes();
    }

    private void refreshEnrollments() {
        loadEnrollments();
    }

    public JComboBox<String> getStudentComboBox() {
        return cmbStudentId;
    }

    public JComboBox<String> getCourseComboBox() {
    return cmbCourseId;
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
        panel.setBorder(BorderFactory.createTitledBorder("Enrollment Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtEnrollmentId = new JTextField(15);
        cmbStudentId = new JComboBox<>();
        cmbCourseId = new JComboBox<>();
        txtSemester = new JTextField(5);
        txtYear = new JTextField(5);
        txtEnrollmentDate = new JTextField(15);
        txtEnrollmentDate.setText(LocalDate.now().toString());
        
        // Row 0: Enrollment ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Enrollment ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEnrollmentId, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 3;
        panel.add(cmbStudentId, gbc);
        
        // Row 1: Course ID and Semester
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Course ID:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbCourseId, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 3;
        panel.add(txtSemester, gbc);
        
        // Row 2: Year and Enrollment Date
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        panel.add(txtYear, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Enrollment Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        panel.add(txtEnrollmentDate, gbc);

        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));
        
        btnAdd = new JButton("Add Enrollment");
        btnUpdate = new JButton("Update Enrollment");
        btnDelete = new JButton("Delete Enrollment");
        btnSearch = new JButton("Search");
        btnViewAll = new JButton("View All");
        btnClearForm = new JButton("Clear Form");
        btnCheckLoad = new JButton("Check Course Load");
        refreshButton = new JButton("Refresh");
        
        styleButton(btnAdd, new Color(0, 150, 0));
        styleButton(btnUpdate, new Color(0, 100, 200));
        styleButton(btnDelete, new Color(200, 0, 0));
        btnCheckLoad.setBackground(new Color(255, 165, 0));
        
        btnAdd.addActionListener(e -> addEnrollment());
        btnUpdate.addActionListener(e -> updateEnrollment());
        btnDelete.addActionListener(e -> deleteEnrollment());
        btnSearch.addActionListener(e -> searchEnrollment());
        btnViewAll.addActionListener(e -> loadEnrollments());
        btnClearForm.addActionListener(e -> clearForm());
        btnCheckLoad.addActionListener(e -> checkCourseLoad());
        refreshButton.addActionListener(e -> refreshEnrollments());
        
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnViewAll);
        panel.add(btnClearForm);
        panel.add(btnCheckLoad);
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
        panel.setBorder(BorderFactory.createTitledBorder("Enrollment List"));
        
        String[] columns = {"Enrollment ID", "Student ID", "Student Name", "Course ID", "Course Name", "Semester", "Year", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrollmentTable.setRowHeight(25);
        
        enrollmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = enrollmentTable.getSelectedRow();
                if (row >= 0) {
                    loadSelectedEnrollment(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadComboBoxes() {
        try {
            // Load students
            cmbStudentId.removeAllItems();
            List<Student> students = studentService.getAllStudents();
            for (Student s : students) {
                cmbStudentId.addItem(s.getStudentId() + " - " + s.getFirstName() + " " + s.getLastName());
            }
            
            // Load courses
            cmbCourseId.removeAllItems();
            List<Course> courses = courseService.getAllCourses();
            for (Course c : courses) {
                cmbCourseId.addItem(c.getCourseId() + " - " + c.getCourseName());
            }
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }
    
    private void loadEnrollments() {
        try {
            tableModel.setRowCount(0);
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            
            for (Enrollment e : enrollments) {
                Student student = studentService.getStudentById(e.getStudentId());
                Course course = courseService.getCourseById(e.getCourseId());
                
                tableModel.addRow(new Object[]{
                    e.getEnrollmentId(),
                    e.getStudentId(),
                    student != null ? student.getFirstName() + " " + student.getLastName() : "Unknown",
                    e.getCourseId(),
                    course != null ? course.getCourseName() : "Unknown",
                    e.getSemester(),
                    e.getYear(),
                    e.getEnrollmentDate()
                });
            }
        } catch (Exception e) {
            showError("Error loading enrollments: " + e.getMessage());
        }
    }
    
    private void loadSelectedEnrollment(int row) {
        txtEnrollmentId.setText(tableModel.getValueAt(row, 0).toString());
        String studentId = tableModel.getValueAt(row, 1).toString();
        String courseId = tableModel.getValueAt(row, 3).toString();
        
        // Select student in combo box
        for (int i = 0; i < cmbStudentId.getItemCount(); i++) {
            if (cmbStudentId.getItemAt(i).startsWith(studentId)) {
                cmbStudentId.setSelectedIndex(i);
                break;
            }
        }
        
        // Select course in combo box
        for (int i = 0; i < cmbCourseId.getItemCount(); i++) {
            if (cmbCourseId.getItemAt(i).startsWith(courseId)) {
                cmbCourseId.setSelectedIndex(i);
                break;
            }
        }
        
        txtSemester.setText(tableModel.getValueAt(row, 5).toString());
        txtYear.setText(tableModel.getValueAt(row, 6).toString());
        txtEnrollmentDate.setText(tableModel.getValueAt(row, 7).toString());
    }
    
    private Enrollment getEnrollmentFromForm() throws Exception {
        String enrollmentId = txtEnrollmentId.getText().trim();
        String studentId = ((String) cmbStudentId.getSelectedItem()).split(" - ")[0];
        String courseId = ((String) cmbCourseId.getSelectedItem()).split(" - ")[0];
        String semesterStr = txtSemester.getText().trim();
        String yearStr = txtYear.getText().trim();
        String dateStr = txtEnrollmentDate.getText().trim();
        
        if (enrollmentId.isEmpty()) throw new IllegalArgumentException("Enrollment ID is required");
        if (semesterStr.isEmpty()) throw new IllegalArgumentException("Semester is required");
        if (yearStr.isEmpty()) throw new IllegalArgumentException("Year is required");
        
        int semester = Integer.parseInt(semesterStr);
        int year = Integer.parseInt(yearStr);
        LocalDate date = LocalDate.parse(dateStr);
        
        return new Enrollment(enrollmentId, studentId, courseId, semester, year, date);
    }
    
    private void addEnrollment() {
        try {
            Enrollment enrollment = getEnrollmentFromForm();
            enrollmentService.addEnrollment(enrollment);
            showSuccess("Enrollment added successfully!");
            clearForm();
            loadEnrollments();
        } catch (Exception e) {
            showError("Error adding enrollment: " + e.getMessage());
        }
    }
    
    private void updateEnrollment() {
        int row = enrollmentTable.getSelectedRow();
        if (row < 0) {
            showError("Please select an enrollment to update");
            return;
        }
        
        try {
            Enrollment enrollment = getEnrollmentFromForm();
            enrollmentService.updateEnrollment(enrollment);
            showSuccess("Enrollment updated successfully!");
            loadEnrollments();
        } catch (Exception e) {
            showError("Error updating enrollment: " + e.getMessage());
        }
    }
    
    private void deleteEnrollment() {
        int row = enrollmentTable.getSelectedRow();
        if (row < 0) {
            showError("Please select an enrollment to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this enrollment?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String enrollmentId = tableModel.getValueAt(row, 0).toString();
                enrollmentService.deleteEnrollment(enrollmentId);
                showSuccess("Enrollment deleted successfully!");
                clearForm();
                loadEnrollments();
            } catch (Exception e) {
                showError("Error deleting enrollment: " + e.getMessage());
            }
        }
    }
    
    private void searchEnrollment() {
        String searchId = JOptionPane.showInputDialog(this, "Enter Enrollment ID to search:");
        if (searchId != null && !searchId.trim().isEmpty()) {
            try {
                Enrollment e = enrollmentService.getEnrollmentById(searchId.trim());
                Student student = studentService.getStudentById(e.getStudentId());
                Course course = courseService.getCourseById(e.getCourseId());
                
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                    e.getEnrollmentId(),
                    e.getStudentId(),
                    student.getFirstName() + " " + student.getLastName(),
                    e.getCourseId(),
                    course.getCourseName(),
                    e.getSemester(),
                    e.getYear(),
                    e.getEnrollmentDate()
                });
            } catch (Exception e) {
                showError("Enrollment not found!");
                loadEnrollments();
            }
        }
    }
    
    private void checkCourseLoad() {
        String selected = (String) cmbStudentId.getSelectedItem();
        if (selected == null) {
            showError("Please select a student");
            return;
        }
        
        String studentId = selected.split(" - ")[0];
        String semesterStr = txtSemester.getText().trim();
        String yearStr = txtYear.getText().trim();
        
        if (semesterStr.isEmpty() || yearStr.isEmpty()) {
            showError("Please enter semester and year");
            return;
        }
        
        try {
            int semester = Integer.parseInt(semesterStr);
            int year = Integer.parseInt(yearStr);
            
            int enrolledCount = enrollmentService.countEnrollmentsByStudent(studentId, semester, year);
            boolean meetsRequirement = progressionService.meetsCourseLoadRequirement(studentId, semester, year, enrolledCount);
            
            String message = "Student: " + studentId + "\n" +
                            "Semester: " + semester + ", Year: " + year + "\n" +
                            "Courses Enrolled: " + enrolledCount + "\n" +
                            "Minimum Required: " + MIN_COURSES + "\n\n";
            
            if (meetsRequirement) {
                message += "✓ Student meets the minimum course load requirement!";
                JOptionPane.showMessageDialog(this, message, "Course Load Check", JOptionPane.INFORMATION_MESSAGE);
            } else {
                message += "✗ Student does NOT meet the minimum course load requirement!\n" +
                          "Need " + (MIN_COURSES - enrolledCount) + " more courses.";
                JOptionPane.showMessageDialog(this, message, "Course Load Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            showError("Error checking course load: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        txtEnrollmentId.setText("");
        txtSemester.setText("");
        txtYear.setText("");
        txtEnrollmentDate.setText(LocalDate.now().toString());
        enrollmentTable.clearSelection();
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}