package com.student.view;
import com.student.dao.StudentDAO;
import com.student.model.Student;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainUI extends JFrame {
    private final StudentDAO dao = new StudentDAO();

    // Input fields
    private final JTextField nameField = new JTextField(15);
    private final JTextField emailField = new JTextField(15);
    private final JTextField courseField = new JTextField(15);
    private final JTextField searchField = new JTextField(15);

    // Buttons
    private final JButton addBtn = new JButton("Add Student");
    private final JButton updateBtn = new JButton("Update Student");
    private final JButton deleteBtn = new JButton("Delete Student");
    private final JButton clearBtn = new JButton("Clear");
    private final JButton searchBtn = new JButton("Search");
    private final JButton clearSearchBtn = new JButton("Clear Search");
    private final JButton refreshBtn = new JButton("Refresh");

    // Table
    private final JTable studentTable;
    private final DefaultTableModel tableModel;

    // Status
    private final JLabel statusLabel = new JLabel("Total Students: 0");

    public MainUI() {
        setTitle("Student Database Management System");
        setLayout(new BorderLayout());

        // Create table
        String[] columnNames = {"ID", "Name", "Email", "Course"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);

        // Input panel
        JPanel inputPanel = createInputPanel();

        // Button panel
        JPanel buttonPanel = createButtonPanel();

        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 280));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event listeners
        setupEventListeners();

        // Initial data load
        refreshStudentList();

        // Frame settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, gbc);

        // Course
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(courseField, gbc);

        // Search
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(searchField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(clearBtn);
        panel.add(searchBtn);
        panel.add(clearSearchBtn);
        panel.add(refreshBtn);

        // Initially disable update and delete buttons
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        return panel;
    }

    private void setupEventListeners() {
        // Add button
        addBtn.addActionListener(e -> addStudent());

        // Update button
        updateBtn.addActionListener(e -> updateStudent());

        // Delete button
        deleteBtn.addActionListener(e -> deleteStudent());

        // Clear button
        clearBtn.addActionListener(e -> clearFields());

        // Search button
        searchBtn.addActionListener(e -> searchStudents());

        // Clear Search button
        clearSearchBtn.addActionListener(e -> clearSearch());

        // Refresh button
        refreshBtn.addActionListener(e -> refreshStudentList());

        // Table selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    loadSelectedStudentToFields(selectedRow);
                    updateBtn.setEnabled(true);
                    deleteBtn.setEnabled(true);
                } else {
                    updateBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                }
            }
        });

        // Enter key support for search
        searchField.addActionListener(e -> searchStudents());
    }

    private void addStudent() {
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String course = courseField.getText().isEmpty() ? "Computer Science" : courseField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (dao.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "Email already exists!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            dao.addStudent(name, email, course);
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            clearFields();
            refreshStudentList();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        try {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a student to update!", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String course = courseField.getText().isEmpty() ? "Computer Science" : courseField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if email exists for another student
            Student existingStudent = dao.getStudentById(studentId);
            if (existingStudent != null && !existingStudent.getEmail().equals(email) && dao.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "Email already exists!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = dao.updateStudent(studentId, name, email, course);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                clearFields();
                refreshStudentList();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        try {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a student to delete!", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String studentName = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student '" + studentName + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = dao.deleteStudent(studentId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                    clearFields();
                    refreshStudentList();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete student!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting student: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudents() {
        try {
            String searchTerm = searchField.getText().trim();
            List<Student> students;

            if (searchTerm.isEmpty()) {
                students = dao.getAllStudents();
                updateTable(students);
                statusLabel.setText("📋 Showing all students (" + students.size() + " total)");
            } else {
                students = dao.searchStudents(searchTerm);

                String message;
                if (students.isEmpty()) {
                    message = "❌ No students found matching '" + searchTerm + "'";
                    JOptionPane.showMessageDialog(this, message, "Search Results", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    message = "✅ Found " + students.size() + " student(s) matching '" + searchTerm + "'";
                    JOptionPane.showMessageDialog(this, message, "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }

                updateTable(students);
                statusLabel.setText("🔍 " + message);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching students: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshStudentList() {
        try {
            List<Student> students = dao.getAllStudents();
            updateTable(students);
            clearFields();
            searchField.setText("");
            statusLabel.setText("📋 Showing all students (" + students.size() + " total)");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] row = {student.getId(), student.getName(), student.getEmail(), student.getCourse()};
            tableModel.addRow(row);
        }
    }

    private void loadSelectedStudentToFields(int row) {
        nameField.setText((String) tableModel.getValueAt(row, 1));
        emailField.setText((String) tableModel.getValueAt(row, 2));
        courseField.setText((String) tableModel.getValueAt(row, 3));
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        courseField.setText("");
        studentTable.clearSelection();
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
    }

    private void clearSearch() {
        searchField.setText("");
        refreshStudentList();
    }
}
