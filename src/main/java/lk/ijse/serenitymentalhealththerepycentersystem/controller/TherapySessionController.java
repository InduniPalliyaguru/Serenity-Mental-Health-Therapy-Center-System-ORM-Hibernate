
package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.*;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapySessionTM;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TimeSlotRowTM;
import lk.ijse.serenitymentalhealththerepycentersystem.exception.ScheduleConflictException;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.*;
import lk.ijse.serenitymentalhealththerepycentersystem.util.ValidateUtil;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

public class TherapySessionController implements Initializable {

    @FXML
    private TableColumn<TimeSlotRowTM, String> date1TSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date2TSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date3TSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date4TSCol;
    @FXML
    private TableColumn<TimeSlotRowTM, String> date5TSCol;

    @FXML
    private TableColumn<TherapySessionTM, Duration> durationCol;
    @FXML
    private TableColumn<TherapySessionTM, String> patientIdCol;

    @FXML
    private TextField patientIdTxt;
    @FXML
    private TextField patientNameTxt;
    @FXML
    private TableColumn<TherapySessionTM, String> programIdCol;
    @FXML
    private TextField programIdTxt;
    @FXML
    private ComboBox<String> programNameTxt;
    @FXML
    private Button saveButton;
    @FXML
    private TextField searchTxt;
    @FXML
    private TableColumn<TherapySessionTM, LocalDate> sessionDateCol;
    @FXML
    private DatePicker sessionDateTxt;
    @FXML
    private ComboBox<String> sessionDurationTxt;
    @FXML
    private TableColumn<TherapySessionTM, String> sessionIdCol;
    @FXML
    private TextField sessionIdTxt;
    @FXML
    private TableColumn<TherapySessionTM, String> sessionTimeCol;
    @FXML
    private TextField sessionTimeTxt;
    @FXML
    private TableColumn<TherapySessionTM, String> statusCol;
    @FXML
    private ComboBox<String> statusTxtChoice;
    @FXML
    private TableColumn<TherapySessionTM, String> therapistIdCol;
    @FXML
    private TextField therapistIdTxt;
    @FXML
    private ComboBox<String> therapistNameTxt;
    @FXML
    private TableView<TherapySessionTM> therapySessionTable;
    @FXML
    private TableView<TimeSlotRowTM> timeSlotTable;
    @FXML
    private TableColumn<TimeSlotRowTM, String> timeTSCol;
    @FXML
    private Button updateButton;

    PatientService patientService = (PatientService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);
    PatientProgramService patientProgram = (PatientProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT_PROGRAM);
    TherapistService therapistService = (TherapistService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);
    TherapyProgramService therapyProgramService = (TherapyProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPY_PROGRAM);
    TherapistProgramService therapistProgram = (TherapistProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST_PROGRAM);
    TherapySessionService theraSession = (TherapySessionService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPY_SESSION);
    TherapistAvailabilityService availabilityService = (TherapistAvailabilityService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST_AVAILABILITY);

    DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder().appendPattern("hh:mm a").toFormatter().withLocale(Locale.ENGLISH);
    private final List<LocalDate> nextFiveDates = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionDurationTxt.getItems().addAll("30 minutes", "1 hour", "1 and half hour", "2 hours");
        statusTxtChoice.getItems().addAll("Scheduled", "Completed", "Cancelled");

        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("therapyProgramId"));
        therapistIdCol.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        sessionDateCol.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        sessionTimeCol.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadTimeTable();

        loadAllSessions();

        sessionDateTxt.valueProperty().addListener((observable, oldValue, newValue) -> {
            String therapistId = therapistIdTxt.getText();
            if (newValue != null && !therapistId.isEmpty()) {
                loadDataToTimeTable(therapistId, newValue);
            }
        });

    }

    @FXML
    void btnRefresh() {
        clearForm();
        loadTimeTable();
        loadAllSessions();
    }

    @FXML
    void delete() {
        String sessionId = sessionIdTxt.getText();
        if (sessionId == null || sessionId.isEmpty()) {
            showAlert("Warning", "Please select a session to delete.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean isDeleted = theraSession.deleteSession(sessionId);
            if (isDeleted) {
                showAlert("Success", "Session deleted!", Alert.AlertType.INFORMATION);
                loadAllSessions();
                clearForm();
            } else {
                showAlert("Failed", "Failed to delete session.", Alert.AlertType.ERROR);
                loadAllSessions();
                clearForm();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void loadPaymentPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/payment.fxml"));
            Parent root = loader.load();

            PaymentsController controller = loader.getController();
            controller.setFromMainPage(true);
            controller.configurePage();

            Stage stage = new Stage();
            stage.setTitle("Manage Payments");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void save() {

        String sessionId = sessionIdTxt.getText().trim();
        String patientId = patientIdTxt.getText().trim();
        String programId = programIdTxt.getText().trim();
        String therapistId = therapistIdTxt.getText().trim();
        LocalDate sessionDate = sessionDateTxt.getValue();
        String sessionTimeStr = sessionTimeTxt.getText().trim();
        String status = statusTxtChoice.getValue();
        String sessionDurationChoice = sessionDurationTxt.getValue();

        if (!ValidateUtil.areRequiredFields(sessionId, patientId, programId, therapistId, sessionTimeStr) ||
                sessionDate == null || sessionDurationChoice == null || status == null) {
            showAlert("Input Error", "Please fill in all required fields.", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidTime(sessionTimeStr)) {
            showAlert("Input Error", "Please enter a valid time format (e.g., 10:30 AM).", Alert.AlertType.ERROR);
            return;
        }

        LocalTime sessionTime;
        try {
            sessionTime = LocalTime.parse(sessionTimeStr, timeFormatter);
        } catch (Exception e) {
            showAlert("Input Error", "Invalid time format. Please use format like 10:30 AM.", Alert.AlertType.ERROR);
            return;
        }


        if (!ValidateUtil.isValidId(patientId, "PATIENT")) {
            showAlert("Input Error", "Invalid patient ID format. Should be P followed by 3 digits (e.g., P001).", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(therapistId, "THERAPIST")) {
            showAlert("Input Error", "Invalid therapist ID format. Should be T followed by 3 digits (e.g., T001).", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(programId, "THERAPY_PROGRAM")) {
            showAlert("Input Error", "Invalid program ID format. Should be TP followed by 3 digits (e.g., TP001).", Alert.AlertType.ERROR);
            return;
        }

        int sessionDuration = switch (sessionDurationChoice) {
            case "30 minutes" -> 30;
            case "1 hour" -> 60;
            case "1 and half hour" -> 90;
            case "2 hours" -> 120;
            default -> 0;
        };

        TherapySessionDTO session = new TherapySessionDTO(
                sessionId, patientId, programId, therapistId,
                null, sessionDate, sessionTime, sessionDuration, status
        );

        try {
            boolean saved = theraSession.saveSession(session);
            if (saved) {
                showAlert("Success", "Therapy session saved successfully!", Alert.AlertType.INFORMATION);
                loadAllSessions();
                clearTimeTable();
                clearForm();
            } else {
                showAlert("Error", "Failed to save therapy session.", Alert.AlertType.ERROR);
                loadAllSessions();
                clearTimeTable();
                clearForm();
            }
        } catch (ScheduleConflictException e) {
            showAlert("Scheduling Conflict", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert("Error", "An unexpected error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    void search() {
        String name = searchTxt.getText().trim();
        if (name.isEmpty()) {
            showAlert("Input Error", "Enter a patient Name to search.", Alert.AlertType.WARNING);
            loadAllSessions();
            clearForm();
            return;
        }
        List<PatientDTO> patients = patientService.findByPatientName(name);
        searchTxt.setText(patients.get(0).getName());
        List<TherapySessionDTO> sessions = theraSession.findByPatientId(patients.get(0).getPatientId());
        therapySessionTable.getItems().clear();
        therapySessionTable.getItems().addAll(
                sessions.stream().map(dto ->
                        new TherapySessionTM(
                                dto.getSessionId(),
                                dto.getPatientId(),
                                dto.getTherapyProgramId(),
                                dto.getTherapistId(),
                                null,
                                dto.getSessionDate(),
                                dto.getSessionTime(),
                                Duration.ofMinutes(dto.getDuration()),
                                dto.getStatus()
                        )
                ).collect(Collectors.toList())
        );

        if (sessions.isEmpty()) {
            showAlert("No Results", "No therapy sessions found.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void searchPatient() {
        String name = patientNameTxt.getText().trim();
        ArrayList<PatientDTO> patients = patientService.findByPatientName(name);

        if (patients.isEmpty()) {
            showAlert("Not Found", "Patient not found", Alert.AlertType.WARNING);
            return;
        }

        PatientDTO patient = patients.getFirst();
        patientIdTxt.setText(patient.getPatientId());
        patientNameTxt.setText(patient.getName());

        List<PatientProgramDTO> programs = patientProgram.getProgramsByPatientId(patient.getPatientId());

        List<String> programNames = programs.stream()
                .map(PatientProgramDTO::getProgramName)
                .collect(Collectors.toList());

        programNameTxt.getItems().clear();
        programIdTxt.clear();
        programNameTxt.getItems().addAll(programNames);

        programNameTxt.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                searchProgram();
            }
        });
    }

    @FXML
    void update() {
        if (sessionIdTxt.getText() == null || sessionIdTxt.getText().isEmpty()) {
            showAlert("Warning", "Please select a session from the table to update.", Alert.AlertType.WARNING);
            return;
        }

        String sessionId = sessionIdTxt.getText().trim();
        String patientId = patientIdTxt.getText().trim();
        String programId = programIdTxt.getText().trim();
        String therapistId = therapistIdTxt.getText().trim();
        LocalDate sessionDate = sessionDateTxt.getValue();
        String sessionTimeStr = sessionTimeTxt.getText().trim();
        String status = statusTxtChoice.getValue();
        String sessionDurationChoice = sessionDurationTxt.getValue();

        if (!ValidateUtil.areRequiredFields(sessionId, patientId, programId, therapistId, sessionTimeStr) ||
                sessionDate == null || sessionDurationChoice == null || status == null) {
            showAlert("Input Error", "Please fill in all required fields.", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidTime(sessionTimeStr)) {
            showAlert("Input Error", "Please enter a valid time format (e.g., 10:30 AM).", Alert.AlertType.ERROR);
            return;
        }

        LocalTime sessionTime;
        try {
            sessionTime = LocalTime.parse(sessionTimeStr, timeFormatter);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert("Input Error", "Invalid time format. Please use format like 10:30 AM.", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(patientId, "PATIENT")) {
            showAlert("Input Error", "Invalid patient ID format. Should be P followed by 3 digits (e.g., P001).", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(therapistId, "THERAPIST")) {
            showAlert("Input Error", "Invalid therapist ID format. Should be T followed by 3 digits (e.g., T001).", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidateUtil.isValidId(programId, "THERAPY_PROGRAM")) {
            showAlert("Input Error", "Invalid program ID format. Should be TP followed by 3 digits (e.g., TP001).", Alert.AlertType.ERROR);
            return;
        }

        int sessionDuration = switch (sessionDurationChoice) {
            case "30 minutes" -> 30;
            case "1 hour" -> 60;
            case "1 and half hour" -> 90;
            case "2 hours" -> 120;
            default -> 0;
        };

        try {
            boolean isUpdated = theraSession.updateSession(
                    new TherapySessionDTO(sessionId, patientId, programId, therapistId, null, sessionDate, sessionTime, sessionDuration, status)
            );

            if (isUpdated) {
                showAlert("Success", "Session updated!", Alert.AlertType.INFORMATION);
                loadAllSessions();
                clearForm();
            } else {
                showAlert("Failed", "Failed to update session.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void tableClick() {
        TherapySessionTM selected = therapySessionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            sessionIdTxt.setText(selected.getSessionId());
            patientIdTxt.setText(selected.getPatientId());
            programIdTxt.setText(selected.getTherapyProgramId());
            therapistIdTxt.setText(selected.getTherapistId());
            sessionDateTxt.setValue(selected.getSessionDate());
            sessionTimeTxt.setText(selected.getSessionTime().format(timeFormatter));
            patientNameTxt.setText(patientService.findPatientByID(selected.getPatientId()).getName());
            try {
                programNameTxt.setValue(therapyProgramService.searchProgram(selected.getTherapyProgramId()).getProgramName());
                therapistNameTxt.setValue(therapistService.findByTherapistId(selected.getTherapistId()).getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            String sessionDurationStr = switch ((int) selected.getDuration().toMinutes()) {
                case 30 -> "30 minutes";
                case 60 -> "1 hour";
                case 90 -> "1 and half hour";
                case 120 -> "2 hours";
                default -> "";
            };

            sessionDurationTxt.setValue(sessionDurationStr);
            statusTxtChoice.setValue(selected.getStatus());
            saveButton.setDisable(true);
            updateButton.setDisable(false);
        }
    }

    @FXML
    void onclickTSTable(MouseEvent event) {
        if (event.getClickCount() == 2 && timeSlotTable.getSelectionModel().getSelectedItem() != null) {
            TimeSlotRowTM selected = timeSlotTable.getSelectionModel().getSelectedItem();
            String selectedTime = selected.getTimeSlot();

            System.out.println("Selected time slot: " + selectedTime);

            TablePosition<?, ?> pos = timeSlotTable.getSelectionModel().getSelectedCells().get(0);
            int columnIndex = pos.getColumn();

            if (columnIndex >= 1 && columnIndex <= 5) {
                LocalDate selectedDate = nextFiveDates.get(columnIndex - 1);
                System.out.println("Selected date: " + selectedDate);
            }
        }
    }

    private void loadAllSessions() {

        try {
            List<TherapySessionDTO> allSessions = theraSession.getAllSessions();
            List<TherapySessionTM> tmList = allSessions.stream().map(dto ->
                    new TherapySessionTM(
                            dto.getSessionId(),
                            dto.getPatientId(),
                            dto.getTherapyProgramId(),
                            dto.getTherapistId(),
                            null,
                            dto.getSessionDate(),
                            dto.getSessionTime(),
                            Duration.ofMinutes(dto.getDuration()),
                            dto.getStatus()
                    )
            ).collect(Collectors.toList());

            therapySessionTable.getItems().setAll(tmList);
            sessionIdTxt.setText(theraSession.getNextSessionPK());
            saveButton.setDisable(false);
            updateButton.setDisable(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void searchProgram() {
        String name = programNameTxt.getValue().trim();

        try {
            ArrayList<TherapyProgramDTO> programs = therapyProgramService.findTherapyProgramByName(name);

            if (programs.isEmpty()) {
                showAlert("Not Found", "Program not found", Alert.AlertType.WARNING);
                return;
            }

            TherapyProgramDTO program = programs.getFirst();
            programIdTxt.setText(program.getProgramId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<TherapistProgramDTO> therapistsPrograms = therapistProgram.findByProgramName(programNameTxt.getValue().trim());

        List<String> therapistNames = therapistsPrograms.stream()
                .map(dto -> therapistService.findByTherapistId(dto.getTherapistId()).getName())
                .collect(Collectors.toList());

        therapistNameTxt.getItems().clear();
        therapistIdTxt.clear();
        therapistNameTxt.getItems().addAll(therapistNames);

        therapistNameTxt.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                searchTherapist();
            }
        });
    }

    void searchTherapist() {
        String name = therapistNameTxt.getValue().trim();
        ArrayList<TherapistDTO> therapists = therapistService.findByTherapistName(name);

        if (therapists.isEmpty()) {
            showAlert("Not Found", "Therapist not found", Alert.AlertType.WARNING);
            return;
        }

        TherapistDTO therapist = therapists.getFirst();
        therapistIdTxt.setText(therapist.getTherapistId());

        String therapistId = therapistIdTxt.getText();
        LocalDate selectedDate = sessionDateTxt.getValue();

        if (!therapistId.isEmpty()) {
            loadDataToTimeTable(therapistId, selectedDate != null ? selectedDate : LocalDate.now());
        }
    }

    private void clearForm() {
        sessionIdTxt.clear();
        patientIdTxt.clear();
        patientNameTxt.clear();
        programIdTxt.clear();
        programNameTxt.getItems().clear();
        therapistIdTxt.clear();
        therapistNameTxt.getItems().clear();
        sessionDateTxt.setValue(null);
        sessionTimeTxt.clear();
        sessionDurationTxt.getSelectionModel().clearSelection();
        statusTxtChoice.getSelectionModel().clearSelection();
        searchTxt.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void loadTimeTable() {
        timeTSCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        date1TSCol.setCellValueFactory(new PropertyValueFactory<>("date1Status"));
        date2TSCol.setCellValueFactory(new PropertyValueFactory<>("date2Status"));
        date3TSCol.setCellValueFactory(new PropertyValueFactory<>("date3Status"));
        date4TSCol.setCellValueFactory(new PropertyValueFactory<>("date4Status"));
        date5TSCol.setCellValueFactory(new PropertyValueFactory<>("date5Status"));
    }

    public void clearTimeTable() {
        timeSlotTable.getItems().clear();
    }

    public void loadDataToTimeTable(String therapistId, LocalDate startDate) {
        if (startDate == null) startDate = LocalDate.now();

        nextFiveDates.clear();
        for (int i = 0; i < 5; i++) {
            nextFiveDates.add(startDate.plusDays(i));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd");
        date1TSCol.setText(nextFiveDates.get(0).format(formatter));
        date2TSCol.setText(nextFiveDates.get(1).format(formatter));
        date3TSCol.setText(nextFiveDates.get(2).format(formatter));
        date4TSCol.setText(nextFiveDates.get(3).format(formatter));
        date5TSCol.setText(nextFiveDates.get(4).format(formatter));

        Set<String> uniqueSlots = new TreeSet<>();
        Map<LocalDate, List<String>> slotMap = new HashMap<>();

        DateTimeFormatter tableTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (LocalDate date : nextFiveDates) {
            List<TherapistAvailabilityDTO> availabilityList = availabilityService.findByTherapistAndDate(therapistId, date);
            List<String> daySlots = new ArrayList<>();

            for (TherapistAvailabilityDTO dto : availabilityList) {

                String formattedStartTime = dto.getStartTime().format(tableTimeFormatter);
                String formattedEndTime = dto.getEndTime().format(tableTimeFormatter);

                String slotRange = formattedStartTime + " - " + formattedEndTime;

                daySlots.add(slotRange);
                uniqueSlots.add(slotRange);
            }
            slotMap.put(date, daySlots);
        }

        List<TimeSlotRowTM> rows = new ArrayList<>();
        for (String slot : uniqueSlots) {
            rows.add(new TimeSlotRowTM(
                    slot,
                    slotMap.getOrDefault(nextFiveDates.get(0), List.of()).contains(slot) ? "✔" : "",
                    slotMap.getOrDefault(nextFiveDates.get(1), List.of()).contains(slot) ? "✔" : "",
                    slotMap.getOrDefault(nextFiveDates.get(2), List.of()).contains(slot) ? "✔" : "",
                    slotMap.getOrDefault(nextFiveDates.get(3), List.of()).contains(slot) ? "✔" : "",
                    slotMap.getOrDefault(nextFiveDates.get(4), List.of()).contains(slot) ? "✔" : ""
            ));
        }
        timeSlotTable.getItems().setAll(rows);
    }
}

