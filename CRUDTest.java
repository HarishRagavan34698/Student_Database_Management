import com.student.dao.StudentDAO;
import com.student.model.Student;
import java.sql.SQLException;
import java.util.List;

public class CRUDTest {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();

        try {
            System.out.println("=== CRUD Operations Test ===\n");

            // Test CREATE (Add students)
            System.out.println("1. Testing CREATE operations:");
            dao.addStudent("Test Student 1", "test1@email.com", "Computer Science");
            dao.addStudent("Test Student 2", "test2@email.com", "Information Technology");
            dao.addStudent("Test Student 3", "test3@email.com", "Business Administration");
            System.out.println("✓ Added 3 test students\n");

            // Test READ (Get all students)
            System.out.println("2. Testing READ operations:");
            List<Student> allStudents = dao.getAllStudents();
            System.out.println("Total students: " + allStudents.size());
            System.out.println("Students list:");
            for (Student s : allStudents) {
                System.out.println("  ID: " + s.getId() + ", Name: " + s.getName() +
                                 ", Email: " + s.getEmail() + ", Course: " + s.getCourse());
            }
            System.out.println();

            // Test READ (Get student by ID)
            System.out.println("3. Testing READ by ID:");
            if (!allStudents.isEmpty()) {
                Student firstStudent = dao.getStudentById(allStudents.get(0).getId());
                if (firstStudent != null) {
                    System.out.println("✓ Found student by ID: " + firstStudent.getName());
                } else {
                    System.out.println("✗ Student not found by ID");
                }
            }
            System.out.println();

            // Test UPDATE
            System.out.println("4. Testing UPDATE operations:");
            if (!allStudents.isEmpty()) {
                Student studentToUpdate = allStudents.get(allStudents.size() - 1); // Last student
                System.out.println("Updating student: " + studentToUpdate.getName());
                boolean updateSuccess = dao.updateStudent(studentToUpdate.getId(),
                    "Updated " + studentToUpdate.getName(), studentToUpdate.getEmail(), "Mathematics");
                if (updateSuccess) {
                    System.out.println("✓ Student updated successfully");
                } else {
                    System.out.println("✗ Student update failed");
                }
            }
            System.out.println();

            // Test SEARCH
            System.out.println("5. Testing SEARCH operations:");
            List<Student> searchResults = dao.searchStudents("Test");
            System.out.println("Search results for 'Test': " + searchResults.size() + " students");
            for (Student s : searchResults) {
                System.out.println("  Found: " + s.getName());
            }
            System.out.println();

            // Test DELETE
            System.out.println("6. Testing DELETE operations:");
            if (!allStudents.isEmpty()) {
                // Find a test student to delete
                Student studentToDelete = null;
                for (Student s : allStudents) {
                    if (s.getName().startsWith("Test Student")) {
                        studentToDelete = s;
                        break;
                    }
                }

                if (studentToDelete != null) {
                    System.out.println("Deleting student: " + studentToDelete.getName());
                    boolean deleteSuccess = dao.deleteStudent(studentToDelete.getId());
                    if (deleteSuccess) {
                        System.out.println("✓ Student deleted successfully");
                    } else {
                        System.out.println("✗ Student deletion failed");
                    }
                } else {
                    System.out.println("No test student found to delete");
                }
            }
            System.out.println();

            // Final count
            System.out.println("7. Final student count:");
            int finalCount = dao.getStudentCount();
            System.out.println("Total students in database: " + finalCount);

            System.out.println("\n=== All CRUD Tests Completed ===");

        } catch (SQLException e) {
            System.err.println("Database error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}