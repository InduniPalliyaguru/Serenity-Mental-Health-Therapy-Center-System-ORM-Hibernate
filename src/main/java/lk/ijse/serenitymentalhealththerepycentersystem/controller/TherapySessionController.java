package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.*;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.AvailabilitySlotTM;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapySessionTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TherapySessionController implements Initializable {

    @FXML
    private Button btnCheckPatient;

    @FXML
    private ComboBox<Integer> cmbDuration;

    @FXML
    private ComboBox<String> cmbProgram;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private ComboBox<String> cmbTherapist;

    @FXML
    private TableColumn<TherapySessionTM, LocalDate> colDate;

    @FXML
    private TableColumn<AvailabilitySlotTM, String> colDay;

    @FXML
    private TableColumn<TherapySessionTM, String> colPatientId;

    @FXML
    private TableColumn<TherapySessionTM, String> colProgram;

    @FXML
    private TableColumn<TherapySessionTM, String> colSessionId;

    @FXML
    private TableColumn<TherapySessionTM, String> colStatus;

    @FXML
    private TableColumn<TherapySessionTM, String> colTherapistId;

    @FXML
    private TableColumn<AvailabilitySlotTM, LocalTime> colTime;

    @FXML
    private DatePicker dateSession;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<AvailabilitySlotTM> tblAvailability;

    @FXML
    private TableView<TherapySessionTM> tblSessions;

    @FXML
    private TextField txtPatientId;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtProgramId;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSessionId;

    @FXML
    private TextField txtSessionTime;

    @FXML
    private TextField txtTherapistId;

    TherapySessionService sessionService = (TherapySessionService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPY_SESSION);
    PatientService patientService = (PatientService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);
    TherapistService therapistService = (TherapistService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);
    TherapyProgramService programService = (TherapyProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPY_PROGRAM);
    TherapistAvailabilityService availabilityService = (TherapistAvailabilityService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST_AVAILABILITY);

    private String selectedAvailabilityId = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        refreshPage();

        // load available slots when change the date
        dateSession.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !txtTherapistId.getText().isEmpty()) {
                loadAvailableSlots(txtTherapistId.getText(), newVal);
            }
        });

        // select slot from available table
        tblAvailability.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtSessionTime.setText(newVal.getStartTime().toString());
                selectedAvailabilityId = newVal.getAvailabilityId();
            }
        });

        // select session from main table to update delete
        tblSessions.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillFieldsFromTable(newVal);
        });

        // load slot when change the therapist
        cmbTherapist.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    List<TherapistDTO> dtoList = therapistService.findByTherapistName(newVal);

                    if (dtoList != null && !dtoList.isEmpty()) {

                        TherapistDTO dto = dtoList.get(0);

                        txtTherapistId.setText(dto.getTherapistId());

                        if (dateSession.getValue() != null) {
                            loadAvailableSlots(dto.getTherapistId(), dateSession.getValue());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // fill ID when change the program
        cmbProgram.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    TherapyProgramDTO dto = programService.findByName(newVal);
                    if (dto != null) txtProgramId.setText(dto.getProgramId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupTableColumns() {
        // Session Table
        colSessionId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colTherapistId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Availability Table
        colTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    void btnCheckPatientOnAction(ActionEvent event) {
        String name = txtPatientName.getText().trim();

        if (name.isEmpty()) {
            showError("Please enter a patient name to check.");
            return;
        }

        try {
            List<PatientDTO> patientList = patientService.findByPatientName(name);

            if (patientList != null && !patientList.isEmpty()) {

                PatientDTO patient = patientList.get(0);

                txtPatientId.setText(patient.getPatientId());
                txtPatientName.setStyle("-fx-border-color: #2ecc71; -fx-border-width: 2px;");

            } else {
                showError("Patient not found! Please check the name.");
                txtPatientId.clear();
                txtPatientName.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateInputs()) return;
        try {
            if (sessionService.saveSession(collectDto())) {
                new Alert(Alert.AlertType.INFORMATION, "Session Booked Successfully!").show();
                refreshPage();
            }
        } catch (Exception e) {
            showError("Something went wrong!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;
        try {
            if (sessionService.updateSession(collectDto(), selectedAvailabilityId)) {
                new Alert(Alert.AlertType.INFORMATION, "Session Updated Successfully!").show();
                refreshPage();
            }
        } catch (Exception e) {
            showError("Something went wrong!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtSessionId.getText();
        if (id == null || id.isEmpty()) {
            showError("Please select a session to cancel.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel Session " + id + "?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                if (sessionService.deleteSession(id, selectedAvailabilityId)) {
                    new Alert(Alert.AlertType.INFORMATION, "Session Cancelled & Time Slot Freed!").show();
                    refreshPage();
                }
            } catch (Exception e) {
                showError("Something went wrong!");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String text = txtSearch.getText();
        if (text.isEmpty()) {
            loadAllSessions();
            return;
        }
        try {
            List<TherapySessionDTO> results = sessionService.searchByPatientName(text);
            loadTableData(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnConfirmAndPayOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to book this session and generate the invoice?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                sessionService.saveSession(collectDto());

                new Alert(Alert.AlertType.INFORMATION, "Session Saved. Redirecting to Print Invoice for " + txtSessionId.getText()).show();

                refreshPage();
            } catch (Exception e) {
                showError("Something went wrong!");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnRefreshOnAction(MouseEvent event) {
        refreshPage();
    }

    private void loadAvailableSlots(String theraId, LocalDate date) {
        try {
            List<TherapistAvailabilityDTO> slots = availabilityService.getAvailableSlots(theraId, date);
            ObservableList<AvailabilitySlotTM> tmList = FXCollections.observableArrayList();

            for (TherapistAvailabilityDTO d : slots) {
                tmList.add(new AvailabilitySlotTM(d.getAvailabilityId(), d.getAvailableDate(), d.getStartTime(), "Available"));
            }
            tblAvailability.setItems(tmList);

            if (tmList.isEmpty()) {
                tblAvailability.setPlaceholder(new Label("No available slots for this date."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllSessions() {
        try {
            List<TherapySessionDTO> all = sessionService.getAllSessions();
            loadTableData(all);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTableData(List<TherapySessionDTO> list) {
        ObservableList<TherapySessionTM> tmList = FXCollections.observableArrayList();
        list.forEach(d -> tmList.add(new TherapySessionTM(
                d.getSessionId(),
                d.getPatientId(),
                d.getTherapistId(),
                d.getTherapyProgramId(),
                d.getSessionDate(),
                d.getStatus()
        )));
        tblSessions.setItems(tmList);
    }

    private void loadComboBoxes() {
        try {
            cmbProgram.setItems(FXCollections.observableArrayList(programService.getAllPrograms().stream().map(TherapyProgramDTO::getProgramName).collect(Collectors.toList())));
            cmbTherapist.setItems(FXCollections.observableArrayList(therapistService.getAllTherapists().stream().map(TherapistDTO::getName).collect(Collectors.toList())));
            cmbStatus.setItems(FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled"));
            cmbDuration.setItems(FXCollections.observableArrayList(30, 45, 60, 90));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshPage() {
        try {
            txtSessionId.setText(sessionService.generateNextSessionId());

            txtPatientId.clear();
            txtPatientName.clear();
            txtPatientName.setStyle(null);
            txtTherapistId.clear();
            txtProgramId.clear();
            txtSessionTime.clear();
            dateSession.setValue(null);
            txtSearch.clear();

            selectedAvailabilityId = null;
            tblAvailability.getItems().clear();

            loadComboBoxes();
            loadAllSessions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TherapySessionDTO collectDto() {
        return new TherapySessionDTO(
                txtSessionId.getText(),
                txtPatientId.getText(),
                txtProgramId.getText(),
                txtTherapistId.getText(),
                selectedAvailabilityId,
                dateSession.getValue(),
                LocalTime.parse(txtSessionTime.getText()),
                cmbDuration.getValue(),
                cmbStatus.getValue()
        );
    }

    private void fillFieldsFromTable(TherapySessionTM tm) {
        txtSessionId.setText(tm.getSessionId());
        txtPatientId.setText(tm.getPatientId());
        txtTherapistId.setText(tm.getTherapistId());
        txtProgramId.setText(tm.getTherapyProgramId());
        dateSession.setValue(tm.getSessionDate());
        cmbStatus.setValue(tm.getStatus());

        try {
            TherapistDTO t = therapistService.findByTherapistId(tm.getTherapistId());
            if (t != null) cmbTherapist.setValue(t.getName());

            TherapyProgramDTO p = programService.searchProgram(tm.getTherapyProgramId());
            if (p != null) cmbProgram.setValue(p.getProgramName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        if (txtPatientId.getText().isEmpty()) {
            showError("Patient ID is missing. Please check the patient name.");
            return false;
        }
        if (txtTherapistId.getText().isEmpty() || dateSession.getValue() == null) {
            showError("Please select a Therapist and a Date.");
            return false;
        }
        if (selectedAvailabilityId == null || txtSessionTime.getText().isEmpty()) {
            showError("Please click on an Available Time Slot from the table.");
            return false;
        }
        if (cmbProgram.getValue() == null || cmbStatus.getValue() == null || cmbDuration.getValue() == null) {
            showError("Please fill all dropdowns (Program, Status, Duration).");
            return false;
        }
        return true;
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }

}
