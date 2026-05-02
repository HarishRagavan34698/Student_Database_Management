package com.student.dao;
import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        return DriverManager.getConnection(url, "app_dev", "DevPass123");
    }
}
