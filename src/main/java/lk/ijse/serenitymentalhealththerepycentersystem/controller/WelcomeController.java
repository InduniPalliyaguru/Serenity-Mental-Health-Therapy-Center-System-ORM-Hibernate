package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lk.ijse.serenitymentalhealththerepycentersystem.util.NavigationUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private Label lblActiveTherapist;

    @FXML
    private Label lblDailySession;

    @FXML
    private Label lblMonthlyIncome;

    @FXML
    private Label lblTotalPatient;

    private final NavigationUtil navigate = new NavigationUtil();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void addPatientOnClicked(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/patientPage.fxml");
    }

    @FXML
    void exportReportOnClicked(MouseEvent event) {

    }

    @FXML
    void processPaymentOnClicked(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/payment.fxml");
    }

    @FXML
    void sessionsOnClicked(MouseEvent event) {
        navigate.navigateTo(bodyPane, "/view/therapySession.fxml");
    }

}
