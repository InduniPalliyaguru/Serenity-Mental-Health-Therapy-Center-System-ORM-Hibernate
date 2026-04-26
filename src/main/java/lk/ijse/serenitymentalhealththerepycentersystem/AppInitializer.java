package lk.ijse.serenitymentalhealththerepycentersystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.UserDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl.UserDAOImpl;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;
import lk.ijse.serenitymentalhealththerepycentersystem.util.PasswordUtil;

import java.io.IOException;

public class AppInitializer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeDefaultUser();

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

    public static void main(String[] args) {
        launch(args);
    }
}