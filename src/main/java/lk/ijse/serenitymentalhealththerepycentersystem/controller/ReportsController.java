package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.HistoryDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PaymentDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.HistoryTM;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.PaymentTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.ReportService;

import java.util.List;
import java.util.Map;

public class ReportsController {

    @FXML
    private BarChart<String, Number> barChartPerformance;
    @FXML
    private PieChart pieChartPrograms;
    @FXML
    private CategoryAxis xAxisTherapists;

    @FXML
    private DatePicker dpFromDate;
    @FXML
    private DatePicker dpToDate;
    @FXML
    private TextField txtSearchPatientId;

    @FXML
    private TableView<PaymentTM> tblPayments;
    @FXML
    private TableColumn<PaymentTM, String> colPayId;
    @FXML
    private TableColumn<PaymentTM, String> colPatientName;
    @FXML
    private TableColumn<PaymentTM, Double> colAmountPaid;

    @FXML
    private TableView<HistoryTM> tblHistory;
    @FXML
    private TableColumn<HistoryTM, String> colDate;
    @FXML
    private TableColumn<HistoryTM, String> colProgram;
    @FXML
    private TableColumn<HistoryTM, String> colTherapist;
    @FXML
    private TableColumn<HistoryTM, String> colHistoryStatus;
    @FXML
    private TableColumn<HistoryTM, String> colNotes;
    @FXML
    private Tab tabPerformance;

    private static String userRole;

    private final ReportService reportService = (ReportService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.REPORT);

    public void initialize() {
        setCellValueFactories();
        loadCharts();
        loadFinancialTable();

        if ("Receptionist".equalsIgnoreCase(userRole)) {
            tabPerformance.setDisable(true);
            System.out.println("Receptionist logged in: Performance tab restricted.");
        }

    }

    public void setUserRole(String role) {
        userRole = role;
        System.out.println("User role set in ReportsController: " + userRole);
    }

    private void setCellValueFactories() {
        colPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colAmountPaid.setCellValueFactory(new PropertyValueFactory<>("amount"));

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colHistoryStatus.setCellValueFactory(new PropertyValueFactory<>("historyStatus"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    private void loadCharts() {
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Map<String, Integer> performance = reportService.getTherapistPerformance();
            performance.forEach((name, count) -> series.getData().add(new XYChart.Data<>(name, count)));
            barChartPerformance.getData().add(series);

            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            Map<String, Integer> popularity = reportService.getProgramPopularity();
            popularity.forEach((name, count) -> pieData.add(new PieChart.Data(name, count)));
            pieChartPrograms.setData(pieData);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load charts!").show();
        }
    }

    private void loadFinancialTable() {
        try {
            List<PaymentDTO> dtoList = reportService.getAllFinancialData();
            ObservableList<PaymentTM> obList = FXCollections.observableArrayList();

            for (PaymentDTO dto : dtoList) {
                String patientName = (dto.getPatient() != null) ? dto.getPatient().getName() : "Unknown";

                obList.add(new PaymentTM(
                        dto.getPaymentId(),
                        patientName,
                        "N/A",
                        "N/A",
                        dto.getAmount(),
                        dto.getPaymentDate()
                ));
            }
            tblPayments.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSearchHistoryOnAction(ActionEvent event) {
        String patientId = txtSearchPatientId.getText();
        if (patientId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid Patient ID!").show();
            return;
        }

        try {
            List<HistoryDTO> dtoList = reportService.getPatientHistory(patientId);
            ObservableList<HistoryTM> obList = FXCollections.observableArrayList();
            for (HistoryDTO dto : dtoList) {
                obList.add(new HistoryTM(dto.getDate(), dto.getProgramName(), dto.getTherapistName(), dto.getHistoryStatus(), dto.getNotes()));
            }
            tblHistory.setItems(obList);

            if (obList.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No history found for this Patient.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load patient history!").show();
        }
    }

    @FXML
    void btnGenerateFinancialReportOnAction(ActionEvent event) {
        if (dpFromDate.getValue() == null || dpToDate.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select both dates!").show();
            return;
        }
        System.out.println("Jasper Report Generating...");
    }
}