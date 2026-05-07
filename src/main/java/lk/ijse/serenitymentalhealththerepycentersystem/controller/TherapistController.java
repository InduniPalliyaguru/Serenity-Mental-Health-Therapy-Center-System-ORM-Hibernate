package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapyProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapistProgramTM;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapistTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistService;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapyProgramService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.ERROR;

public class TherapistController implements Initializable {

    @FXML
    private Button btnAssign;

    @FXML
    private ComboBox<String> cmbPrograms;

    @FXML
    private TableColumn<TherapistTM, String> colEmail;

    @FXML
    private TableColumn<TherapistTM, String> colId;

    @FXML
    private TableColumn<TherapistTM, String> colName;

    @FXML
    private TableColumn<TherapistProgramTM, String> colProgId;

    @FXML
    private TableColumn<TherapistProgramTM, String> colProgName;

    @FXML
    private TableColumn<TherapistTM, String> colSpecialization;

    @FXML
    private TableColumn<TherapistTM, String> colPhone;

    @FXML
    private TableView<TherapistProgramTM> tblAssignedPrograms;

    @FXML
    private TableView<TherapistTM> tblTherapists;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSpecialization;

    @FXML
    private TextField txtTherapistId;

    @FXML
    private TextField txtTherapistName;

    private static final String ID_PATTERN = "^T[0-9]{3,}$";
    private static final String NAME_PATTERN = "^[A-z\\s]{3,}$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PHONE_PATTERN = "^0[0-9]{9}$";

    TherapistService therapistBO = (TherapistService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);
    TherapyProgramService programBO = (TherapyProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPY_PROGRAM);

    private ObservableList<TherapistTM> therapistList = FXCollections.observableArrayList();
    private ObservableList<TherapistProgramTM> assignedProgramList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        colProgId.setCellValueFactory(new PropertyValueFactory<>("therapyProgramId"));
        colProgName.setCellValueFactory(new PropertyValueFactory<>("therapyProgramName"));

        refreshPage();

