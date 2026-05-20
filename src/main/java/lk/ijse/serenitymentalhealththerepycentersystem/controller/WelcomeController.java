package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.DashboardDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.DashboardService;
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
    @FXML
    private VBox vBoxRecentActivity;

    private final NavigationUtil navigate = new NavigationUtil();
    private final DashboardService dashboardBO = (DashboardService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.DASHBOARD);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashboardDetails();
    }

    @FXML
    void addPatientOnClicked() {
        navigate.navigateTo(bodyPane, "/view/patientPage.fxml");
    }

    @FXML
    void exportReportOnClicked() {
        navigate.navigateTo(bodyPane, "/view/reportsView.fxml");
    }

    @FXML
    void processPaymentOnClicked() {
        navigate.navigateTo(bodyPane, "/view/payment.fxml");
    }

    @FXML
    void sessionsOnClicked() {
        navigate.navigateTo(bodyPane, "/view/therapySession.fxml");
    }

    private void loadDashboardDetails() {
        try {
            DashboardDTO dto = dashboardBO.getDashboardData();

            lblTotalPatient.setText(String.valueOf(dto.getTotalPatients()));
            lblDailySession.setText(String.valueOf(dto.getDailySessions()));
            lblActiveTherapist.setText(String.valueOf(dto.getActiveTherapists()));

            lblMonthlyIncome.setText(String.format("%,.2f", dto.getMonthlyRevenue()));

            if (vBoxRecentActivity != null) {
                vBoxRecentActivity.getChildren().clear();
                vBoxRecentActivity.setSpacing(10);

                for (String activityText : dto.getRecentActivities()) {
                    Label lblActivity = new Label(activityText);
                    lblActivity.setStyle("-fx-font-size: 13px; -fx-text-fill: #475569; -fx-padding: 5;");
                    vBoxRecentActivity.getChildren().add(lblActivity);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}