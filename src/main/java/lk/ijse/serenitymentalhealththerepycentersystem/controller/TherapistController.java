package lk.ijse.serenitymentalhealththerepycentersystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapistProgramTM;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapistTM;

public class TherapistController {

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
    private ImageView imgSearchIcon;

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

    @FXML
    void btnAssignProgramOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnTherapistAvailableOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

}
