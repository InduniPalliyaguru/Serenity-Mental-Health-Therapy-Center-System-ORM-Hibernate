package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lk.ijse.serenitymentalhealththerepycentersystem.util.NavigationUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML
    private StackPane bodyPane;

    @FXML
    private AnchorPane baseAnchorPane;

    @FXML
    private Label lblRole;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button patientProgramPageBtn;

    @FXML
    private Button patientsPageBtn;

    @FXML
    private Button paymentsPageBtn;

    @FXML
    private Button therapistsPageBtn;

    @FXML
    private Button therapyProgramPageBtn;

    @FXML
    private Button therapySessionPageBtn;

    @FXML
    private TextField txtDate;

    @FXML
    private Button usersPageBtn;

    @FXML
    private ImageView welcomePageBtn;

    private Button currentActiveButton;

    private final NavigationUtil navigate = new NavigationUtil();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navigate.navigateTo(bodyPane, "/view/welcome.fxml");
    }

    private void setActiveStyle(Button clickedButton) {

        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("sidebar-button-active");
        }

        clickedButton.getStyleClass().add("sidebar-button-active");

        currentActiveButton = clickedButton;
    }

    @FXML
    void loadPatientProgramPage(MouseEvent event) {
        setActiveStyle(patientProgramPageBtn);
        navigate.navigateTo(bodyPane, "/view/patientProgram.fxml");
    }

    @FXML
    void loadPatientsPage(MouseEvent event) {
        setActiveStyle(patientsPageBtn);
        navigate.navigateTo(bodyPane, "/view/patientPage.fxml");
    }

    @FXML
    void loadPaymentsPageBtn(MouseEvent event) {
        setActiveStyle(paymentsPageBtn);
        navigate.navigateTo(bodyPane, "/view/payment.fxml");
    }

    @FXML
    void loadTherapistsPage(MouseEvent event) {
        setActiveStyle(therapistsPageBtn);
        navigate.navigateTo(bodyPane, "/view/therapist.fxml");
    }

    @FXML
    void loadTherapyProgramsPage(MouseEvent event) {
        setActiveStyle(therapyProgramPageBtn);
        navigate.navigateTo(bodyPane, "/view/therapyProgram.fxml");
    }

    @FXML
    void loadTherapySessionsPage(MouseEvent event) {
        setActiveStyle(therapySessionPageBtn);
        navigate.navigateTo(bodyPane, "/view/therapySession.fxml");
    }

    @FXML
    void loadUsersPage(MouseEvent event) {
        setActiveStyle(usersPageBtn);
        navigate.navigateTo(bodyPane, "/view/userManage.fxml");
    }

    @FXML
    void loadWelcomePage(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/welcome.fxml");
    }

    @FXML
    void logOut(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Logout Confirmation");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                navigate.navigateBack(baseAnchorPane, "/view/login.fxml");
            }
        });
    }

    public void setUserRole(String role) {
        boolean isAdmin = "Admin".equalsIgnoreCase(role);

        if (isAdmin) {
            therapistsPageBtn.setVisible(true);
            therapyProgramPageBtn.setVisible(true);
            usersPageBtn.setVisible(true);
        } else {
            therapistsPageBtn.setVisible(false);
            therapyProgramPageBtn.setVisible(false);
            usersPageBtn.setVisible(false);
        }


        System.out.println("Admin access: " + isAdmin);
    }

}
