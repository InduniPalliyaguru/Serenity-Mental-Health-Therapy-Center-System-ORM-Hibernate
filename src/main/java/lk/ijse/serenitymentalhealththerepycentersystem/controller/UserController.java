package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class UserController {

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<?, ?> passwordCol;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTxt;

    @FXML
    private Button updateButton;

    @FXML
    private TableColumn<?, ?> userEmailCol;

    @FXML
    private TextField userEmailTxt;

    @FXML
    private TableColumn<?, ?> userIdCol;

    @FXML
    private TextField userIdTxt;

    @FXML
    private TextField userPasswordTxt;

    @FXML
    private TableColumn<?, ?> userRoleCol;

    @FXML
    private ComboBox<?> userRoleTxt;

    @FXML
    private TableColumn<?, ?> usernameCol;

    @FXML
    private TextField usernameTxt;

    @FXML
    private TableView<?> usersTable;

    @FXML
    void btnRefresh(MouseEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }

    @FXML
    void tableClick(MouseEvent event) {

    }

    @FXML
    void update(ActionEvent event) {

    }

}
