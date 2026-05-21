package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.PatientTM;
import lk.ijse.serenitymentalhealththerepycentersystem.exception.RegistrationException;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.PatientService;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.*;

public class PatientsController implements Initializable {

    @FXML
    private TableColumn<PatientTM, String> colAddress;
    @FXML
    private TableColumn<PatientTM, String> colEmail;
    @FXML
    private TableColumn<PatientTM, String> colMedicalHistory;
    @FXML
    private TableColumn<PatientTM, String> colName;
    @FXML
    private TableColumn<PatientTM, String> colPatientId;
    @FXML
    private TableColumn<PatientTM, String> colPhone;
    @FXML
    private TableView<PatientTM> tblPatients;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextArea txtMedicalHistory;
    @FXML
    private TextField txtPatientId;
    @FXML
    private TextField txtPatientName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtSearch;

    private static final String ID_PATTERN = "^P[0-9]{3,}$";
    private static final String NAME_PATTERN = "^[A-z|\\s]{3,}$";
    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_PATTERN = "^0[0-9]{9}$";

    PatientService patientService = (PatientService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMedicalHistory.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));

        loadAllPatients();
        refreshPage();

        tblPatients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) fillFieldFromTable(newValue);
        });

    }

    @FXML
    void btnDeleteOnAction() {
        String patientId = txtPatientId.getText();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this Patient?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = patientService.deletePatient(patientId);

                if (isDeleted) {
                    showAlert(INFORMATION, "Patient deleted successfully!");
                    refreshPage();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void btnSaveOnAction() {
        try {
            checkEmptyFields();

            if (!validateData()) return;

            boolean isSaved = patientService.savePatient(new PatientDTO(
                    txtPatientId.getText(), txtPatientName.getText(), txtEmail.getText(),
                    txtPhone.getText(), txtAddress.getText(), txtMedicalHistory.getText()
            ));

            if (isSaved) {
                showAlert(INFORMATION, "Patient Saved!");
                refreshPage();
            }

        } catch (RegistrationException e) {
            showAlert(ERROR, e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert(ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction() {

        if (txtPatientId.getText().isEmpty()) {
            showAlert(WARNING, "Please select a patient!");
            return;
        }

        if (!validateData()) return;

        try {

            PatientDTO patientDTO = new PatientDTO(
                    txtPatientId.getText(),
                    txtPatientName.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    txtAddress.getText(),
                    txtMedicalHistory.getText()
            );

            boolean isUpdated = patientService.updatePatient(patientDTO);

            if (isUpdated) {
                showAlert(INFORMATION, "Patient updated successfully!");
                refreshPage();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnSearchOnAction() {
        String searchText = txtSearch.getText();
        if (searchText.isEmpty()) {
            loadAllPatients();
            return;
        }

        try {
            if (searchText.matches(ID_PATTERN)) {
                PatientDTO dto = patientService.findPatientByID(searchText);
                if (dto != null) {
                    fillFields(dto);

                    ObservableList<PatientTM> list = FXCollections.observableArrayList();
                    list.add(new PatientTM(
                            dto.getPatientId(), dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAddress(), dto.getMedicalHistory()));
                    tblPatients.setItems(list);
                    return;
                }
            }

            List<PatientDTO> filteredList = patientService.getPatientsBySession(searchText);

            if (!filteredList.isEmpty()) {
                ObservableList<PatientTM> tmList = FXCollections.observableArrayList();
                for (PatientDTO dto : filteredList) {
                    tmList.add(new PatientTM(
                            dto.getPatientId(), dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAddress(), dto.getMedicalHistory()));
                }
                tblPatients.setItems(tmList);
            } else {
                List<PatientDTO> patientsByName = patientService.findByPatientName(searchText);

                if (patientsByName != null && !patientsByName.isEmpty()) {
                    System.out.println("patient found");
                    ObservableList<PatientTM> tmList = FXCollections.observableArrayList();

                    for (PatientDTO dto : patientsByName) {
                        tmList.add(new PatientTM(
                                dto.getPatientId(),
                                dto.getName(),
                                dto.getEmail(),
                                dto.getPhone(),
                                dto.getAddress(),
                                dto.getMedicalHistory()
                        ));
                    }
                    tblPatients.setItems(tmList);
                } else {
                    showAlert(WARNING, "Cannot Find Patient!");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnRefreshOnAction() {
        refreshPage();
    }

    @FXML
    void btnFilterAllProgramsOnAction() {
        try {
            List<PatientDTO> specialPatients = patientService.getPatientsEnrolledInAllPrograms();

            if (specialPatients.isEmpty()) {
                showAlert(INFORMATION, "No patients are enrolled in all therapy programs currently.");
                return;
            }

            tblPatients.getItems().clear();
            for (PatientDTO dto : specialPatients) {
                tblPatients.getItems().add(new PatientTM(
                        dto.getPatientId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getPhone(),
                        dto.getAddress(),
                        dto.getMedicalHistory()
                ));
            }

            showAlert(INFORMATION, "Showing patients registered for ALL therapy programs!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert(ERROR, "Failed to load filtered data.");
        }
    }

    private void checkEmptyFields() {

        if (txtPatientId.getText().isEmpty()) {
            throw new RegistrationException("ID field is empty. Enter valid ID! (P001)");
        }
        if (txtPatientName.getText().isEmpty()) {
            throw new RegistrationException("Name field is empty. Enter valid Name!");
        }
        if (txtEmail.getText().isEmpty()) {
            throw new RegistrationException("Email field is empty. Enter valid Email!");
        }
        if (txtPhone.getText().isEmpty()) {
            throw new RegistrationException("Contact field is empty. Enter Contact!");
        }
        if (txtAddress.getText().isEmpty()) {
            throw new RegistrationException("Address field is empty. Enter Address!");
        }
        if (txtMedicalHistory.getText().isEmpty()) {
            throw new RegistrationException("Medical History field is empty. Enter Medical History!");
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    private void fillFields(PatientDTO dto) {
        txtPatientId.setText(dto.getPatientId());
        txtPatientName.setText(dto.getName());
        txtEmail.setText(dto.getEmail());
        txtPhone.setText(dto.getPhone());
        txtAddress.setText(dto.getAddress());
        txtMedicalHistory.setText(dto.getMedicalHistory());
    }

    private void refreshPage() {
        txtPatientName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        txtMedicalHistory.clear();
        txtSearch.clear();
        loadAllPatients();

        try {
            txtPatientId.setText(patientService.getNextPatientPK());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadAllPatients() {
        ObservableList<PatientTM> patientTMS = FXCollections.observableArrayList();
        try {
            List<PatientDTO> allPatients = patientService.getAllPatients();
            for (PatientDTO dto : allPatients) {
                patientTMS.add(new PatientTM(
                        dto.getPatientId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getPhone(),
                        dto.getAddress(),
                        dto.getMedicalHistory()
                ));
            }
            tblPatients.setItems(patientTMS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean validateData() {
        if (!txtPatientId.getText().matches(ID_PATTERN)) {
            showAlert(ERROR, "Invalid Patient ID (Ex: P001)");
            return false;
        }
        if (!txtPatientName.getText().matches(NAME_PATTERN)) {
            showAlert(ERROR, "Invalid Name");
            return false;
        }
        if (!txtEmail.getText().matches(EMAIL_PATTERN)) {
            showAlert(ERROR, "Invalid Email");
            return false;
        }
        if (!txtPhone.getText().matches(PHONE_PATTERN)) {
            showAlert(ERROR, "Invalid Phone (Ex: 0771234567)");
            return false;
        }
        return true;
    }

    private void fillFieldFromTable(PatientTM tm) {
        txtPatientId.setText(tm.getPatientId());
        txtPatientName.setText(tm.getName());
        txtEmail.setText(tm.getEmail());
        txtPhone.setText(tm.getContact());
        txtAddress.setText(tm.getAddress());
        txtMedicalHistory.setText(tm.getMedicalHistory());
    }

}
