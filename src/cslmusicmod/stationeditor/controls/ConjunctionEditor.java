package cslmusicmod.stationeditor.controls;

import cslmusicmod.stationeditor.controls.helpers.ControlsHelper;
import cslmusicmod.stationeditor.model.Conjunction;
import cslmusicmod.stationeditor.model.Formula;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.controlsfx.control.GridCell;

import java.util.List;

public class ConjunctionEditor extends GridCell<Conjunction> {

    private Conjunction conjunction;

    @FXML
    private ListView<String> content;

    public ConjunctionEditor() {
        ControlsHelper.initControl(this);
    }

    @FXML
    private void initialize() {

    }

    public void setConjunction(Conjunction conjunction) {
        this.conjunction = conjunction;
    }



}
