package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.serenitymentalhealththerepycentersystem.exception.LoginException;
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
        try {
            checkEmptyFields();

            String username = loginUsernameText.getText();
            String password = loginPasswordText.getText();

            String role = userBO.authenticateUser(username, password);

            if (role != null) {
                navigateTo("/view/dashboard.fxml", role);
                new ReportsController().setUserRole(role);
            }

        } catch (LoginException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
        }
    }

    private void checkEmptyFields() {
        if (loginUsernameText.getText() == null || loginUsernameText.getText().trim().isEmpty()) {
            throw new LoginException("Login Failed: Username field cannot be empty!");
        }
        if (loginPasswordText.getText() == null || loginPasswordText.getText().trim().isEmpty()) {
            throw new LoginException("Login Failed: Password field cannot be empty!");
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
