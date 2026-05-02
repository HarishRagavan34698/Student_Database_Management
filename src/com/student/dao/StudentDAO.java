package com.student.dao;
import com.student.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public void addStudent(String name, String email, String course) throws SQLException {
        String sql = "INSERT INTO Students VALUES (student_seq.NEXTVAL, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, email); ps.setString(3, course);
            ps.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Students")) {
            while (rs.next()) {
                list.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        }
        return list;
    }
}
