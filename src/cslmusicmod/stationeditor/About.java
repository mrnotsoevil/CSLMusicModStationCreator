package cslmusicmod.stationeditor;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class About extends BorderPane {

    @FXML
    private Label version;

    public About() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {
        version.setText("Version " + App.APP_VERSION);
    }

}
