package lk.ijse.serenitymentalhealththerepycentersystem.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationUtil {

    public void navigateTo(Pane basePane, String fxmlPath) {
        try {
            basePane.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));

            load.prefWidthProperty().bind(basePane.widthProperty());
            load.prefHeightProperty().bind(basePane.heightProperty());

            basePane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        }
    }

    public void navigateBack(AnchorPane currentPane, String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane newPane = loader.load();

            Stage stage = (Stage) currentPane.getScene().getWindow();
            Scene scene = new Scene(newPane);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
