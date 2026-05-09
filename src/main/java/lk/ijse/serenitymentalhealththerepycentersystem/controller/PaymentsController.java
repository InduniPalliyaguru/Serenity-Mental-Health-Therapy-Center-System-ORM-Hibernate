package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PaymentDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapyProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.PaymentTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.PatientService;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.PaymentService;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapyProgramService;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentsController implements Initializable {

    private boolean fromMainPage = false;

    public void setFromMainPage(boolean fromMainPage) {
        this.fromMainPage = fromMainPage;
    }

    public void configurePage() {
        if (fromMainPage) {
            searchButton.setVisible(false);
            deleteButton.setVisible(false);
            updateButton.setVisible(false);
        }
    }


    @FXML
    private TableColumn<PaymentTM, Double> amountCol;

    @FXML
    private TextField amountTxt;

    @FXML
    private TableColumn<PaymentTM, LocalDate> dateCol;

    @FXML
    private DatePicker dateTxt;

    @FXML
    private Button deleteButton;

    @FXML
    private Button getInvoice;

    @FXML
    private TableColumn<PaymentTM, String> patientIdCol;

    @FXML
    private TextField patientIdTxt;

    @FXML
    private TextField patientNameTxt;

    @FXML
    private Button patientSearchButton;

    @FXML
    private TableColumn<PaymentTM, String> paymentIdCol;

    @FXML
    private TextField paymentIdTxt;

    @FXML
    private ComboBox<String> paymentTypeChoice;

    @FXML
    private TableView<PaymentTM> paymentsTable;

    @FXML
    private TableColumn<PaymentTM, String> programIdCol;

    @FXML
    private TextField programIdTxt;

    @FXML
    private TextField programNameTxt;

    @FXML
    private Button programSearchButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableColumn<PaymentTM, String> sessionIdCol;

    @FXML
    private TextField sessionIdTxt;

    @FXML
    private Label sessionIdPart1;

    @FXML
    private Button updateButton;

    PatientService patientService = (PatientService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);
    TherapyProgramService therapyProgramService = (TherapyProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPY_PROGRAM);
    PaymentService payService = (PaymentService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PAYMENT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("therapyProgramId"));
        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("therapySessionId"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        refreshPage();

        paymentTypeChoice.setItems(FXCollections.observableArrayList("Program Register", "Session Payment"));
        paymentTypeChoice.setValue("Program Register");
        sessionIdPart1.setVisible(false);
        sessionIdTxt.setVisible(false);
        paymentTypeChoice.setOnAction(e -> toggleSessionField());

    }

    private void toggleSessionField() {
        sessionIdPart1.setVisible(!"Program Register".equals(paymentTypeChoice.getValue()));
        sessionIdTxt.setVisible(!"Program Register".equals(paymentTypeChoice.getValue()));
    }

    private void refreshPage() {
        clearFields();
        refreshTable();
        paymentIdTxt.setText(payService.getNextPaymentPK());
    }

    private void refreshTable() {
        ArrayList<PaymentDTO> paymentList = payService.getAllPayments();
        ObservableList<PaymentTM> payments = FXCollections.observableArrayList();

        for (PaymentDTO dto : paymentList) {
            String session = null;
            if (dto.getTherapySession() != null) {
                session = dto.getTherapySession().getSession_id();
            }
            payments.add(new PaymentTM(
                    dto.getPaymentId(),
                    dto.getPatient().getPatient_id(),
                    dto.getTherapyProgram().getProgramId(),
                    session,
                    dto.getAmount(),
                    dto.getPaymentDate()
            ));
        }

        paymentsTable.setItems(payments);
    }

    private void clearFields() {
        paymentIdTxt.clear();
        patientIdTxt.clear();
        patientNameTxt.clear();
        programIdTxt.clear();
        programNameTxt.clear();
        sessionIdTxt.clear();
        amountTxt.clear();
        dateTxt.setValue(null);
    }

    @FXML
    void btnRefresh(MouseEvent event) {
        refreshPage();
    }

    @FXML
    void delete(ActionEvent event) {
        String id = paymentIdTxt.getText();

        if (id == null || id.trim().isEmpty()) {
            showAlert("Error", "Please enter a Payment ID to delete.", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete Payment ID: " + id + "?");

        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnYes, btnNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == btnYes) {
            try {
                if (payService.deletePayment(id)) {
                    showAlert("Success", "Payment deleted successfully.", Alert.AlertType.INFORMATION);
                    refreshPage();
                } else {
                    showAlert("Error", "Delete failed. Please try again.", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                showAlert("Error", "Something went wrong!", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    void getInvoiceOnAction(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        PaymentDTO dto = payService.constructPaymentDto(
                paymentIdTxt.getText(),
                patientIdTxt.getText(),
                programIdTxt.getText(),
                sessionIdTxt.getText(),
                new BigDecimal(amountTxt.getText()),
                dateTxt.getValue()
        );

        if (payService.savePayment(dto)) {
            showAlert("Success", "Payment saved successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to save payment", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void search(ActionEvent event) {
        String query = searchTxt.getText();
        if (query.isEmpty()) {
            showAlert("Error", "Please enter a search term", Alert.AlertType.WARNING);
            refreshPage();
            return;
        }
        ArrayList<PaymentDTO> results = payService.searchByPatientName(query);

        ObservableList<PaymentTM> payments = FXCollections.observableArrayList();

        for (PaymentDTO dto : results) {
            String sessionId = null;
            if (dto.getTherapySession() != null) {
                sessionId = dto.getTherapySession().getSession_id();
            }

            payments.add(new PaymentTM(
                    true,
                    dto.getPaymentId(),
                    dto.getPatient().getPatient_id(),
                    dto.getTherapyProgram().getProgramId(),
                    sessionId,
                    dto.getAmount(),
                    dto.getPaymentDate()
            ));
        }

        paymentsTable.setItems(payments);
    }

    @FXML
    void searchPatient(ActionEvent event) {
        String name = patientNameTxt.getText().trim();
        ArrayList<PatientDTO> patients = patientService.findByPatientName(name);

        if (patients.isEmpty()) {
            showAlert("Not Found", "Patient not found", Alert.AlertType.WARNING);
            return;
        }

        PatientDTO patient = patients.getFirst();

        patientIdTxt.setText(patient.getPatientId());
        patientNameTxt.setText(patient.getName());
    }

    @FXML
    void searchProgram(ActionEvent event) {
        String name = programNameTxt.getText().trim();

        try {
            TherapyProgramDTO programs = therapyProgramService.findByName(name);

            if (programs == null) {
                showAlert("Not Found", "Program not found", Alert.AlertType.WARNING);
                return;
            }


            programIdTxt.setText(programs.getProgramId());
            programNameTxt.setText(programs.getProgramName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void update(ActionEvent event) {
        PaymentDTO dto = payService.constructPaymentDto(
                paymentIdTxt.getText(),
                patientIdTxt.getText(),
                programIdTxt.getText(),
                sessionIdTxt.getText(),
                new BigDecimal(amountTxt.getText()),
                dateTxt.getValue()
        );

        if (payService.updatePayment(dto)) {
            showAlert("Success", "Payment updated successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to update payment", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void tableClick(MouseEvent event) {
        PaymentTM selected = paymentsTable.getSelectionModel().getSelectedItem();
        try {
            if (selected != null) {
                paymentIdTxt.setText(selected.getPaymentId());
                patientIdTxt.setText(selected.getPatientId());
                patientNameTxt.setText(patientService.findPatientByID(selected.getPatientId()).getName());
                programIdTxt.setText(selected.getTherapyProgramId());
                programNameTxt.setText(therapyProgramService.findByName(selected.getTherapyProgramId()).getProgramName());
                sessionIdTxt.setText(selected.getTherapySessionId());
                amountTxt.setText(String.valueOf(selected.getAmount()));
                dateTxt.setValue(selected.getPaymentDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
