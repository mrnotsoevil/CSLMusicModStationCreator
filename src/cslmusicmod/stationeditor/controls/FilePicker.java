package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.controls.helpers.RememberingFileChooser;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class FilePicker extends HBox {

    @FXML
    private TextField path;

    private RememberingFileChooser fileChooser;

    public FilePicker () {
        ControlsHelper.initControl(this);
        fileChooser = new RememberingFileChooser();
    }

    @FXML
    public void pickFile(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(this.getScene().getWindow());

        if(file != null) {
            path.setText(file.getAbsolutePath());
        }
    }

    public StringProperty textProperty() {
        return path.textProperty();
    }
}
