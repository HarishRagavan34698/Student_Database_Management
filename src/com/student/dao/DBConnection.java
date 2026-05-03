package com.student.dao;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        return DriverManager.getConnection(url, "app_dev", "DevPass123");
    }

    // Initialize database schema if tables don't exist
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT COUNT(*) FROM user_tables WHERE table_name = 'STUDENTS'")) {

            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Database not initialized. Creating schema...");

                Path schemaPath = resolveSchemaPath();
                if (Files.exists(schemaPath)) {
                    executeSchemaFile(conn, schemaPath.toString());
                    System.out.println("Database schema created successfully!");
                } else {
                    createBasicSchema(conn);
                    System.out.println("Basic database schema created!");
                }
            } else {
                System.out.println("Database already initialized.");
            }

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    /** Locate schema next to cwd or any ancestor (handles IDE/run configs with varying working directories). */
    private static Path resolveSchemaPath() {
        Path relative = Paths.get("database_schema.sql");
        if (Files.exists(relative)) {
            return relative.toAbsolutePath().normalize();
        }
        Path dir = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        for (int i = 0; i < 8; i++) {
            Path candidate = dir.resolve("database_schema.sql");
            if (Files.exists(candidate)) {
                return candidate.normalize();
            }
            Path parent = dir.getParent();
            if (parent == null) {
                break;
            }
            dir = parent;
        }
        return Paths.get(System.getProperty("user.dir", ".")).resolve("database_schema.sql").normalize();
    }

    private static void executeSchemaFile(Connection conn, String filePath) throws SQLException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] statements = content.split(";");

            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty() && !statement.startsWith("--")) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(statement);
                    }
                }
            }
        } catch (IOException e) {
            throw new SQLException("Failed to read schema file: " + e.getMessage(), e);
        }
    }

    private static void createBasicSchema(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Create Students table
            stmt.execute(
                "CREATE TABLE Students (" +
                "id NUMBER PRIMARY KEY, " +
                "name VARCHAR2(100) NOT NULL, " +
                "email VARCHAR2(100) UNIQUE NOT NULL, " +
                "course VARCHAR2(100) NOT NULL)"
            );

            // Create sequence
            stmt.execute("CREATE SEQUENCE student_seq START WITH 1 INCREMENT BY 1");

            // Insert sample data
            stmt.execute("INSERT INTO Students VALUES (student_seq.NEXTVAL, 'John Doe', 'john.doe@email.com', 'Computer Science')");
            stmt.execute("INSERT INTO Students VALUES (student_seq.NEXTVAL, 'Jane Smith', 'jane.smith@email.com', 'Information Technology')");

            conn.commit();
        }
    }
}
