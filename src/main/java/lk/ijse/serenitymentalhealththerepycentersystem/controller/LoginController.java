package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.serenitymentalhealththerepycentersystem.service.BOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.UserBO;

import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane baseAnchorPane;

    @FXML
    private Hyperlink lblForgotPassword;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField loginPasswordText;

    @FXML
    private TextField loginUsernameText;

    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    @FXML
    void loginValidate(ActionEvent event) {
        String username = loginUsernameText.getText();
        String password = loginPasswordText.getText();

        try {

            String role = userBO.authenticateUser(username, password);

            if (role != null) {
                    navigateTo("/view/dashboard.fxml", role);
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Username or Password!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateTo(String fxmlFile, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) loginUsernameText.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);

            // Pass the role to the dashboard controller
            DashBoardController controller = loader.getController();
            controller.setUserRole(role);

            // Debug message to verify role
            System.out.println("User logged in with role: " + role);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
