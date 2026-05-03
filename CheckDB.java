import com.student.dao.StudentDAO;
import com.student.model.Student;
import java.util.List;

public class CheckDB {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        try {
            List<Student> students = dao.getAllStudents();
            System.out.println("=== CURRENT STUDENTS IN DATABASE ===");
            System.out.println("Total: " + students.size());
            for (Student s : students) {
                System.out.println("ID: " + s.getId() + " | Name: " + s.getName() + " | Email: " + s.getEmail() + " | Course: " + s.getCourse());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}