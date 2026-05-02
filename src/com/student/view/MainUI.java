package com.student.view;
import com.student.dao.StudentDAO;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

public class MainUI extends JFrame {
    private final StudentDAO dao = new StudentDAO();
    private final JTextField nameF = new JTextField(10), emailF = new JTextField(10);
    private final JTextField courseF = new JTextField(10);
    private final JButton addBtn = new JButton("Add Student");

    public MainUI() {
        setLayout(new FlowLayout());
        add(new JLabel("Name:")); add(nameF);
        add(new JLabel("Email:")); add(emailF);
        add(new JLabel("Course:")); add(courseF);
        add(addBtn);
        
        addBtn.addActionListener(e -> {
            try {
                String course = courseF.getText().isEmpty() ? "Computer Science" : courseF.getText();
                dao.addStudent(nameF.getText(), emailF.getText(), course);
                JOptionPane.showMessageDialog(this, "Added!");
                nameF.setText("");
                emailF.setText("");
                courseF.setText("");
            } catch (SQLException ex) { 
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 150);
        setVisible(true);
    }
}
