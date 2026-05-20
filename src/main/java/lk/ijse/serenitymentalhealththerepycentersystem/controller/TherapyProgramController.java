package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapyProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapyProgramTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapyProgramService;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapyProgramController implements Initializable {

    @FXML
    private TableColumn<TherapyProgramTM, String> colDescription;
    @FXML
    private TableColumn<TherapyProgramTM, String> colDuration;
    @FXML
    private TableColumn<TherapyProgramTM, BigDecimal> colFee;
    @FXML
    private TableColumn<TherapyProgramTM, String> colName;
    @FXML
    private TableColumn<TherapyProgramTM, String> colProgramId;
    @FXML
    private TableView<TherapyProgramTM> tblTherapyProgram;
    @FXML
    private TextArea txtDescription;
    @FXML
    private TextField txtDuration;
    @FXML
    private TextField txtFee;
    @FXML
    private TextField txtProgramId;
    @FXML
    private TextField txtProgramName;
    @FXML
    private TextField txtSearch;

    TherapyProgramService therapyProgramBO = (TherapyProgramService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPY_PROGRAM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        refreshPage();
        colFee.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblTherapyProgram.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) fillFields(newValue);
        });

    }

    @FXML
    void btnDeleteOnAction() {
        String id = txtProgramId.getText();
        if (id.isEmpty()) return;

        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Delete this program?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                if (therapyProgramBO.deleteProgram(id)) {
                    showAlert(Alert.AlertType.INFORMATION, "Deleted!");
                    refreshPage();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Delete Failed!");
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void btnSaveOnAction() {
        if (haveEmptyFields()) return;
        if (!validateData()) return;
        try {
            BigDecimal fee = new BigDecimal(txtFee.getText());

            boolean isSaved = therapyProgramBO.saveProgram(new TherapyProgramDTO(
                    txtProgramId.getText(),
                    txtProgramName.getText(),
                    txtDuration.getText(),
                    fee,
                    txtDescription.getText()
            ));
            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Saved Successfully!");
                refreshPage();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Save Failed!");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnSearchOnAction() {
        try {
            TherapyProgramDTO dto = therapyProgramBO.searchProgram(txtSearch.getText());
            if (dto != null) {
                txtProgramId.setText(dto.getProgramId());
                txtProgramName.setText(dto.getProgramName());
                txtDuration.setText(dto.getDuration());
                txtFee.setText(String.valueOf(dto.getFee()));
                txtDescription.setText(dto.getDescription());
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction() {
        if (haveEmptyFields()) return;
        if (!validateData()) return;
        try {
            BigDecimal fee = new BigDecimal(txtFee.getText());

            boolean isUpdated = therapyProgramBO.updateProgram(new TherapyProgramDTO(
                    txtProgramId.getText(),
                    txtProgramName.getText(),
                    txtDuration.getText(),
                    fee,
                    txtDescription.getText()
            ));
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Updated!");
                refreshPage();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed!");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void btnRefreshOnAction() {
        refreshPage();
    }

    private void loadAllPrograms() {
        ObservableList<TherapyProgramTM> tmList = FXCollections.observableArrayList();
        try {
            List<TherapyProgramDTO> allDTOs = therapyProgramBO.getAllPrograms();

            for (TherapyProgramDTO dto : allDTOs) {
                tmList.add(new TherapyProgramTM(
                        dto.getProgramId(),
                        dto.getProgramName(),
                        dto.getDuration(),
                        dto.getFee(),
                        dto.getDescription()
                ));
            }
            tblTherapyProgram.setItems(tmList);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Data loading failed!").show();
        }
    }

    private void fillFields(TherapyProgramTM tm) {
        txtProgramId.setText(tm.getProgramId());
        txtProgramName.setText(tm.getProgramName());
        txtDuration.setText(tm.getDuration());
        txtFee.setText(tm.getFee().toString());
        txtDescription.setText(tm.getDescription());
    }

    private boolean validateData() {

        String ID_PATTERN = "^MT[0-9]{4}$";
        if (!txtProgramId.getText().matches(ID_PATTERN)) {
            showAlert(Alert.AlertType.ERROR, "Invalid ID! (MT1001)");
            return false;
        }
        String NAME_PATTERN = "^[A-z\\s]{3,}$";
        if (!txtProgramName.getText().matches(NAME_PATTERN)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Name!");
            return false;
        }
        String FEE_PATTERN = "^[0-9]+(\\.[0-9]{1,2})?$";
        if (!txtFee.getText().matches(FEE_PATTERN)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Fee Format!");
            return false;
        }
        return true;
    }

    private boolean haveEmptyFields() {

        if (txtProgramId.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "ID field is empty. Enter valid ID! (MT1001)");
            return true;
        }
        if (txtProgramName.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Name field is empty. Enter valid Name!");
            return true;
        }
        if (txtFee.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Fee field is empty. Enter valid Fee!");
            return true;
        }
        if (txtDuration.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Duration field is empty. Enter Program Duration!");
            return true;
        }
        if (txtDescription.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Description field is empty. Enter Description!");
            return true;
        }
        return false;
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    private void refreshPage() {
        txtProgramName.clear();
        txtDuration.clear();
        txtFee.clear();
        txtDescription.clear();
        txtSearch.clear();
        loadAllPrograms();

        try {
            txtProgramId.setText(therapyProgramBO.getNextTherapyProgramPK());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
