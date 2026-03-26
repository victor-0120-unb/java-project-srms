package com.chuka.srms.view;

import com.chuka.srms.model.Course;
import com.chuka.srms.service.CourseService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CoursePanel extends JPanel {
    
    // Form Fields
    private JTextField txtCourseId;
    private JTextField txtCourseName;
    private JTextField txtCredits;
    private JTextField txtDepartment;
    
    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnViewAll;
    private JButton btnClearForm;
    private JButton refreshButton;
    
    // Table
    private JTable courseTable;
    private DefaultTableModel tableModel;
    
    // Service
    private CourseService courseService;
    
    private EnrollmentPanel enrollmentPanel;

    public CoursePanel() {
        courseService = new CourseService();
        initializeUI();
        loadCourses();
    }

    
    public CoursePanel(EnrollmentPanel enrollmentPanel) {
        this.enrollmentPanel = enrollmentPanel;
        courseService = new CourseService();
        initializeUI();
        loadCourses();
    }


    private void refreshCourses() {
        loadCourses(); // refresh the course table

        if (enrollmentPanel != null) {
            reloadCourseDropdown(enrollmentPanel.getCourseComboBox());
        }

        System.out.println("Courses refreshed and enrollment dropdown updated!");
    }


    private void reloadCourseDropdown(JComboBox<String> comboBox) {
        try {
            comboBox.removeAllItems();
            List<Course> courses = courseService.getAllCourses();
            for (Course c : courses) {
                comboBox.addItem(c.getCourseId() + " - " + c.getCourseName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading course dropdown: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
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
        panel.setBorder(BorderFactory.createTitledBorder("Course Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create text fields
        txtCourseId = new JTextField(15);
        txtCourseName = new JTextField(20);
        txtCredits = new JTextField(5);
        txtDepartment = new JTextField(15);
        
        // Row 0: Course ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Course ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCourseId, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 3;
        panel.add(txtCourseName, gbc);
        
        // Row 1: Credits and Department
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCredits, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 3;
        panel.add(txtDepartment, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));
        
        // Create buttons
        btnAdd = new JButton("Add Course");
        btnUpdate = new JButton("Update Course");
        btnDelete = new JButton("Delete Course");
        btnSearch = new JButton("Search Course");
        btnViewAll = new JButton("View All Courses");
        btnClearForm = new JButton("Clear Form");
        refreshButton = new JButton("Refresh");
        
        // Style buttons
        styleButton(btnAdd, new Color(0, 150, 0));
        styleButton(btnUpdate, new Color(0, 100, 200));
        styleButton(btnDelete, new Color(200, 0, 0));
        
        // Add action listeners
        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnSearch.addActionListener(e -> searchCourse());
        btnViewAll.addActionListener(e -> loadCourses());
        btnClearForm.addActionListener(e -> clearForm());
        refreshButton.addActionListener(e -> refreshCourses());
        
        // Add buttons to panel
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnViewAll);
        panel.add(btnClearForm);
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
        panel.setBorder(BorderFactory.createTitledBorder("Course List"));
        
        // Table columns
        String[] columns = {"Course ID", "Course Name", "Credits", "Department"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.setRowHeight(25);
        
        // Load selected course when clicking on a row
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = courseTable.getSelectedRow();
                if (row >= 0) {
                    loadSelectedCourse(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadCourses() {
        try {
            tableModel.setRowCount(0);
            List<Course> courses = courseService.getAllCourses();
            
            for (Course c : courses) {
                tableModel.addRow(new Object[]{
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getCredits(),
                    c.getDepartment()
                });
            }
            
            if (courses.isEmpty()) {
                // Add sample message
                tableModel.addRow(new Object[]{"No courses found", "Add a course using the form above", "", ""});
            }
        } catch (Exception e) {
            showError("Error loading courses: " + e.getMessage());
        }
    }
    
    private void loadSelectedCourse(int row) {
        txtCourseId.setText(tableModel.getValueAt(row, 0).toString());
        txtCourseName.setText(tableModel.getValueAt(row, 1).toString());
        txtCredits.setText(tableModel.getValueAt(row, 2).toString());
        txtDepartment.setText(tableModel.getValueAt(row, 3).toString());
    }
    
    private Course getCourseFromForm() throws Exception {
        String courseId = txtCourseId.getText().trim();
        String courseName = txtCourseName.getText().trim();
        String creditsStr = txtCredits.getText().trim();
        String department = txtDepartment.getText().trim();
        
        // Validation
        if (courseId.isEmpty()) throw new IllegalArgumentException("Course ID is required");
        if (courseName.isEmpty()) throw new IllegalArgumentException("Course Name is required");
        if (creditsStr.isEmpty()) throw new IllegalArgumentException("Credits are required");
        if (department.isEmpty()) throw new IllegalArgumentException("Department is required");
        
        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
            if (credits <= 0) throw new IllegalArgumentException("Credits must be positive");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Credits must be a number");
        }
        
        return new Course(courseId, courseName, credits, department);
    }
    
    private void addCourse() {
        try {
            Course course = getCourseFromForm();
            courseService.addCourse(course);
            showSuccess("Course added successfully!");
            clearForm();
            loadCourses();
        } catch (Exception e) {
            showError("Error adding course: " + e.getMessage());
        }
    }
    
    private void updateCourse() {
        int row = courseTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a course to update");
            return;
        }
        
        try {
            Course course = getCourseFromForm();
            courseService.updateCourse(course);
            showSuccess("Course updated successfully!");
            loadCourses();
        } catch (Exception e) {
            showError("Error updating course: " + e.getMessage());
        }
    }
    
    private void deleteCourse() {
        int row = courseTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a course to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this course?\nThis will also delete all enrollments for this course!",
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String courseId = tableModel.getValueAt(row, 0).toString();
                courseService.deleteCourse(courseId);
                showSuccess("Course deleted successfully!");
                clearForm();
                loadCourses();
            } catch (Exception e) {
                showError("Error deleting course: " + e.getMessage());
            }
        }
    }
    
    private void searchCourse() {
        String searchId = JOptionPane.showInputDialog(this, "Enter Course ID to search:");
        if (searchId != null && !searchId.trim().isEmpty()) {
            try {
                Course course = courseService.getCourseById(searchId.trim());
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCredits(),
                    course.getDepartment()
                });
            } catch (Exception e) {
                showError("Course not found!");
                loadCourses();
            }
        }
    }
    
    private void clearForm() {
        txtCourseId.setText("");
        txtCourseName.setText("");
        txtCredits.setText("");
        txtDepartment.setText("");
        courseTable.clearSelection();
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}