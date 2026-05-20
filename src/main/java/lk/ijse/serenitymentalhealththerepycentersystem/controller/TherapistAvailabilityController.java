package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistAvailabilityDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapistAvailabilityTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistAvailabilityService;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistService;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapistAvailabilityController implements Initializable {

    @FXML
    private ComboBox<String> cmbAvailability;
    @FXML
    private TableColumn<TherapistAvailabilityTM, String> colAvailability;
    @FXML
    private TableColumn<TherapistAvailabilityTM, String> colAvailabilityId;
    @FXML
    private TableColumn<TherapistAvailabilityTM, LocalDate> colDate;
    @FXML
    private TableColumn<TherapistAvailabilityTM, LocalTime> colEndTime;
    @FXML
    private TableColumn<TherapistAvailabilityTM, LocalTime> colStartTime;
    @FXML
    private TableColumn<TherapistAvailabilityTM, String> colTherapistId;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<TherapistAvailabilityTM> tblAvailability;
    @FXML
    private TextField txtAvailabilityId;
    @FXML
    private TextField txtEndTime;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtStartTime;
    @FXML
    private TextField txtTherapistName;

    private String selectedTherapistId;

    TherapistAvailabilityService availService = (TherapistAvailabilityService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST_AVAILABILITY);
    TherapistService therapistService = (TherapistService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbAvailability.setItems(FXCollections.observableArrayList("Available", "Not Available"));

        colAvailabilityId.setCellValueFactory(new PropertyValueFactory<>("availId"));
        colTherapistId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("status"));

        refreshPage();

        tblAvailability.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) fillFieldFromTable(newValue);
        });
    }

    @FXML
    void btnCheckOnAction() {
        String nameInput = txtTherapistName.getText().trim();

        if (nameInput.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter the therapist's name!").show();
            return;
        }

        try {

            List<TherapistDTO> therapistList = therapistService.findByTherapistName(nameInput);

            if (therapistList.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "No therapist found by this name!").show();
                selectedTherapistId = null;
                txtTherapistName.setStyle("-fx-border-color: red;");
            } else if (therapistList.size() > 1) {
                new Alert(Alert.AlertType.WARNING, "There are several therapists with this name. Please enter the full name.").show();
                selectedTherapistId = null;
            } else {
                TherapistDTO selectedTherapist = therapistList.get(0);
                selectedTherapistId = selectedTherapist.getTherapistId();

                txtTherapistName.setText(selectedTherapist.getName());
                txtTherapistName.setStyle("-fx-border-color: green;");

                new Alert(Alert.AlertType.INFORMATION, "The therapist confirmed: " + selectedTherapistId).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction() {
        String id = txtAvailabilityId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this Availability record?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                if (availService.deleteAvailability(id)) {
                    new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                    refreshPage();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void btnSavOnAction() {
        if (!validateInputs() || selectedTherapistId == null) {
            new Alert(Alert.AlertType.WARNING, "Please check the therapist and enter all data!").show();
            return;
        }

        try {
            TherapistAvailabilityDTO dto = new TherapistAvailabilityDTO(
                    txtAvailabilityId.getText(),
                    selectedTherapistId,
                    txtTherapistName.getText(),
                    datePicker.getValue(),
                    LocalTime.parse(txtStartTime.getText()),
                    LocalTime.parse(txtEndTime.getText()),
                    cmbAvailability.getValue().equals("Available")
            );

            if (availService.saveAvailability(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Schedule saved successfully!").show();
                refreshPage();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    @FXML
    void btnSearchOnAction() {
        String searchText = txtSearch.getText();
        if (searchText.isEmpty()) {
            loadAllAvailability();
            return;
        }

        try {
            ObservableList<TherapistAvailabilityTM> tmList = FXCollections.observableArrayList();

            List<TherapistAvailabilityDTO> results = availService.findAvailabilityByTherapistName(searchText);

            if (results.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "Result not found!").show();
            } else {
                for (TherapistAvailabilityDTO dto : results) {
                    tmList.add(new TherapistAvailabilityTM(
                            dto.getAvailabilityId(),
                            dto.getTherapistId(),
                            dto.getAvailableDate(),
                            dto.getStartTime(),
                            dto.getEndTime(),
                            dto.isAvailable() ? "Available" : "Not Available"
                    ));
                }
                tblAvailability.setItems(tmList);

                if (results.size() == 1) {
                    fillFields(results.get(0));
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction() {
        if (!validateInputs()) return;

        try {
            TherapistAvailabilityDTO dto = new TherapistAvailabilityDTO(
                    txtAvailabilityId.getText(),
                    selectedTherapistId,
                    txtTherapistName.getText(),
                    datePicker.getValue(),
                    LocalTime.parse(txtStartTime.getText()),
                    LocalTime.parse(txtEndTime.getText()),
                    cmbAvailability.getValue().equals("Available")
            );

            if (availService.updateAvailability(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Update Successfully!").show();
                refreshPage();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnRefreshOnAction() {
        refreshPage();
    }

    private boolean validateInputs() {

        if (datePicker.getValue() == null || datePicker.getValue().isBefore(LocalDate.now())) {
            new Alert(Alert.AlertType.ERROR, "Please select a valid future date!").show();
            return false;
        }

        try {
            LocalTime start = LocalTime.parse(txtStartTime.getText());
            LocalTime end = LocalTime.parse(txtEndTime.getText());
            if (!start.isBefore(end)) {
                new Alert(Alert.AlertType.ERROR, "The start time must be before the end time!").show();
                return false;
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Please enter the time correctly (HH:mm)").show();
            return false;
        }
        return true;
    }

    private void fillFields(TherapistAvailabilityDTO dto) {
        txtAvailabilityId.setText(dto.getAvailabilityId());
        selectedTherapistId = dto.getTherapistId();
        txtTherapistName.setText(dto.getTherapistName());
        datePicker.setValue(dto.getAvailableDate());
        txtStartTime.setText(dto.getStartTime().toString());
        txtEndTime.setText(dto.getEndTime().toString());
        cmbAvailability.setValue(dto.isAvailable() ? "Available" : "Not Available");
    }

    private void refreshPage() {
        try {
            txtAvailabilityId.setText(availService.getNextAvailabilityId());
            txtSearch.clear();
            txtTherapistName.clear();
            selectedTherapistId = null;
            txtStartTime.clear();
            txtEndTime.clear();
            loadAllAvailability();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void fillFieldFromTable(TherapistAvailabilityTM tm) {
        try {
            txtAvailabilityId.setText(tm.getAvailId());
            txtTherapistName.setText(therapistService.findByTherapistId(tm.getTherapistId()).getName());
            selectedTherapistId = tm.getTherapistId();
            txtStartTime.setText(tm.getStartTime().toString());
            txtEndTime.setText(tm.getEndTime().toString());
            cmbAvailability.setValue(tm.getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadAllAvailability() {
        try {
            List<TherapistAvailabilityDTO> all = availService.getAllAvailability();
            ObservableList<TherapistAvailabilityTM> tmList = FXCollections.observableArrayList();
            for (TherapistAvailabilityDTO dto : all) {
                tmList.add(new TherapistAvailabilityTM(
                        dto.getAvailabilityId(),
                        dto.getTherapistId(),
                        dto.getAvailableDate(),
                        dto.getStartTime(),
                        dto.getEndTime(),
                        dto.isAvailable() ? "Available" : "Not Available"
                ));
            }
            tblAvailability.setItems(tmList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
