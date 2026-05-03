import com.student.dao.DBConnection;
import com.student.view.MainUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize database schema if needed
            DBConnection.initializeDatabase();

            // Launch the GUI
            SwingUtilities.invokeLater(MainUI::new);

        } catch (Exception e) {
            System.err.println("Application startup failed: " + e.getMessage());
            System.err.println("Please check your database connection and try again.");
            System.exit(1);
        }
    }
}