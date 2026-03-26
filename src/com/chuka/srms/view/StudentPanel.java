package com.chuka.srms.view;

import com.chuka.srms.model.Student;
import com.chuka.srms.service.StudentService;
import com.chuka.srms.service.AcademicProgressionService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class StudentPanel extends JPanel {

    // Form Fields
    private JTextField txtStudentId;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtDateOfBirth;
    private JTextField txtMajor;
    private JTextField txtCurrentSemester;
    private JTextField txtCurrentYear;
    private JComboBox<String> cmbAcademicStatus;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnViewAll;
    private JButton btnClearForm;
    private JButton btnPromote;
    private JButton btnClearGraduation;
    private JButton refreshButton;

    // Table
    private JTable studentTable;
    private DefaultTableModel tableModel;

    // Services
    private StudentService studentService;
    private AcademicProgressionService progressionService;

    private EnrollmentPanel enrollmentPanel;

    public StudentPanel() {
        studentService = new StudentService();
        progressionService = new AcademicProgressionService();
        initializeUI();
        loadStudents();
    }

    public StudentPanel(EnrollmentPanel enrollmentPanel) {
        this.enrollmentPanel = enrollmentPanel; // store reference
        studentService = new StudentService();
        progressionService = new AcademicProgressionService();
        initializeUI();
        loadStudents();
    }

    private void refreshStudents() {
        loadStudents(); // refresh table
        if (enrollmentPanel != null) {
            JComboBox<String> comboBox = enrollmentPanel.getStudentComboBox();
            comboBox.removeAllItems();
            try {
                List<Student> students = studentService.getAllStudents();
                for (Student s : students) {
                    comboBox.addItem(s.getStudentId() + " - " + s.getFirstName() + " " + s.getLastName());
                }
            } catch (Exception e) {
                showError("Error updating enrollment dropdown: " + e.getMessage());
            }
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add form panel at top
        add(createFormPanel(), BorderLayout.NORTH);

        // Add button panel in center
        add(createButtonPanel(), BorderLayout.CENTER);

        // Add table panel at bottom
        add(createTablePanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create text fields
        txtStudentId = new JTextField(15);
        txtFirstName = new JTextField(15);
        txtLastName = new JTextField(15);
        txtDateOfBirth = new JTextField(15);
        txtMajor = new JTextField(15);
        txtCurrentSemester = new JTextField(15);
        txtCurrentYear = new JTextField(15);
        cmbAcademicStatus = new JComboBox<>(
                new String[] { "Active", "On Probation", "Cleared for Graduation", "Graduated" });

        // Row 0: Student ID and First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtStudentId, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3;
        panel.add(txtFirstName, gbc);

        // Row 1: Last Name and Date of Birth
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        panel.add(txtLastName, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        panel.add(txtDateOfBirth, gbc);

        // Row 2: Major and Current Semester
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Major:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMajor, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Current Semester:"), gbc);
        gbc.gridx = 3;
        panel.add(txtCurrentSemester, gbc);

        // Row 3: Current Year and Academic Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Current Year:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCurrentYear, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Academic Status:"), gbc);
        gbc.gridx = 3;
        panel.add(cmbAcademicStatus, gbc);

        return panel;

    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));

        // Create buttons
        btnAdd = new JButton("Add Student");
        btnUpdate = new JButton("Update Student");
        btnDelete = new JButton("Delete Student");
        btnSearch = new JButton("Search Student");
        btnViewAll = new JButton("View All Students");
        btnClearForm = new JButton("Clear Form");
        btnPromote = new JButton("Promote to Next Class");
        btnClearGraduation = new JButton("Clear for Graduation");
        refreshButton = new JButton("Refresh");

        // Style buttons
        styleButton(btnAdd, new Color(0, 150, 0));
        styleButton(btnUpdate, new Color(0, 100, 200));
        styleButton(btnDelete, new Color(200, 0, 0));
        btnPromote.setBackground(new Color(255, 165, 0));
        btnClearGraduation.setBackground(new Color(0, 100, 100));

        // Add action listeners
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSearch.addActionListener(e -> searchStudent());
        btnViewAll.addActionListener(e -> loadStudents());
        btnClearForm.addActionListener(e -> clearForm());
        btnPromote.addActionListener(e -> promoteStudent());
        btnClearGraduation.addActionListener(e -> clearForGraduation());
        refreshButton.addActionListener(e -> refreshStudents());

        // Add buttons to panel
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnViewAll);
        panel.add(btnClearForm);
        panel.add(btnPromote);
        panel.add(btnClearGraduation);
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
        panel.setBorder(BorderFactory.createTitledBorder("Student List"));

        // Table columns
        String[] columns = { "Student ID", "First Name", "Last Name", "Date of Birth",
                "Major", "Semester", "Year", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(25);

        // Load selected student when clicking on a row
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    loadSelectedStudent(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadStudents() {
        try {
            tableModel.setRowCount(0);
            List<Student> students = studentService.getAllStudents();

            for (Student s : students) {
                tableModel.addRow(new Object[] {
                        s.getStudentId(),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getDateOfBirth(),
                        s.getMajor(),
                        s.getCurrentSemester(),
                        s.getCurrentYear(),
                        s.getAcademicStatus()
                });
            }
        } catch (Exception e) {
            showError("Error loading students: " + e.getMessage());
        }
    }

    private void loadSelectedStudent(int row) {
        txtStudentId.setText(tableModel.getValueAt(row, 0).toString());
        txtFirstName.setText(tableModel.getValueAt(row, 1).toString());
        txtLastName.setText(tableModel.getValueAt(row, 2).toString());
        txtDateOfBirth.setText(tableModel.getValueAt(row, 3).toString());
        txtMajor.setText(tableModel.getValueAt(row, 4).toString());
        txtCurrentSemester.setText(tableModel.getValueAt(row, 5).toString());
        txtCurrentYear.setText(tableModel.getValueAt(row, 6).toString());
        cmbAcademicStatus.setSelectedItem(tableModel.getValueAt(row, 7).toString());
    }

    private Student getStudentFromForm() throws Exception {
        String studentId = txtStudentId.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String dobStr = txtDateOfBirth.getText().trim();
        String major = txtMajor.getText().trim();
        String semesterStr = txtCurrentSemester.getText().trim();
        String yearStr = txtCurrentYear.getText().trim();
        String status = (String) cmbAcademicStatus.getSelectedItem();

        // Validation
        if (studentId.isEmpty())
            throw new IllegalArgumentException("Student ID is required");
        if (firstName.isEmpty())
            throw new IllegalArgumentException("First name is required");
        if (lastName.isEmpty())
            throw new IllegalArgumentException("Last name is required");
        if (dobStr.isEmpty())
            throw new IllegalArgumentException("Date of birth is required");
        if (major.isEmpty())
            throw new IllegalArgumentException("Major is required");
        if (semesterStr.isEmpty())
            throw new IllegalArgumentException("Semester is required");
        if (yearStr.isEmpty())
            throw new IllegalArgumentException("Year is required");

        LocalDate dob;
        try {
            dob = LocalDate.parse(dobStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }

        int semester, year;
        try {
            semester = Integer.parseInt(semesterStr);
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Semester and Year must be numbers");
        }

        return new Student(studentId, firstName, lastName, dob, major, semester, year, status);
    }

    private void addStudent() {
        try {
            Student student = getStudentFromForm();
            studentService.addStudent(student);
            showSuccess("Student added successfully!");
            clearForm();
            loadStudents();
        } catch (Exception e) {
            showError("Error adding student: " + e.getMessage());
        }
    }

    private void updateStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a student to update");
            return;
        }

        try {
            Student student = getStudentFromForm();
            studentService.updateStudent(student);
            showSuccess("Student updated successfully!");
            loadStudents();
        } catch (Exception e) {
            showError("Error updating student: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a student to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String studentId = tableModel.getValueAt(row, 0).toString();
                studentService.deleteStudent(studentId);
                showSuccess("Student deleted successfully!");
                clearForm();
                loadStudents();
            } catch (Exception e) {
                showError("Error deleting student: " + e.getMessage());
            }
        }
    }

    private void searchStudent() {
        String searchId = JOptionPane.showInputDialog(this, "Enter Student ID to search:");
        if (searchId != null && !searchId.trim().isEmpty()) {
            try {
                Student student = studentService.getStudentById(searchId.trim());
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[] {
                        student.getStudentId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getDateOfBirth(),
                        student.getMajor(),
                        student.getCurrentSemester(),
                        student.getCurrentYear(),
                        student.getAcademicStatus()
                });
            } catch (Exception e) {
                showError("Student not found!");
                loadStudents();
            }
        }
    }

    private void clearForm() {
        txtStudentId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtDateOfBirth.setText("");
        txtMajor.setText("");
        txtCurrentSemester.setText("");
        txtCurrentYear.setText("");
        cmbAcademicStatus.setSelectedIndex(0);
        studentTable.clearSelection();
    }

    private void promoteStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a student to promote");
            return;
        }

        try {
            String studentId = tableModel.getValueAt(row, 0).toString();
            boolean promoted = progressionService.promoteStudent(studentId);

            if (promoted) {
                showSuccess("Student promoted successfully!");
                loadStudents();
            } else {
                showError(progressionService.getPromotionMessage(studentId));
            }
        } catch (Exception e) {
            showError("Error promoting student: " + e.getMessage());
        }
    }

    private void clearForGraduation() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a student to clear for graduation");
            return;
        }

        try {
            String studentId = tableModel.getValueAt(row, 0).toString();
            boolean cleared = progressionService.clearForGraduation(studentId);

            if (cleared) {
                showSuccess("Student cleared for graduation!");
                loadStudents();
            } else {
                showError("Student does not meet graduation requirements");
            }
        } catch (Exception e) {
            showError("Error clearing for graduation: " + e.getMessage());
        }
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}