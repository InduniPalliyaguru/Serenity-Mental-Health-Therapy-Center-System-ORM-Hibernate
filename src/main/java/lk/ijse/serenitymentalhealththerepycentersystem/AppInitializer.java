package lk.ijse.serenitymentalhealththerepycentersystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.UserDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl.UserDAOImpl;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapyProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;
import lk.ijse.serenitymentalhealththerepycentersystem.util.PasswordUtil;
import org.hibernate.Session;

public class AppInitializer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeDefaultUser();
        checkCache();

        Parent root = FXMLLoader.load(getClass().getResource("/view/DashBoard.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void initializeDefaultUser() {
        UserDAO userDAO = new UserDAOImpl();

        if (userDAO.findByUsername("admin") == null) {

            String hashedPassword = PasswordUtil.hashPassword("admin123");

            User defaultAdmin = new User(
                    "U001",
                    "admin",
                    hashedPassword,
                    "admin@serenity.lk",
                    "Admin"
            );

            boolean isSaved = userDAO.save(defaultAdmin);

            if (isSaved) {
                System.out.println("Default Admin User created successfully!");
            }
        }
    }

    private void checkCache() {
        System.out.println("--- FIRST SESSION START ---");
        Session session1 = FactoryConfiguration.getInstance().getSession();

        TherapyProgram prog1 = session1.get(TherapyProgram.class, "MT1001");
        System.out.println("Loaded: " + prog1.getProgramName());

        session1.close();
        System.out.println("--- FIRST SESSION CLOSED ---\n");

        System.out.println("--- SECOND SESSION START ---");
        Session session2 = FactoryConfiguration.getInstance().getSession();

        TherapyProgram prog2 = session2.get(TherapyProgram.class, "MT1001");
        System.out.println("Loaded: " + prog2.getProgramName());

        session2.close();
        System.out.println("--- SECOND SESSION CLOSED ---");
    }

    public static void main(String[] args) {
        launch(args);
    }
}