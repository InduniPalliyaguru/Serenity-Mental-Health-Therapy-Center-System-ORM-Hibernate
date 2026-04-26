package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class DashBoardController {

    @FXML
    private StackPane bodyPane;

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

    private void setActiveStyle(Button clickedButton) {
        // 1. කලින් Active වෙලා තිබුණු බොත්තමෙන් Style එක ඉවත් කරන්න
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("sidebar-button-active");
        }

        // 2. අලුත් බොත්තමට Style එක එකතු කරන්න
        clickedButton.getStyleClass().add("sidebar-button-active");

        // 3. දැන් මේ බොත්තම Active ලෙස මතක තබා ගන්න
        currentActiveButton = clickedButton;
    }

    @FXML
    void loadPatientProgramPage(MouseEvent event) {
        setActiveStyle(patientProgramPageBtn);
    }

    @FXML
    void loadPatientsPage(MouseEvent event) {
        setActiveStyle(patientsPageBtn);
    }

    @FXML
    void loadPaymentsPageBtn(MouseEvent event) {
        setActiveStyle(paymentsPageBtn);
    }

    @FXML
    void loadTherapistsPage(MouseEvent event) {
        setActiveStyle(therapistsPageBtn);
    }

    @FXML
    void loadTherapyProgramsPage(MouseEvent event) {
        setActiveStyle(therapyProgramPageBtn);
    }

    @FXML
    void loadTherapySessionsPage(MouseEvent event) {
        setActiveStyle(therapySessionPageBtn);
    }

    @FXML
    void loadUsersPage(MouseEvent event) {
        setActiveStyle(usersPageBtn);
    }

    @FXML
    void loadWelcomePage(MouseEvent event) {

    }

    @FXML
    void logOut(MouseEvent event) {

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
//        System.out.println("Admin buttons visible: " + adminOnlyButtonBox.isVisible());
//        System.out.println("Common buttons visible: " + commonButtonBox.isVisible());
    }

}
