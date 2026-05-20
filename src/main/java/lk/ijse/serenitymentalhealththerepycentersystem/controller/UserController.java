package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.UserDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.UserTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.UserService;
import lk.ijse.serenitymentalhealththerepycentersystem.util.ValidateUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private TableColumn<UserTM, String> passwordCol;
    @FXML
    private TextField searchTxt;
    @FXML
    private TableColumn<UserTM, String> userEmailCol;
    @FXML
    private TextField userEmailTxt;
    @FXML
    private TableColumn<UserTM, String> userIdCol;
    @FXML
    private TextField userIdTxt;
    @FXML
    private TextField userPasswordTxt;
    @FXML
    private TableColumn<UserTM, String> userRoleCol;
    @FXML
    private ComboBox<String> userRoleTxt;
    @FXML
    private TableColumn<UserTM, String> usernameCol;
    @FXML
    private TextField usernameTxt;
    @FXML
    private TableView<UserTM> usersTable;

    UserService userService = (UserService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.USER);
    private final ObservableList<UserTM> userTMList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userRoleTxt.setItems(FXCollections.observableArrayList("Admin", "Receptionist"));

        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadAllUsers();
    }

    private void loadAllUsers() {
        userTMList.clear();
        for (UserDTO dto : userService.getAllUsers()) {
            userTMList.add(new UserTM(
                    dto.getUser_id(),
                    dto.getUsername(),
                    dto.getPassword(),
                    dto.getEmail(),
                    dto.getRole()

            ));
        }
        usersTable.setItems(userTMList);
    }

    @FXML
    void btnRefresh() {
        clearForm();
        loadAllUsers();
    }

    @FXML
    void delete() {
        if (userIdTxt.getText() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete").show();
            return;
        }

        boolean isDeleted = userService.deleteUser(userIdTxt.getText());
        if (isDeleted) {
            loadAllUsers();
            clearForm();
            new Alert(Alert.AlertType.INFORMATION, "User deleted!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to delete user").show();
        }
    }

    @FXML
    void save() {
        String id = userService.generateNextUserId();
        String username = usernameTxt.getText();
        String email = userEmailTxt.getText();
        String role = userRoleTxt.getValue();
        String password = userPasswordTxt.getText();

        if (!ValidateUtil.areRequiredFields(username, email, password)) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all required fields").show();
            return;
        }

        if (!ValidateUtil.isValidEmail(email)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid email address").show();
            return;
        }

        if (role == null) {
            new Alert(Alert.AlertType.ERROR, "Please select a role").show();
            return;
        }

        boolean isSaved = userService.registerUser(new UserDTO(id, username, password, email, role));
        if (isSaved) {
            loadAllUsers();
            clearForm();
            new Alert(Alert.AlertType.INFORMATION, "User saved!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save user").show();
        }
    }

    @FXML
    void search() {
        String name = searchTxt.getText();
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a username to search").show();
            loadAllUsers();
            clearForm();
            return;
        }

        userTMList.clear();

        for (UserDTO dto : userService.searchUser(name)) {
            userTMList.add(new UserTM(
                    dto.getUser_id(),
                    dto.getUsername(),
                    dto.getPassword(),
                    dto.getEmail(),
                    dto.getRole()
            ));
        }
        usersTable.setItems(userTMList);

        if (userTMList.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No users found for that username").show();
        }
    }

    @FXML
    void tableClick() {
        UserTM selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            userIdTxt.setText(selected.getUserId());
            usernameTxt.setText(selected.getUsername());
            userEmailTxt.setText(selected.getEmail());
            userPasswordTxt.setText(selected.getPassword());
            userRoleTxt.setValue(selected.getRole());
        }
    }

    @FXML
    void update() {
        if (userIdTxt.getText() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user from the table").show();
            return;
        }

        String userId = userIdTxt.getText();
        String username = usernameTxt.getText();
        String password = userPasswordTxt.getText();
        String email = userEmailTxt.getText();
        String role = userRoleTxt.getValue();

        if (!ValidateUtil.areRequiredFields(username, email, password)) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all required fields").show();
            return;
        }

        if (!ValidateUtil.isValidEmail(email)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid email address").show();
            return;
        }

        if (role == null) {
            new Alert(Alert.AlertType.ERROR, "Please select a role").show();
            return;
        }

        boolean isUpdated = userService.updateUser(new UserDTO(userId, username, password, email, role));

        if (isUpdated) {
            loadAllUsers();
            clearForm();
            new Alert(Alert.AlertType.INFORMATION, "User updated!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update user").show();
        }
    }

    private void setNextUserId() {
        String nextId = userService.generateNextUserId();
        userIdTxt.setText(nextId);
    }

    private void clearForm() {
        setNextUserId();
        usernameTxt.clear();
        userEmailTxt.clear();
        userPasswordTxt.clear();
        userRoleTxt.getSelectionModel().clearSelection();
    }

}
