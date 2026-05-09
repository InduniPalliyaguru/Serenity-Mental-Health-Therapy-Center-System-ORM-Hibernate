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
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapyProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.PatientProgramTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.PatientProgramService;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapyProgramService;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientProgramController implements Initializable {

    @FXML
    private Button btnCheckPatient;

    @FXML
    private Button btnCheckProgram;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<PatientProgramTM, Double> leftToPayCol;

    @FXML
    private Label leftToPayTxt;

    @FXML
    private TableColumn<PatientProgramTM, String> patientIdCol;

    @FXML
    private TextField patientIdTxt;

    @FXML
    private TableColumn<PatientProgramTM, String> patientNameCol;

    @FXML
    private TextField patientNameTxt;

    @FXML
    private TableView<PatientProgramTM> patientProgramTable;

    @FXML
    private TableColumn<PatientProgramTM, String> paymentIdCol;

    @FXML
    private TextField paymentIdTxt;

    @FXML
    private TableColumn<PatientProgramTM, Double> programFeeCol;

    @FXML
    private Label programFeeTxt;

    @FXML
    private TableColumn<PatientProgramTM, String> programIdCol;

    @FXML
    private TextField programIdTxt;

    @FXML
    private TextField programNameTxt;

    @FXML
    private TableColumn<PatientProgramTM, LocalDate> registerDateCol;

    @FXML
    private DatePicker registerDateTxt;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    @FXML
    private ToggleButton searchToggleButton;

    @FXML
    private TextField searchTxt;

    @FXML
    private Button updateButton;

    PatientProgramService patientProgramService = (PatientProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT_PROGRAM);
    TherapyProgramService therapyProgramService = (TherapyProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPY_PROGRAM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("programId"));
        registerDateCol.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        programFeeCol.setCellValueFactory(new PropertyValueFactory<>("programFee"));
        leftToPayCol.setCellValueFactory(new PropertyValueFactory<>("leftToPay"));
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        refreshTable();

    }

    @FXML
    void btnRefresh(MouseEvent event) {
        refreshPage();
    }

    @FXML
    void delete(ActionEvent event) {
        String patientName = patientNameTxt.getText();
        String programName = programNameTxt.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Confirm Program Deletion");
        alert.setContentText("Are you sure you want to delete the program '" + programName +
                "' for patient '" + patientName + "'?");

        ButtonType btnYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(btnYes, btnNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == btnYes) {
            try {
                if (patientProgramService.deletePatientProgram(patientName, programName)) {
                    showAlert("Success", "Program deleted successfully", Alert.AlertType.INFORMATION);
                    refreshPage();
                } else {
                    showAlert("Error", "Failed to delete program", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Deletion cancelled by user.");
        }
    }

    @FXML
    void save(ActionEvent event) {
        PatientProgramDTO dto = new PatientProgramDTO(
                patientIdTxt.getText(),
                patientNameTxt.getText(),
                programIdTxt.getText(),
                programNameTxt.getText(),
                registerDateTxt.getValue(),
                paymentIdTxt.getText(),
                null
        );

        if (patientProgramService.savePatientProgram(dto)) {
            showAlert("Success", "Program saved successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to save program", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void search(ActionEvent event) {
        String query = searchTxt.getText().trim();

        if (query.isEmpty()) {
            showAlert("Input Error", "Please enter a search term", Alert.AlertType.WARNING);
            refreshPage();
            return;
        }

        ObservableList<PatientProgramTM> programTMS = FXCollections.observableArrayList();
        try {
            if (searchToggleButton.isSelected()) {
                ArrayList<PatientProgramDTO> programList = null;

                programList = patientProgramService.search(query, false);

                for (PatientProgramDTO dto : programList) {
                    TherapyProgramDTO program = therapyProgramService.searchProgram(dto.getProgramId());
                    programTMS.add(new PatientProgramTM(
                            dto.getPatientId(),
                            dto.getPatientName(),
                            dto.getProgramId(),
                            dto.getProgramName(),
                            dto.getRegistrationDate(),
                            dto.getPaymentId(),
                            program.getFee(),
                            dto.getLeftToPay()
                    ));
                }

            } else {
                ArrayList<PatientProgramDTO> programList = patientProgramService.search(query, true);
                for (PatientProgramDTO dto : programList) {
                    TherapyProgramDTO program = therapyProgramService.searchProgram(dto.getProgramId());
                    programTMS.add(new PatientProgramTM(
                            dto.getPatientId(),
                            dto.getPatientName(),
                            dto.getProgramId(),
                            dto.getProgramName(),
                            dto.getRegistrationDate(),
                            dto.getPaymentId(),
                            program.getFee(),
                            dto.getLeftToPay()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (programTMS.isEmpty()) {
            showAlert("Not Found", "No results found for: " + query, Alert.AlertType.INFORMATION);
        }

        patientProgramTable.setItems(programTMS);
    }

    @FXML
    void searchToggle(ActionEvent event) {
        if (searchToggleButton.isSelected()) {
            searchToggleButton.setText("Search by Program");
        } else {
            searchToggleButton.setText("Search by Patient");
        }
    }

    @FXML
    void update(ActionEvent event) {
        String feeText = leftToPayTxt.getText();
        BigDecimal fee = new BigDecimal(feeText);

        System.out.println(feeText);
        System.out.println(fee);
        PatientProgramDTO dto = new PatientProgramDTO(
                patientIdTxt.getText(),
                patientNameTxt.getText(),
                programIdTxt.getText(),
                programNameTxt.getText(),
                registerDateTxt.getValue(),
                paymentIdTxt.getText(),
                fee
        );

        if (patientProgramService.updatePatientProgram(dto)) {
            showAlert("Success", "Program updated successfully", Alert.AlertType.INFORMATION);
            refreshPage();
        } else {
            showAlert("Error", "Failed to update program", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void tableClick(MouseEvent event) {
        PatientProgramTM selected = patientProgramTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            patientIdTxt.setText(selected.getPatientId());
            patientNameTxt.setText(selected.getPatientName());
            programIdTxt.setText(selected.getProgramId());
            programNameTxt.setText(selected.getProgramName());
            registerDateTxt.setValue(selected.getRegistrationDate());
            paymentIdTxt.setText(selected.getPaymentId());
            programFeeTxt.setText(String.valueOf(selected.getProgramFee()));
            leftToPayTxt.setText(String.valueOf(selected.getLeftToPay()));
        }
    }

    @FXML
    void searchPatient(ActionEvent event) {
        String name = patientNameTxt.getText().trim();
        PatientDTO patient = patientProgramService.findByPatientName(name);
        if (patient == null) {
            showAlert("Not Found", "Patient not found", Alert.AlertType.WARNING);
            return;
        }
        patientIdTxt.setText(patient.getPatientId());
        patientNameTxt.setText(patient.getName());
    }

    @FXML
    void searchProgram(ActionEvent event) {
        String name = programNameTxt.getText().trim();
        TherapyProgramDTO program = patientProgramService.findByProgramName(name);
        if (program == null) {
            showAlert("Not Found", "Program not found", Alert.AlertType.WARNING);
            return;
        }
        programIdTxt.setText(program.getProgramId());
        programNameTxt.setText(program.getProgramName());
    }

    private void refreshTable() {
        ArrayList<PatientProgramDTO> programList = patientProgramService.getAllPatientPrograms();
        ObservableList<PatientProgramTM> programTMS = FXCollections.observableArrayList();

        for (PatientProgramDTO dto : programList) {
            TherapyProgramDTO program = null;
            try {
                program = therapyProgramService.searchProgram(dto.getProgramId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            programTMS.add(new PatientProgramTM(
                    dto.getPatientId(),
                    dto.getPatientName(),
                    dto.getProgramId(),
                    dto.getProgramName(),
                    dto.getRegistrationDate(),
                    dto.getPaymentId(),
                    program.getFee(),
                    dto.getLeftToPay()
            ));
        }

        patientProgramTable.setItems(programTMS);
    }

    private void clearFields() {
        patientIdTxt.clear();
        patientNameTxt.clear();
        programIdTxt.clear();
        programNameTxt.clear();
        paymentIdTxt.clear();
        registerDateTxt.setValue(null);
        programFeeTxt.setText("");
        leftToPayTxt.setText("");
    }

    public void refreshPage() {
        clearFields();
        refreshTable();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
