package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.UserService;

import java.io.IOException;

public class LoginController {

    @FXML
    private PasswordField loginPasswordText;

    @FXML
    private TextField loginUsernameText;

    UserService userBO = (UserService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.USER);

    @FXML
    void loginValidate() {
        String username = loginUsernameText.getText();
        String password = loginPasswordText.getText();

        try {

            String role = userBO.authenticateUser(username, password);

            if (role != null) {
                navigateTo("/view/dashboard.fxml", role);

                new ReportsController().setUserRole(role);
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Username or Password!").show();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void navigateTo(String fxmlFile, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) loginUsernameText.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);

            DashBoardController controller = loader.getController();
            controller.setUserRole(role);

            System.out.println("User logged in with role: " + role);

            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading dashboard: " + e.getMessage());
        }
    }

}
