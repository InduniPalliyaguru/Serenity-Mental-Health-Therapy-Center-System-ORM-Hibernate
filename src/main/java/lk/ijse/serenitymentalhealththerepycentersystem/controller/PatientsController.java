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
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.PatientTM;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapyProgramTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.BOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.PatientBO;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.*;

public class PatientsController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

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

    PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOType.PATIENT);

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
    void btnDeleteOnAction(ActionEvent event) {
        String patientId = txtPatientId.getText();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this Patient?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = patientBO.deletePatient(patientId);

                if (isDeleted) {
                    showAlert(INFORMATION, "Patient deleted successfully!");
                    refreshPage();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (haveEmptyFields()) {
            showAlert(WARNING, "Fields cannot be empty!");
            return;
        }
        if (!validateData()) return;

        try {
            boolean isSaved = patientBO.savePatient(new PatientDTO(
                    txtPatientId.getText(), txtPatientName.getText(), txtEmail.getText(),
                    txtPhone.getText(), txtAddress.getText(), txtMedicalHistory.getText()
            ));
            if (isSaved) {
                showAlert(INFORMATION, "Patient Saved!");
                refreshPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

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

            boolean isUpdated = patientBO.updatePatient(patientDTO);

            if (isUpdated) {
                showAlert(INFORMATION, "Patient updated successfully!");
                refreshPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchText = txtSearch.getText();
        if (searchText.isEmpty()) {
            loadAllPatients();
            return;
        }

        try {
            if (searchText.matches(ID_PATTERN)) {
                PatientDTO dto = patientBO.findPatientByID(searchText);
                if (dto != null) {
                    fillFields(dto);

                    ObservableList<PatientTM> list = FXCollections.observableArrayList();
                    list.add(new PatientTM(
                            dto.getPatientId(), dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAddress(), dto.getMedicalHistory()));
                    tblPatients.setItems(list);
                    return;
                }
            }

            List<PatientDTO> filteredList = patientBO.getPatientsBySession(searchText);

            if (!filteredList.isEmpty()) {
                ObservableList<PatientTM> tmList = FXCollections.observableArrayList();
                for (PatientDTO dto : filteredList) {
                    tmList.add(new PatientTM(
                            dto.getPatientId(), dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAddress(), dto.getMedicalHistory()));
                }
                tblPatients.setItems(tmList);
            } else {
                List<PatientDTO> patientsByName = patientBO.findByPatientName(searchText);

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
            e.printStackTrace();
        }
    }

    @FXML
    void btnRefreshOnAction(MouseEvent event) {
        refreshPage();
    }

    private boolean haveEmptyFields() {

        if (txtPatientId.getText().isEmpty()) {
            showAlert(ERROR,"ID field is empty. Enter valid ID! (P001)");
            return true;
        }
        if (txtPatientName.getText().isEmpty()) {
            showAlert(ERROR,"Name field is empty. Enter valid Name!");
            return true;
        }
        if (txtEmail.getText().isEmpty()) {
            showAlert(ERROR,"Email field is empty. Enter valid Email!");
            return true;
        }
        if (txtPhone.getText().isEmpty()) {
            showAlert(ERROR,"Contact field is empty. Enter Contact!");
            return true;
        }
        if (txtAddress.getText().isEmpty()) {
            showAlert(ERROR,"Address field is empty. Enter Address!");
            return true;
        }
        if (txtMedicalHistory.getText().isEmpty()) {
            showAlert(ERROR,"Medical History field is empty. Enter Medical History!");
            return true;
        }
        return false;
    }

    private void showAlert(Alert.AlertType type,String msg) {
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
            txtPatientId.setText(patientBO.getNextPatientPK());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllPatients() {
        ObservableList<PatientTM> patientTMS = FXCollections.observableArrayList();
        try {
            List<PatientDTO> allPatients = patientBO.getAllPatients();
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
            e.printStackTrace();
        }
    }

    private boolean validateData() {
        if (!txtPatientId.getText().matches(ID_PATTERN)) {
            showAlert(ERROR,"Invalid Patient ID (Ex: P001)");
            return false;
        }
        if (!txtPatientName.getText().matches(NAME_PATTERN)) {
            showAlert(ERROR,"Invalid Name");
            return false;
        }
        if (!txtEmail.getText().matches(EMAIL_PATTERN)) {
            showAlert(ERROR,"Invalid Email");
            return false;
        }
        if (!txtPhone.getText().matches(PHONE_PATTERN)) {
            showAlert(ERROR,"Invalid Phone (Ex: 0771234567)");
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
