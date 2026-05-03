package com.student.dao;
import com.student.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // CREATE - Add new student
    public void addStudent(String name, String email, String course) throws SQLException {
        String sql = "INSERT INTO Students (id, name, email, course) VALUES (student_seq.NEXTVAL, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, course);
            ps.executeUpdate();
        }
    }

    // READ - Get all students
    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, email, course FROM Students ORDER BY id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        }
        return list;
    }

    // READ - Get student by ID
    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT id, name, email, course FROM Students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                }
            }
        }
        return null;
    }

    // UPDATE - Update student information
    public boolean updateStudent(int id, String name, String email, String course) throws SQLException {
        String sql = "UPDATE Students SET name = ?, email = ?, course = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, course);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE - Delete student by ID
    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM Students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // SEARCH - Find students by name or email
    public List<Student> searchStudents(String searchTerm) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, email, course FROM Students " +
                    "WHERE LOWER(name) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) ORDER BY id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }
            }
        }
        return list;
    }

    // UTILITY - Check if email exists (for validation)
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Students WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // UTILITY - Get total student count
    public int getStudentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
