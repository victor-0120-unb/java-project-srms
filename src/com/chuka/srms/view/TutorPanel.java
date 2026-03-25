package com.chuka.srms.view;

import com.chuka.srms.model.Tutor;
import com.chuka.srms.service.TutorService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TutorPanel extends JPanel {
    
    // Form Fields
    private JTextField txtTutorId;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtDepartment;
    
    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnViewAll;
    private JButton btnClearForm;
    
    // Table
    private JTable tutorTable;
    private DefaultTableModel tableModel;
    
    // Service
    private TutorService tutorService;
    
    public TutorPanel() {
        tutorService = new TutorService();
        initializeUI();
        loadTutors();
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
        panel.setBorder(BorderFactory.createTitledBorder("Tutor Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtTutorId = new JTextField(15);
        txtFirstName = new JTextField(15);
        txtLastName = new JTextField(15);
        txtDepartment = new JTextField(15);
        
        // Row 0: Tutor ID and First Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tutor ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTutorId, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3;
        panel.add(txtFirstName, gbc);
        
        // Row 1: Last Name and Department
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        panel.add(txtLastName, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 3;
        panel.add(txtDepartment, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));
        
        btnAdd = new JButton("Add Tutor");
        btnUpdate = new JButton("Update Tutor");
        btnDelete = new JButton("Delete Tutor");
        btnSearch = new JButton("Search Tutor");
        btnViewAll = new JButton("View All Tutors");
        btnClearForm = new JButton("Clear Form");
        
        styleButton(btnAdd, new Color(0, 150, 0));
        styleButton(btnUpdate, new Color(0, 100, 200));
        styleButton(btnDelete, new Color(200, 0, 0));
        
        btnAdd.addActionListener(e -> addTutor());
        btnUpdate.addActionListener(e -> updateTutor());
        btnDelete.addActionListener(e -> deleteTutor());
        btnSearch.addActionListener(e -> searchTutor());
        btnViewAll.addActionListener(e -> loadTutors());
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
        panel.setBorder(BorderFactory.createTitledBorder("Tutor List"));
        
        String[] columns = {"Tutor ID", "First Name", "Last Name", "Department"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tutorTable = new JTable(tableModel);
        tutorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tutorTable.setRowHeight(25);
        
        tutorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tutorTable.getSelectedRow();
                if (row >= 0) {
                    loadSelectedTutor(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tutorTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadTutors() {
        try {
            tableModel.setRowCount(0);
            List<Tutor> tutors = tutorService.getAllTutors();
            
            for (Tutor t : tutors) {
                tableModel.addRow(new Object[]{
                    t.getTutorId(),
                    t.getFirstName(),
                    t.getLastName(),
                    t.getDepartment()
                });
            }
        } catch (Exception e) {
            showError("Error loading tutors: " + e.getMessage());
        }
    }
    
    private void loadSelectedTutor(int row) {
        txtTutorId.setText(tableModel.getValueAt(row, 0).toString());
        txtFirstName.setText(tableModel.getValueAt(row, 1).toString());
        txtLastName.setText(tableModel.getValueAt(row, 2).toString());
        txtDepartment.setText(tableModel.getValueAt(row, 3).toString());
    }
    
    private Tutor getTutorFromForm() throws Exception {
        String tutorId = txtTutorId.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String department = txtDepartment.getText().trim();
        
        if (tutorId.isEmpty()) throw new IllegalArgumentException("Tutor ID is required");
        if (firstName.isEmpty()) throw new IllegalArgumentException("First name is required");
        if (lastName.isEmpty()) throw new IllegalArgumentException("Last name is required");
        if (department.isEmpty()) throw new IllegalArgumentException("Department is required");
        
        return new Tutor(tutorId, firstName, lastName, department);
    }
    
    private void addTutor() {
        try {
            Tutor tutor = getTutorFromForm();
            tutorService.addTutor(tutor);
            showSuccess("Tutor added successfully!");
            clearForm();
            loadTutors();
        } catch (Exception e) {
            showError("Error adding tutor: " + e.getMessage());
        }
    }
    
    private void updateTutor() {
        int row = tutorTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a tutor to update");
            return;
        }
        
        try {
            Tutor tutor = getTutorFromForm();
            tutorService.updateTutor(tutor);
            showSuccess("Tutor updated successfully!");
            loadTutors();
        } catch (Exception e) {
            showError("Error updating tutor: " + e.getMessage());
        }
    }
    
    private void deleteTutor() {
        int row = tutorTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a tutor to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this tutor?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String tutorId = tableModel.getValueAt(row, 0).toString();
                tutorService.deleteTutor(tutorId);
                showSuccess("Tutor deleted successfully!");
                clearForm();
                loadTutors();
            } catch (Exception e) {
                showError("Error deleting tutor: " + e.getMessage());
            }
        }
    }
    
    private void searchTutor() {
        String searchId = JOptionPane.showInputDialog(this, "Enter Tutor ID to search:");
        if (searchId != null && !searchId.trim().isEmpty()) {
            try {
                Tutor tutor = tutorService.getTutorById(searchId.trim());
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                    tutor.getTutorId(),
                    tutor.getFirstName(),
                    tutor.getLastName(),
                    tutor.getDepartment()
                });
            } catch (Exception e) {
                showError("Tutor not found!");
                loadTutors();
            }
        }
    }
    
    private void clearForm() {
        txtTutorId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtDepartment.setText("");
        tutorTable.clearSelection();
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}