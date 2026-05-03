import com.student.dao.StudentDAO;
import com.student.model.Student;
import java.sql.SQLException;
import java.util.List;

public class SearchTest {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();

        try {
            System.out.println("=== Search Test ===\n");

            // First, show all students
            System.out.println("All students in database:");
            List<Student> allStudents = dao.getAllStudents();
            for (Student s : allStudents) {
                System.out.println("ID: " + s.getId() + ", Name: '" + s.getName() + "', Email: '" + s.getEmail() + "'");
            }
            System.out.println();

            // Test search for "John"
            System.out.println("Searching for 'John':");
            List<Student> johnResults = dao.searchStudents("John");
            System.out.println("Found " + johnResults.size() + " results:");
            for (Student s : johnResults) {
                System.out.println("  " + s.getName());
            }
            System.out.println();

            // Test search for "John Doe"
            System.out.println("Searching for 'John Doe':");
            List<Student> johnDoeResults = dao.searchStudents("John Doe");
            System.out.println("Found " + johnDoeResults.size() + " results:");
            for (Student s : johnDoeResults) {
                System.out.println("  " + s.getName());
            }
            System.out.println();

            // Test search for "Doe"
            System.out.println("Searching for 'Doe':");
            List<Student> doeResults = dao.searchStudents("Doe");
            System.out.println("Found " + doeResults.size() + " results:");
            for (Student s : doeResults) {
                System.out.println("  " + s.getName());
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}