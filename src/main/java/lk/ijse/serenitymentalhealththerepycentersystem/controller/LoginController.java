package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginController {

    @FXML
    private AnchorPane baseAnchorPane;

    @FXML
    private Hyperlink lblFogotPassword;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField loginPasswordText;

    @FXML
    private TextField loginUsernameText;

    @FXML
    void loginValidate(ActionEvent event) {

    }

}