        // Table Selection Listener
        tblTherapists.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) fillFieldsFromTable(newValue);
        });

        tblAssignedPrograms.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tblAssignedPrograms.getSelectionModel().isEmpty()) {
                removeProgramFromTable();
            }
        });
    }

    @FXML
    void btnAssignProgramOnAction(ActionEvent event) {
        String selectedProgId = cmbPrograms.getSelectionModel().getSelectedItem();
        if (selectedProgId == null) return;

        try {
            TherapyProgramDTO progDto = programBO.searchProgram(selectedProgId);
            assignedProgramList.add(new TherapistProgramTM(progDto.getProgramId(), progDto.getProgramName()));
            tblAssignedPrograms.setItems(assignedProgramList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtTherapistId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure to delete this Therapist?",
                ButtonType.YES, ButtonType.NO);

        alert.setTitle("Confirm Deletion");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                if (therapistBO.deleteTherapist(id)) {
                    showAlert(Alert.AlertType.INFORMATION, "Deleted Successfully");
                    refreshPage();
                } else {
                     showAlert(Alert.AlertType.ERROR, "Deletion Failed!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Deletion Failed. Something went wrong!");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateData()) return;

        try {
            TherapistDTO therapistDTO = new TherapistDTO(
                    txtTherapistId.getText(),
                    txtTherapistName.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    txtSpecialization.getText(),
                    assignedProgramList
            );

            boolean isSaved = therapistBO.saveTherapistWithPrograms(therapistDTO);
            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Saved Successfully!");
                refreshPage();
            } else {
                showAlert(ERROR, "Save Failed!");
            }
        } catch (Exception e) {
            showAlert(ERROR, "Save Failed. Something went wrong!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchText = txtSearch.getText();

        if (searchText.isEmpty()) {
            loadAllTherapists();
            return;
        }

        try {

            if (searchText.matches(ID_PATTERN)) {
                TherapistDTO dto = therapistBO.findByTherapistId(searchText);
                if (dto != null) {
                    fillFields(dto);

                    assignedProgramList.clear();
                    if (dto.getAssignedPrograms() != null) {
                        assignedProgramList.addAll(dto.getAssignedPrograms());
                    }
                    tblAssignedPrograms.setItems(assignedProgramList);

                    for (TherapistTM tm : tblTherapists.getItems()) {
                        if (tm.getTherapistId().equals(dto.getTherapistId())) {
                            tblTherapists.getSelectionModel().select(tm);
                            break;
                        }
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Therapist Not Found!");
                }
            } else {
                List<TherapistDTO> results = therapistBO.findByTherapistName(searchText);
                if (!results.isEmpty()) {
                    ObservableList<TherapistTM> tmList = FXCollections.observableArrayList();
                    for (TherapistDTO dto : results) {
                        tmList.add(new TherapistTM(dto.getTherapistId(), dto.getName(), dto.getEmail(), dto.getPhone(), dto.getSpecialization()));
                    }
                    tblTherapists.setItems(tmList);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Cannot Find therapist By that Name!.");
                }
            }
        } catch (Exception e) {
            showAlert(ERROR, "Something went wrong!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnRefreshPageOnAction(MouseEvent event) {
        refreshPage();
    }

    @FXML
    void btnTherapistAvailableOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/therapistAvailability.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Therapist Availability Management");

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.initOwner(btnAssign.getScene().getWindow());

            stage.show();

        } catch (IOException e) {
            showAlert(ERROR, "Something went wrong. Cannot open the page!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateData()) return;

        try {
            TherapistDTO dto = new TherapistDTO(
                    txtTherapistId.getText(),
                    txtTherapistName.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    txtSpecialization.getText(),
                    assignedProgramList
            );

            boolean isUpdated = therapistBO.updateTherapist(dto);
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Therapist details updated successfully!");
                refreshPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed. Something went wrong!");
            e.printStackTrace();
        }
    }

    private void fillFields(TherapistDTO dto) {
        txtTherapistId.setText(dto.getTherapistId());
        txtTherapistName.setText(dto.getName());
        txtEmail.setText(dto.getEmail());
        txtPhone.setText(dto.getPhone());
        txtSpecialization.setText(dto.getSpecialization());
    }

    private void fillFieldsFromTable(TherapistTM tm) {
        txtTherapistId.setText(tm.getTherapistId());
        txtTherapistName.setText(tm.getName());
        txtEmail.setText(tm.getEmail());
        txtPhone.setText(tm.getPhone());
        txtSpecialization.setText(tm.getSpecialization());
    }

    public void refreshPage() {
        txtTherapistId.setText(therapistBO.getNextTherapistPK());
        txtTherapistName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtSpecialization.clear();
        assignedProgramList.clear();
        tblAssignedPrograms.setItems(assignedProgramList);

        loadAllTherapists();
        loadProgramIds();
    }

    private void loadAllTherapists() {
        therapistList.clear();
        try {
            List<TherapistDTO> allTherapists = therapistBO.getAllTherapists();
            for (TherapistDTO dto : allTherapists) {
                therapistList.add(new TherapistTM(dto.getTherapistId(), dto.getName(), dto.getEmail(), dto.getPhone(), dto.getSpecialization()));
            }
            tblTherapists.setItems(therapistList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Something went wrong. Therapist Table not loading!");
            e.printStackTrace();
        }
    }

    private void loadProgramIds() {
        try {
            List<TherapyProgramDTO> allPrograms = programBO.getAllPrograms();
            ObservableList<String> ids = FXCollections.observableArrayList();
            for (TherapyProgramDTO dto : allPrograms) {
                ids.add(dto.getProgramId());
            }
            cmbPrograms.setItems(ids);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Something went wrong. Program IDs not loading!");
            e.printStackTrace();
        }
    }

    private boolean validateData() {
        if (!txtTherapistId.getText().matches(ID_PATTERN)) {
            showAlert(ERROR, "Invalid Patient ID (Ex: T001)");
            return false;
        }
        if (!txtTherapistName.getText().matches(NAME_PATTERN)) {
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

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    private void removeProgramFromTable() {
        TherapistProgramTM selectedItem = tblAssignedPrograms.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Do you want to remove '" + selectedItem.getTherapyProgramName() + "' from this therapist?",
                    ButtonType.YES, ButtonType.NO);

            alert.setTitle("Remove Program");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                assignedProgramList.remove(selectedItem);
                tblAssignedPrograms.refresh();

                showAlert(Alert.AlertType.INFORMATION, "Program removed from the list. Click 'Update' to save changes.");
            }
        }
    }

}
