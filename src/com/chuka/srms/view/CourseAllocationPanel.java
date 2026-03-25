package com.chuka.srms.view;

import com.chuka.srms.model.CourseAllocation;
import com.chuka.srms.model.Course;
import com.chuka.srms.model.Tutor;
import com.chuka.srms.service.CourseAllocationService;
import com.chuka.srms.service.CourseService;
import com.chuka.srms.service.TutorService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseAllocationPanel extends JPanel {
    
    // Form Fields
    private JTextField txtAllocationId;
    private JComboBox<String> cmbCourseId;
    private JComboBox<String> cmbTutorId;
    private JTextField txtSemester;
    private JTextField txtYear;
    
    // Display info
    private JLabel lblCourseInfo;
    private JLabel lblTutorInfo;
    
    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnViewAll;
    private JButton btnClearForm;
    
    // Table
    private JTable allocationTable;
    private DefaultTableModel tableModel;
    
    // Services
    private CourseAllocationService allocationService;
    private CourseService courseService;
    private TutorService tutorService;
    
    public CourseAllocationPanel() {
        allocationService = new CourseAllocationService();
        courseService = new CourseService();
        tutorService = new TutorService();
        initializeUI();
        loadAllocations();
        loadComboBoxes();
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
        panel.setBorder(BorderFactory.createTitledBorder("Course Allocation"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtAllocationId = new JTextField(15);
        cmbCourseId = new JComboBox<>();
        cmbTutorId = new JComboBox<>();
        txtSemester = new JTextField(5);
        txtYear = new JTextField(5);
        
        lblCourseInfo = new JLabel("Select course to see details");
        lblTutorInfo = new JLabel("Select tutor to see details");
        lblCourseInfo.setForeground(Color.BLUE);
        lblTutorInfo.setForeground(new Color(0, 100, 0));
        
        // Row 0: Allocation ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Allocation ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtAllocationId, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 3;
        panel.add(cmbCourseId, gbc);
        
        // Row 1: Course Info
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Course Info:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(lblCourseInfo, gbc);
        gbc.gridwidth = 1;
        
        // Row 2: Tutor
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Tutor:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbTutorId, gbc);
        
        // Row 3: Tutor Info
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tutor Info:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(lblTutorInfo, gbc);
        gbc.gridwidth = 1;
        
        // Row 4: Semester and Year
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSemester, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 3;
        panel.add(txtYear, gbc);
        
        // Add listeners
        cmbCourseId.addActionListener(e -> updateCourseInfo());
        cmbTutorId.addActionListener(e -> updateTutorInfo());
        
        return panel;
    }
    
    private void updateCourseInfo() {
        String selected = (String) cmbCourseId.getSelectedItem();
        if (selected != null && !selected.isEmpty()) {
            try {
                String courseId = selected.split(" - ")[0];
                Course course = courseService.getCourseById(courseId);
                lblCourseInfo.setText(course.getCourseId() + " - " + course.getCourseName() + 
                    " (" + course.getCredits() + " credits, " + course.getDepartment() + ")");
            } catch (Exception e) {
                lblCourseInfo.setText("Error loading course info");
            }
        }
    }
    
    private void updateTutorInfo() {
        String selected = (String) cmbTutorId.getSelectedItem();
        if (selected != null && !selected.isEmpty()) {
            try {
                String tutorId = selected.split(" - ")[0];
                Tutor tutor = tutorService.getTutorById(tutorId);
                lblTutorInfo.setText(tutor.getTutorId() + " - " + tutor.getFirstName() + " " + 
                    tutor.getLastName() + " (" + tutor.getDepartment() + ")");
            } catch (Exception e) {
                lblTutorInfo.setText("Error loading tutor info");
            }
        }
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));
        
        btnAdd = new JButton("Allocate Course");
        btnUpdate = new JButton("Update Allocation");
        btnDelete = new JButton("Delete Allocation");
        btnSearch = new JButton("Search");
        btnViewAll = new JButton("View All");
        btnClearForm = new JButton("Clear Form");
        
        styleButton(btnAdd, new Color(0, 150, 0));
        styleButton(btnUpdate, new Color(0, 100, 200));
        styleButton(btnDelete, new Color(200, 0, 0));
        
        btnAdd.addActionListener(e -> addAllocation());
        btnUpdate.addActionListener(e -> updateAllocation());
        btnDelete.addActionListener(e -> deleteAllocation());
        btnSearch.addActionListener(e -> searchAllocation());
        btnViewAll.addActionListener(e -> loadAllocations());
        btnClearForm.addActionListener(e -> clearForm());
        
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnViewAll);
        panel.add(btnClearForm);
        
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
        panel.setBorder(BorderFactory.createTitledBorder("Course Allocations"));
        
        String[] columns = {"Allocation ID", "Course ID", "Course Name", "Tutor ID", "Tutor Name", "Semester", "Year"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        allocationTable = new JTable(tableModel);
        allocationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allocationTable.setRowHeight(25);
        
        allocationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = allocationTable.getSelectedRow();
                if (row >= 0) {
                    loadSelectedAllocation(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(allocationTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadComboBoxes() {
        try {
            // Load courses
            cmbCourseId.removeAllItems();
            List<Course> courses = courseService.getAllCourses();
            for (Course c : courses) {
                cmbCourseId.addItem(c.getCourseId() + " - " + c.getCourseName());
            }
            
            // Load tutors
            cmbTutorId.removeAllItems();
            List<Tutor> tutors = tutorService.getAllTutors();
            for (Tutor t : tutors) {
                cmbTutorId.addItem(t.getTutorId() + " - " + t.getFirstName() + " " + t.getLastName());
            }
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }
    
    private void loadAllocations() {
        try {
            tableModel.setRowCount(0);
            List<CourseAllocation> allocations = allocationService.getAllAllocations();
            
            for (CourseAllocation a : allocations) {
                Course course = courseService.getCourseById(a.getCourseId());
                Tutor tutor = tutorService.getTutorById(a.getTutorId());
                
                tableModel.addRow(new Object[]{
                    a.getAllocationId(),
                    a.getCourseId(),
                    course != null ? course.getCourseName() : "Unknown",
                    a.getTutorId(),
                    tutor != null ? tutor.getFirstName() + " " + tutor.getLastName() : "Unknown",
                    a.getSemester(),
                    a.getYear()
                });
            }
        } catch (Exception e) {
            showError("Error loading allocations: " + e.getMessage());
        }
    }
    
    private void loadSelectedAllocation(int row) {
        txtAllocationId.setText(tableModel.getValueAt(row, 0).toString());
        
        String courseId = tableModel.getValueAt(row, 1).toString();
        String tutorId = tableModel.getValueAt(row, 3).toString();
        
        // Select course in combo box
        for (int i = 0; i < cmbCourseId.getItemCount(); i++) {
            if (cmbCourseId.getItemAt(i).startsWith(courseId)) {
                cmbCourseId.setSelectedIndex(i);
                break;
            }
        }
        
        // Select tutor in combo box
        for (int i = 0; i < cmbTutorId.getItemCount(); i++) {
            if (cmbTutorId.getItemAt(i).startsWith(tutorId)) {
                cmbTutorId.setSelectedIndex(i);
                break;
            }
        }
        
        txtSemester.setText(tableModel.getValueAt(row, 5).toString());
        txtYear.setText(tableModel.getValueAt(row, 6).toString());
    }
    
    private CourseAllocation getAllocationFromForm() throws Exception {
        String allocationId = txtAllocationId.getText().trim();
        String courseId = ((String) cmbCourseId.getSelectedItem()).split(" - ")[0];
        String tutorId = ((String) cmbTutorId.getSelectedItem()).split(" - ")[0];
        String semesterStr = txtSemester.getText().trim();
        String yearStr = txtYear.getText().trim();
        
        if (allocationId.isEmpty()) throw new IllegalArgumentException("Allocation ID is required");
        if (semesterStr.isEmpty()) throw new IllegalArgumentException("Semester is required");
        if (yearStr.isEmpty()) throw new IllegalArgumentException("Year is required");
        
        int semester = Integer.parseInt(semesterStr);
        int year = Integer.parseInt(yearStr);
        
        return new CourseAllocation(allocationId, courseId, tutorId, semester, year);
    }
    
    private void addAllocation() {
        try {
            CourseAllocation allocation = getAllocationFromForm();
            allocationService.addAllocation(allocation);
            showSuccess("Course allocated successfully!");
            clearForm();
            loadAllocations();
        } catch (Exception e) {
            showError("Error adding allocation: " + e.getMessage());
        }
    }
    
    private void updateAllocation() {
        int row = allocationTable.getSelectedRow();
        if (row < 0) {
            showError("Please select an allocation to update");
            return;
        }
        
        try {
            CourseAllocation allocation = getAllocationFromForm();
            allocationService.updateAllocation(allocation);
            showSuccess("Allocation updated successfully!");
            loadAllocations();
        } catch (Exception e) {
            showError("Error updating allocation: " + e.getMessage());
        }
    }
    
    private void deleteAllocation() {
        int row = allocationTable.getSelectedRow();
        if (row < 0) {
            showError("Please select an allocation to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this allocation?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String allocationId = tableModel.getValueAt(row, 0).toString();
                allocationService.deleteAllocation(allocationId);
                showSuccess("Allocation deleted successfully!");
                clearForm();
                loadAllocations();
            } catch (Exception e) {
                showError("Error deleting allocation: " + e.getMessage());
            }
        }
    }
    
    private void searchAllocation() {
        String searchId = JOptionPane.showInputDialog(this, "Enter Allocation ID to search:");
        if (searchId != null && !searchId.trim().isEmpty()) {
            try {
                CourseAllocation a = allocationService.getAllocationById(searchId.trim());
                Course course = courseService.getCourseById(a.getCourseId());
                Tutor tutor = tutorService.getTutorById(a.getTutorId());
                
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                    a.getAllocationId(),
                    a.getCourseId(),
                    course.getCourseName(),
                    a.getTutorId(),
                    tutor.getFirstName() + " " + tutor.getLastName(),
                    a.getSemester(),
                    a.getYear()
                });
            } catch (Exception e) {
                showError("Allocation not found!");
                loadAllocations();
            }
        }
    }
    
    private void clearForm() {
        txtAllocationId.setText("");
        txtSemester.setText("");
        txtYear.setText("");
        allocationTable.clearSelection();
        if (cmbCourseId.getItemCount() > 0) cmbCourseId.setSelectedIndex(0);
        if (cmbTutorId.getItemCount() > 0) cmbTutorId.setSelectedIndex(0);
        lblCourseInfo.setText("Select course to see details");
        lblTutorInfo.setText("Select tutor to see details");
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}